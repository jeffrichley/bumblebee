package com.infinity.bumblebee.training.net;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.google.gson.Gson;
import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.data.MTJMatrix;
import com.infinity.bumblebee.data.MatrixTuple;
import com.infinity.bumblebee.exceptions.BumbleException;
import com.infinity.bumblebee.math.CostFunction;
import com.infinity.bumblebee.math.DoubleVector;
import com.infinity.bumblebee.math.Fmincg;
import com.infinity.bumblebee.math.IterationCompletionListener;
import com.infinity.bumblebee.network.NeuralNet;
import com.infinity.bumblebee.training.GradientDecentTrainingDataProvider;
import com.infinity.bumblebee.training.NeuralNetTrainer;
import com.infinity.bumblebee.training.NeuralNetTrainerCostFunction;
import com.infinity.bumblebee.training.StochasticGradientDecentTrainingDataProvider;
import com.infinity.bumblebee.training.TrainingDataProvider;
import com.infinity.bumblebee.training.data.TrainingEntry;
import com.infinity.bumblebee.util.BumbleMatrixMarshaller;
import com.infinity.bumblebee.util.BumbleMatrixUnmarshaller;
import com.infinity.bumblebee.util.BumbleMatrixUtils;
import com.infinity.bumblebee.util.MathBridge;

public class NetworkTrainer {

	private final NeuralNetTrainer trainer;
	private final double lambda;
	private final int maxTrainingIterations;
	private final List<IterationCompletionListener> listeners = new ArrayList<>();
	private final NetworkTrainerConfiguration config;

	private BumbleMatrix trainingData;
	private BumbleMatrix testingData;
	private BumbleMatrix crossValidationData;
	private BumbleMatrix trainingOutputData;
	private BumbleMatrix testingOutputData;
	private BumbleMatrix crossValidationOutputData;
	private CostFunction costFunction;

	public NetworkTrainer(NetworkTrainerConfiguration config) {
		this.config = config;
		this.lambda = config.getLambda();
		if (config.getPreviousTrainingFile() == null) {
			this.trainer = new NeuralNetTrainer(config.getLayers());
		} else {
			BumbleMatrixUnmarshaller unmarshaller = new BumbleMatrixUnmarshaller();
			try {
				FileReader in = new FileReader(config.getPreviousTrainingFile());
				NeuralNet network = unmarshaller.unmarshal(in);
				this.trainer = new NeuralNetTrainer(network.getThetas());
			} catch (FileNotFoundException e) {
				throw new BumbleException("Unable to read network file: " + config.getPreviousTrainingFile(), e);
			}
		}
		this.maxTrainingIterations = config.getMaxTrainingIterations();
	}
	
	public NeuralNet train() {
		return train(false);
	}
	
