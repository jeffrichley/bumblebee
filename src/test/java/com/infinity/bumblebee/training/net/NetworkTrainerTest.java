package com.infinity.bumblebee.training.net;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.training.net.NetworkTrainer;

public class NetworkTrainerTest {
	
	private NetworkTrainer cut = null;
	
	@Before
	public void setup() {
		NetworkTrainerConfiguration config = new NetworkTrainerConfiguration();
		config.setTrainingData("./test-data/iris.csv", 4, 1);
		
		cut = new NetworkTrainer(config);
	}

	@Test
	public void ensureCanLoadTrainingData() {
		 BumbleMatrix trainingData = cut.getTrainingData();
	}

}
