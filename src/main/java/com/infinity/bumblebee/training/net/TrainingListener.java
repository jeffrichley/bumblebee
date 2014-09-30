package com.infinity.bumblebee.training.net;

import com.infinity.bumblebee.math.DoubleVector;

public interface TrainingListener {

	void onIterationFinished(int iteration, double cost, DoubleVector currentWeights);

}
