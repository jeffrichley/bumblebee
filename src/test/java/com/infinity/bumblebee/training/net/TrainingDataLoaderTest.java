package com.infinity.bumblebee.training.net;

import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;

public class TrainingDataLoaderTest {

	@Test
	public void ensureLoadsData() {
		NetworkTrainerConfiguration config = new NetworkTrainerConfiguration();
		config.setTrainingData("./test-data/iris.csv", 4, 1);
		
		TrainingDataLoader loader = new TrainingDataLoader();
		BumbleMatrix data = loader.loadData(config);
		
	}
	
}
