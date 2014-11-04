package com.infinity.bumblebee.training.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.data.MatrixTuple;
import com.infinity.bumblebee.math.CostFunction;
import com.infinity.bumblebee.math.DoubleVector;
import com.infinity.bumblebee.math.Fmincg;
import com.infinity.bumblebee.math.IterationCompletionListener;
import com.infinity.bumblebee.network.NeuralNet;
import com.infinity.bumblebee.training.NeuralNetTrainer;
import com.infinity.bumblebee.training.NeuralNetTrainerCostFunction;
import com.infinity.bumblebee.training.data.TrainingEntry;
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
	private BumbleMatrix trainingOutputData;
	private BumbleMatrix testingOutputData;
	private CostFunction costFunction;

	public NetworkTrainer(NetworkTrainerConfiguration config) {
		this.config = config;
		this.lambda = config.getLambda();
		this.trainer = new NeuralNetTrainer(config.getLayers());
		this.maxTrainingIterations = config.getMaxTrainingIterations();
	}
	
	public NeuralNet train(boolean verbose) {
		return train(verbose, 1, 0);
	}
	
	public NeuralNet train(boolean verbose, double percentageForTraining, double percentageForTesting) {
		long start = System.currentTimeMillis();
		if (verbose) {
			System.out.print("Loading data...");
		}
		MatrixTuple tuple = new TrainingDataLoader().loadData(config);
		setTrainingAndTestingData(tuple.getOne(), tuple.getTwo());
		
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
		
		// unroll the thetas
		DoubleVector thetas = mb.convert(bmu.unroll(thetaArray));		
		costFunction = new NeuralNetTrainerCostFunction(trainingData, trainingOutputData, lambda, 
																	 trainingOutputData.getColumnDimension(), 
																	 thetaList);
		
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

	private void setTrainingAndTestingData(BumbleMatrix input, BumbleMatrix out) {
		int numTesting = (int) (input.getRowDimension() * config.getPercentabgeForTesting());
		int numTraining = input.getRowDimension() - numTesting;
		
		BumbleMatrixFactory factory = new BumbleMatrixFactory();
		trainingData = factory.createMatrix(numTraining, input.getColumnDimension());
		testingData = factory.createMatrix(numTesting, input.getColumnDimension());
		trainingOutputData = factory.createMatrix(numTraining, out.getColumnDimension());
		testingOutputData = factory.createMatrix(numTesting, out.getColumnDimension());
		
		List<TrainingEntry> entries = new ArrayList<>();
		for (int row = 0; row < numTraining; row++) {
			TrainingEntry entry = new TrainingEntry(input.getRow(row), out.getRow(row));
			entries.add(entry);
		}
		
		Collections.shuffle(entries);
		
		
		for (int row = 0; row < numTraining; row++) {
			// set input data
			for (int column = 0; column < input.getColumnDimension(); column++) {
				trainingData.setEntry(row, column, entries.get(row).getInput()[column]);
//				trainingData.setEntry(row, column, input.getEntry(row, column));
			}
			// set output data
			for (int column = 0; column < out.getColumnDimension(); column++) {
//				trainingOutputData.setEntry(row, column, out.getEntry(row, column));
				trainingOutputData.setEntry(row, column, entries.get(row).getOutput()[column]);
			}
		}
		
		for (int row = 0; row < numTesting; row++) {
			// set input data
			for (int column = 0; column < input.getColumnDimension(); column++) {
//				testingData.setEntry(row, column, input.getEntry(numTraining + row, column));
				testingData.setEntry(row, column, entries.get(row).getInput()[column]);
			}
			// set output data
			for (int column = 0; column < out.getColumnDimension(); column++) {
//				testingOutputData.setEntry(row, column, out.getEntry(numTraining + row, column));
				testingOutputData.setEntry(row, column, entries.get(row).getOutput()[column]);
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

}
