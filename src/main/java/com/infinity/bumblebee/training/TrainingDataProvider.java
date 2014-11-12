package com.infinity.bumblebee.training;

import com.infinity.bumblebee.data.BumbleMatrix;

public interface TrainingDataProvider {

	BumbleMatrix getIterationX();
	BumbleMatrix getIterationY();
	void incrementIteration();

}
