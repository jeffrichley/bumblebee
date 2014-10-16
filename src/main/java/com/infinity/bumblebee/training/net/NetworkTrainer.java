package com.infinity.bumblebee.training.net;

import java.util.ArrayList;
import java.util.List;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.MatrixTuple;
import com.infinity.bumblebee.math.CostFunction;
import com.infinity.bumblebee.math.DoubleVector;
import com.infinity.bumblebee.math.Fmincg;
import com.infinity.bumblebee.math.IterationCompletionListener;
import com.infinity.bumblebee.network.NeuralNet;
import com.infinity.bumblebee.training.NeuralNetTrainer;
import com.infinity.bumblebee.training.NeuralNetTrainerCostFunction;
import com.infinity.bumblebee.util.BumbleMatrixUtils;
import com.infinity.bumblebee.util.MathBridge;

public class NetworkTrainer {

	private final NeuralNetTrainer trainer;
	private final double lambda;
	private final int maxTrainingIterations;
	private final List<IterationCompletionListener> listeners = new ArrayList<>();
	private final NetworkTrainerConfiguration config;

	private BumbleMatrix inputData;
	private BumbleMatrix outputData;
	private CostFunction costFunction;

	public NetworkTrainer(NetworkTrainerConfiguration config) {
		this.config = config;
		this.lambda = config.getLambda();
		this.trainer = new NeuralNetTrainer(config.getLayers());
		this.maxTrainingIterations = config.getMaxTrainingIterations();
	}
	
	public NeuralNet train(boolean verbose) {
		long start = System.currentTimeMillis();
		if (verbose) {
			System.out.print("Loading data...");
		}
		MatrixTuple tuple = new TrainingDataLoader().loadData(config);
		inputData = tuple.getOne();
		outputData = tuple.getTwo();
		
		if (verbose) {
			long end = System.currentTimeMillis();
			System.out.println("completed in " + ((end - start) / 1000) + " seconds with " + inputData.getData().length + " training samples");
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
		costFunction = new NeuralNetTrainerCostFunction(inputData, outputData, lambda, 
																	 outputData.getColumnDimension(), 
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

	public NeuralNetTrainer getNeuralNetTrainer() {
		return trainer;
	}

	public BumbleMatrix getInputData() {
		return inputData;
	}

	public BumbleMatrix getOutputData() {
		return outputData;
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

}