	public NeuralNet train(boolean verbose) {
		long start = System.currentTimeMillis();
		if (verbose) {
			System.out.print("Loading data...");
		}
		
		if (config.getTrainingGroupsFile() == null) {
			MatrixTuple tuple = new TrainingDataLoader().loadData(config);
			BumbleMatrix input = tuple.getOne();
			BumbleMatrix output = tuple.getTwo();
			
			if (config.getNormalizationMethod() != null) {
				if (verbose) {
					System.out.print("Normalizing data...");
				}
				input = config.getNormalizationMethod().normalize(input);
			}
			
			setTrainingTestingAndCrossValidationData(input, output);
			if (config.getTrainingDataSaveFile() != null) {
				saveTrainingData(config.getTrainingDataSaveFile());
			}
		} else {
			setTrainingTestingAndCrossValidationData(config.getTrainingGroupsFile());
		}
		
		if (verbose) {
			long end = System.currentTimeMillis();
			System.out.println("completed in " + ((end - start) / 1000) + " seconds");
			System.out.println(trainingData.getData().length + " training samples");
			System.out.println(testingData.getData().length + " testing samples");
			System.out.println(crossValidationData.getData().length + " cross validation samples");
			System.out.println("Training");
		}
		
		MathBridge mb = new MathBridge();
		BumbleMatrixUtils bmu = new BumbleMatrixUtils();
		
		List<BumbleMatrix> thetaList = trainer.getThetas();
		BumbleMatrix[] thetaArray = new BumbleMatrix[thetaList.size()];
		int[] thetaSizes = new int[thetaList.size() * 2];
		for (int i = 0; i < thetaList.size(); i++) {
			BumbleMatrix theta = thetaList.get(i);
			thetaArray[i] = theta;
			thetaSizes[i*2] = theta.getRowDimension();
			thetaSizes[i*2+1] = theta.getColumnDimension();
		}
		
		TrainingDataProvider trainingDataProvider = null;
		switch (config.getTrainingDataProviderType()) {
		case GRADIENT_DECENT:
			trainingDataProvider = new GradientDecentTrainingDataProvider(trainingData, trainingOutputData);
			break;
			
		case STOCHASTIC_GRADIENT_DECENT:
			trainingDataProvider = new StochasticGradientDecentTrainingDataProvider(trainingData, trainingOutputData);
			break;

		default:
			break;
		}
		
		// unroll the thetas
		DoubleVector thetas = mb.convert(bmu.unroll(thetaArray));		
		costFunction = new NeuralNetTrainerCostFunction(/*trainingData, trainingOutputData,*/ lambda, 
																	 trainingOutputData.getColumnDimension(), 
																	 thetaList,
																	 trainingDataProvider);
		
		Fmincg min = new Fmincg();
		if (verbose) {
			min.addIterationCompletionCallback(new IterationCompletionListener() {
				
				private long iterationStarted = System.currentTimeMillis();
				
				@Override
				public void onIterationFinished(int iteration, double cost, DoubleVector currentWeights) {
					long iterationEnded = System.currentTimeMillis();
					
					long time = (iterationEnded - iterationStarted) / 1000;
					System.out.println("Iteration #" + iteration + " cost = " + cost + " time in seconds: " + time);
					
					iterationStarted = iterationEnded;
				}
			});
		}
		for (IterationCompletionListener listener : listeners) {
			min.addIterationCompletionCallback(listener);
		}
		DoubleVector minimized = min.minimize(costFunction, thetas, maxTrainingIterations, verbose);
		
		List<BumbleMatrix> ts = bmu.reshape(mb.convert(minimized), thetaSizes);
		NeuralNet net = new NeuralNet(ts);
		
		return net;
	}

	private void saveTrainingData(String trainingDataSaveFile) {
		Gson gson = new Gson();
		try (FileOutputStream fos = new FileOutputStream(trainingDataSaveFile);
			 BufferedOutputStream bos = new BufferedOutputStream(fos);
			 ZipOutputStream zos = new ZipOutputStream(bos);) {
			
			zos.putNextEntry(new ZipEntry("trainingData.json"));
	        String json = gson.toJson(trainingData, MTJMatrix.class);
			zos.write(json.getBytes());
			zos.closeEntry();
			
			zos.putNextEntry(new ZipEntry("testingData.json"));
	        zos.write(gson.toJson(testingData, MTJMatrix.class).getBytes());
			zos.closeEntry();
			
			zos.putNextEntry(new ZipEntry("crossValidationData.json"));
	        zos.write(gson.toJson(crossValidationData, MTJMatrix.class).getBytes());
			zos.closeEntry();
			
			zos.putNextEntry(new ZipEntry("trainingOutputData.json"));
	        zos.write(gson.toJson(trainingOutputData, MTJMatrix.class).getBytes());
			zos.closeEntry();
			
			zos.putNextEntry(new ZipEntry("testingOutputData.json"));
	        zos.write(gson.toJson(testingOutputData, MTJMatrix.class).getBytes());
			zos.closeEntry();
			
			zos.putNextEntry(new ZipEntry("crossValidationOutputData.json"));
	        zos.write(gson.toJson(crossValidationOutputData, MTJMatrix.class).getBytes());
			zos.closeEntry();
		} catch (IOException e) {
			throw new BumbleException("Unable to save training data: " + trainingDataSaveFile, e);
		}
	}

	private void setTrainingTestingAndCrossValidationData(String trainingGroupsFile) {
		Gson gson = new Gson();
		
		try (FileInputStream fin = new FileInputStream(trainingGroupsFile);
	         ZipInputStream zin = new ZipInputStream(fin);) {
			
			trainingData = gson.fromJson(getNextEntryAsString(zin), MTJMatrix.class);
			testingData = gson.fromJson(getNextEntryAsString(zin), MTJMatrix.class);
			crossValidationData = gson.fromJson(getNextEntryAsString(zin), MTJMatrix.class);
			trainingOutputData = gson.fromJson(getNextEntryAsString(zin), MTJMatrix.class);
			testingOutputData = gson.fromJson(getNextEntryAsString(zin), MTJMatrix.class);
			crossValidationOutputData = gson.fromJson(getNextEntryAsString(zin), MTJMatrix.class);
		} catch (IOException e) {
			throw new BumbleException("Unable to read training configuration: " + trainingGroupsFile, e);
		}
	}
	
