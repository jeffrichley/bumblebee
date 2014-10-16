package com.infinity.bumblebee.training.net;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.network.NeuralNet;
import com.infinity.bumblebee.network.Prediction;
import com.infinity.bumblebee.training.NeuralNetTrainer;

public class NetworkTrainerTest {

	private NetworkTrainer cut = null;

	@Before
	public void setup() {
		NetworkTrainerConfiguration config = new NetworkTrainerConfiguration();
		config.setTrainingData("./test-data/iris.csv", 100, 0.3, 4, 4, 3);

		cut = new NetworkTrainer(config);
	}

	@Test
	public void ensureKnowsMaxTrainingIterations() {
		assertThat(cut.getMaxTrainingIterations(), is(equalTo(100)));
	}

	@Test
	public void ensureKnowsLambda() {
		assertThat(cut.getLambda(), is(equalTo(0.3)));
	}

	@Test
	public void ensureCanLoadTrainingData() {
		cut.train(false);
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
		NeuralNet network = cut.train(false);

		assertThat(network, is(notNullValue()));

		BumbleMatrixFactory factory = new BumbleMatrixFactory();
		BumbleMatrix x = cut.getInputData();
		BumbleMatrix y = cut.getOutputData();

		int correctCount = 0;
		for (int i = 0; i < x.getRowDimension(); i++) {
			double[] values = x.getRow(i);
			BumbleMatrix input = factory
					.createMatrix(new double[][] { values });
			Prediction prediction = network.predict(input);

			if (prediction.getAnswer() == (int) y.getEntry(i, 0)) {
				correctCount++;
			}
		}

		assertThat(correctCount, is(greaterThan(145)));
	}

}
