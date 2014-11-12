package com.infinity.bumblebee.training;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;

public class StocasticGradientDecentTrainingDataProvider implements TrainingDataProvider {

	private final BumbleMatrix x;
	private final BumbleMatrix y;
	private final BumbleMatrixFactory factory = new BumbleMatrixFactory();
	
	private int iteration = 0;

	public StocasticGradientDecentTrainingDataProvider(BumbleMatrix x, BumbleMatrix y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public BumbleMatrix getIterationX() {
		double[] row = x.getRow(iteration);
		return factory.createMatrix(new double[][]{row});
	}

	@Override
	public BumbleMatrix getIterationY() {
		double[] row = y.getRow(iteration);
		return factory.createMatrix(new double[][]{row});
	}

	@Override
	public void incrementIteration() {
		iteration++;
		if (iteration >= x.getRowDimension()) {
			iteration = 0;
		}
	}

}
