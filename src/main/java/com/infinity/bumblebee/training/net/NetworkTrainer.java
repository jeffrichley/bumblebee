package com.infinity.bumblebee.training.net;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
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
		MatrixTuple tuple = new TrainingDataLoader().loadData(config);
		setTrainingTestingAndCrossValidationData(tuple.getOne(), tuple.getTwo());
		
		if (verbose) {
			long end = System.currentTimeMillis();
			System.out.println("completed in " + ((end - start) / 1000) + " seconds with " + trainingData.getData().length + " training samples");
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
		for (int row = 0; row < numTraining; row++) {
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
		
		for (int row = 0; row < numTesting; row++) {
			// set input data
			for (int column = 0; column < input.getColumnDimension(); column++) {
				testingData.setEntry(row, column, entries.get(row).getInput()[column]);
			}
			// set output data
			for (int column = 0; column < out.getColumnDimension(); column++) {
				testingOutputData.setEntry(row, column, entries.get(row).getOutput()[column]);
			}
		}
		
		for (int row = 0; row < numCross; row++) {
			// set input data
			for (int column = 0; column < input.getColumnDimension(); column++) {
				crossValidationData.setEntry(row, column, entries.get(row).getInput()[column]);
			}
			// set output data
			for (int column = 0; column < out.getColumnDimension(); column++) {
				crossValidationOutputData.setEntry(row, column, entries.get(row).getOutput()[column]);
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
