package com.infinity.bumblebee.training;

import com.infinity.bumblebee.data.BumbleMatrix;

public class GradientDecentTrainingDataProvider implements TrainingDataProvider {

	private final BumbleMatrix x;
	private final BumbleMatrix y;

	public GradientDecentTrainingDataProvider(BumbleMatrix x, BumbleMatrix y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public BumbleMatrix getIterationX() {
		return x;
	}

	@Override
	public BumbleMatrix getIterationY() {
		return y;
	}

	@Override
	public void incrementIteration() {
		; // nothing to do on iteration increment
	}

}
