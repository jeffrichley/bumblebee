package com.infinity.bumblebee.training.net;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.training.NeuralNet;
import com.infinity.bumblebee.training.NeuralNetTrainer;

public class NetworkTrainerTest {
	
	private NetworkTrainer cut = null;
	
	@Before
	public void setup() {
		NetworkTrainerConfiguration config = new NetworkTrainerConfiguration();
		config.setTrainingData("./test-data/iris.csv", 1000, 0.3, 4, 4, 1);
		
		cut = new NetworkTrainer(config);
	}

	@Test
	public void ensureKnowsMaxTrainingIterations() {
		assertThat(cut.getMaxTrainingIterations(), is(equalTo(1000)));
	}
	
	@Test
	public void ensureKnowsLambda() {
		assertThat(cut.getLambda(), is(equalTo(0.3)));
	}
	
	@Test
	public void ensureCanLoadTrainingData() {
		 BumbleMatrix inptutData = cut.getInputData();
		 BumbleMatrix outputData = cut.getOutputData();
		 
		 assertThat(inptutData, is(notNullValue()));
		 assertThat(outputData, is(notNullValue()));
	}
	
	@Test
	public void ensureCanCreateNetworkTrainer() {
		NeuralNetTrainer trainer = cut.getNeuralNetTrainer();
		List<BumbleMatrix> thetas = trainer.getThetas();
		
		assertThat(trainer, is(notNullValue()));
		assertThat(thetas, is(notNullValue()));
		assertThat(thetas.size(), is(equalTo(2)));
	}
	
	@Test
	public void ensureCanTrain() {
		NeuralNet network = cut.train(true);
		
		assertThat(network, is(notNullValue()));
	}

}