	private String getNextEntryAsString(ZipInputStream zin) throws IOException {
		zin.getNextEntry();
		BufferedReader in = new BufferedReader(new InputStreamReader(zin));
		String data = in.readLine();
		zin.closeEntry();
		return data;
	}

	private void setTrainingTestingAndCrossValidationData(BumbleMatrix input, BumbleMatrix out) {
		int numTraining = (int) (input.getRowDimension() * config.getPercentageForTraining());
		int numTesting = (int) (input.getRowDimension() * config.getPercentabgeForTesting());
		int numCross = (int) (input.getRowDimension() * config.getPercentageForCrossValidation());
		
		BumbleMatrixFactory factory = new BumbleMatrixFactory();
		trainingData = factory.createMatrix(numTraining, input.getColumnDimension());
		testingData = factory.createMatrix(numTesting, input.getColumnDimension());
		crossValidationData = factory.createMatrix(numCross, input.getColumnDimension());
		trainingOutputData = factory.createMatrix(numTraining, out.getColumnDimension());
		testingOutputData = factory.createMatrix(numTesting, out.getColumnDimension());
		crossValidationOutputData = factory.createMatrix(numCross, out.getColumnDimension());
		
		// shuffle the training set to optimize training efficiency
		List<TrainingEntry> entries = new ArrayList<>();
		for (int row = 0; row < input.getRowDimension(); row++) {
			TrainingEntry entry = new TrainingEntry(input.getRow(row), out.getRow(row));
			entries.add(entry);
		}
		
		Collections.shuffle(entries);
		
		// fill the data sets
		for (int row = 0; row < numTraining; row++) {
			// set input data
			for (int column = 0; column < input.getColumnDimension(); column++) {
				trainingData.setEntry(row, column, entries.get(row).getInput()[column]);
			}
			// set output data
			for (int column = 0; column < out.getColumnDimension(); column++) {
				trainingOutputData.setEntry(row, column, entries.get(row).getOutput()[column]);
			}
		}
		
//		int trainingInputRowIndex = 0;
//		int trainingOutputRowIndex = 0;
//		for (int row = numTraining; row < numTesting + numTraining - 1; row++) {
		for (int row = 0; row < numTesting; row++) {
			// set input data
			TrainingEntry trainingEntry = entries.get(row + numTraining);
			for (int column = 0; column < input.getColumnDimension(); column++) {
				double value = trainingEntry.getInput()[column];
				testingData.setEntry(row, column, value);
			}
			// set output data
			for (int column = 0; column < out.getColumnDimension(); column++) {
				testingOutputData.setEntry(row, column, trainingEntry.getOutput()[column]);
			}
		}
		
//		int crossValidationInputRowIndex = 0;
//		int crossValidationOutputRowIndex = 0;
//		for (int row = numTraining + numTesting; row < numCross + numTesting + numTraining - 1; row++) {
		for (int row = 0; row < numCross; row++) {
			// set input data
			TrainingEntry trainingEntry = entries.get(row + numTesting + numTraining);
			for (int column = 0; column < input.getColumnDimension(); column++) {
				crossValidationData.setEntry(row, column, trainingEntry.getInput()[column]);
			}
			// set output data
			for (int column = 0; column < out.getColumnDimension(); column++) {
				crossValidationOutputData.setEntry(row, column, trainingEntry.getOutput()[column]);
			}
		}
	}

	public NeuralNetTrainer getNeuralNetTrainer() {
		return trainer;
	}

	public BumbleMatrix getInputData() {
		return trainingData;
	}

	public BumbleMatrix getOutputData() {
		return trainingOutputData;
	}

	public Double getLambda() {
		return lambda;
	}

	public int getMaxTrainingIterations() {
		return maxTrainingIterations;
	}
	
	public void addListener(IterationCompletionListener listener) {
		listeners.add(listener);
	}

	public NeuralNet getCurrentNetwork() {
		return costFunction.getCurrentNetwork();
	}

	public BumbleMatrix getTestingData() {
		return testingData;
	}

	public BumbleMatrix getTestingOutputData() {
		return testingOutputData;
	}

	public BumbleMatrix getCrossValidationData() {
		return crossValidationData;
	}

	public BumbleMatrix getCrossValidationOutputData() {
		return crossValidationOutputData;
	}

}
