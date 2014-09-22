package com.infinity.bumblebee.functions;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.util.BumbleMatrixUtils;

public class GradientRegularizationFunction {
	
	private int numSamples;
	private double lambda;
	
	public GradientRegularizationFunction(int numSamples, double lambda) {
		this.numSamples = numSamples;
		this.lambda = lambda;
	}

	public BumbleMatrix calculate(BumbleMatrix theta, BumbleMatrix thetaGradient) {
		BumbleMatrixUtils bmu = new BumbleMatrixUtils();
		
		BumbleMatrix first = bmu.scalarDivide(bmu.removeFirstColumn(thetaGradient), numSamples);
		BumbleMatrix second = bmu.scalarMultiply(bmu.removeFirstColumn(theta), lambda/numSamples);
		BumbleMatrix combined = bmu.elementWiseAddition(first, second);
		
		BumbleMatrix reg = bmu.onesColumnAdded(combined);
		for (int row = 0; row < reg.getRowDimension(); row++) {
			double value = thetaGradient.getEntry(row, 0) / numSamples;
			reg.setEntry(row, 0, value);
		}
		
		return reg;
	}


}
