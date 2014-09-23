package com.infinity.bumblebee.training.net;

import com.infinity.bumblebee.data.BumbleMatrix;

public class NetworkTrainer {

	private final BumbleMatrix trainingData;

	public NetworkTrainer(NetworkTrainerConfiguration config) {
		trainingData = null;
	}

	public BumbleMatrix getTrainingData() {
		return trainingData;
	}

}
