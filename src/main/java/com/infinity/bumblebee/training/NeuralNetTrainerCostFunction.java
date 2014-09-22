package com.infinity.bumblebee.training;

import java.util.List;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.IntegerTuple;
import com.infinity.bumblebee.data.TrainingTuple;
import com.infinity.bumblebee.math.CostFunction;
import com.infinity.bumblebee.math.CostGradientTuple;
import com.infinity.bumblebee.math.DoubleVector;
import com.infinity.bumblebee.util.BumbleMatrixUtils;
import com.infinity.bumblebee.util.MathBridge;

public class NeuralNetTrainerCostFunction implements CostFunction {

	private final BumbleMatrix x;
	private final BumbleMatrix y;
	private final BumbleMatrixUtils bmu = new BumbleMatrixUtils();
	private final MathBridge mathBridge = new MathBridge();
	private final double lambda;
	private List<BumbleMatrix> thetas;
	private final int numLabels;

	public NeuralNetTrainerCostFunction(BumbleMatrix x, BumbleMatrix y, double lambda, int numLabels, List<BumbleMatrix> thetas) {
		this.x = x;
		this.y = y;
		this.lambda = lambda;
		this.thetas = thetas;
		this.numLabels = numLabels;
	}

	@Override
	public CostGradientTuple evaluateCost(DoubleVector input) {
//		System.out.println("evaluate");
		
		NeuralNetTrainer trainer = new NeuralNetTrainer(thetas);
		
		int numberOfThetas = trainer.getNumberOfThetas();
		int[] sizes = new int[numberOfThetas*2];
		for (int i = 0; i < numberOfThetas; i++) {
			IntegerTuple sizeOfTheta = trainer.getSizeOfTheta(i);
			sizes[i*2] = sizeOfTheta.getOne();
			sizes[i*2+1] = sizeOfTheta.getTwo();
		}
		
		BumbleMatrix original = mathBridge.convert(input);
		List<BumbleMatrix> thetas = bmu.reshape(original, sizes);
		
		trainer.setThetas(thetas);
		
		TrainingTuple training = trainer.calculateCost(x, y, numLabels, lambda);
		
		BumbleMatrix unroll = bmu.unroll(training.getGradients());
		DoubleVector gradient = mathBridge.convert(unroll);
		CostGradientTuple answer = new CostGradientTuple(training.getCost(), gradient );
		
		return answer;
	}

}
