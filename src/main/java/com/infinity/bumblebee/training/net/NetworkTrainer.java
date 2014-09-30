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

	private final BumbleMatrix inputData;
	private final BumbleMatrix outputData;
	private final NeuralNetTrainer trainer;
	private final double lambda;
	private final int maxTrainingIterations;
	private final List<IterationCompletionListener> listeners = new ArrayList<>();
	private CostFunction costFunction;

	public NetworkTrainer(NetworkTrainerConfiguration config) {
		MatrixTuple tuple = new TrainingDataLoader().loadData(config);
		
		this.inputData = tuple.getOne();
		this.outputData = tuple.getTwo();
		this.lambda = config.getLambda();
		this.trainer = new NeuralNetTrainer(config.getLayers());
		this.maxTrainingIterations = config.getMaxTrainingIterations();
	}
	
	public NeuralNet train(boolean verbose) {
		
		System.out.println("Training");
		
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
				@Override
				public void onIterationFinished(int iteration, double cost, DoubleVector currentWeights) {
					System.out.println("Iteration #" + iteration + ": cost = " + cost);
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
