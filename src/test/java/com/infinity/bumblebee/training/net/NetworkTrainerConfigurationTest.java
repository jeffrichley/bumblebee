package com.infinity.bumblebee.training.net;

import static com.infinity.bumblebee.training.net.NetworkDSL.usingTrainingData;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.network.NeuralNet;
import com.infinity.bumblebee.training.net.NetworkDSL.NetworkDSLTrainer;

public class NetworkTrainerConfigurationTest {

	@Test
	public void ensureReasonableDefaults() {
		NetworkDSLTrainer trainingData = usingTrainingData("./test-data/iris.csv")
											 .havingLayers(4, 4, 3);
		
		NetworkTrainerConfiguration dslConfig = trainingData.getConfiguration();
		
		assertThat(dslConfig.getMaxTrainingIterations(), is(equalTo(100)));
		assertThat(dslConfig.getLambda(), is(closeTo(0.3, 0.0001)));
	}
	
	@Test
	public void ensureCanConfigure() {
		NetworkDSLTrainer trainingData = usingTrainingData("./test-data/iris.csv")
										.havingLayers(4, 4, 3)
										.atMostIterations(1000)
										.withLearningRate(0.8);
		
		NetworkTrainerConfiguration dslConfig = trainingData.getConfiguration();
		
		assertThat(dslConfig.getTestDataFileName(), is(equalTo("./test-data/iris.csv")));
		assertThat(dslConfig.getLayers()[0], is(equalTo(4)));
		assertThat(dslConfig.getLayers()[1], is(equalTo(4)));
		assertThat(dslConfig.getLayers()[2], is(equalTo(3)));
		
		assertThat(dslConfig.getColumnsOfInputs(), is(equalTo(4)));
		assertThat(dslConfig.getColumnsOfExpectedOuputs(), is(equalTo(3)));
		
		assertThat(dslConfig.getMaxTrainingIterations(), is(equalTo(1000)));
		assertThat(dslConfig.getLambda(), is(closeTo(0.8, 0.0001)));
	}
	
	@Test
	public void ensureCanTrain() {
		NeuralNet network = usingTrainingData("./test-data/iris.csv")
				 				.havingLayers(4, 4, 3)
				 				.atMostIterations(1000)
				 				.withLearningRate(0.3)
				 				.train();
		
		BumbleMatrixFactory factory = new BumbleMatrixFactory();
		BumbleMatrix zeroInput = factory.createMatrix(new double[][]{{5.1, 3.5, 1.4, 0.2}});
		BumbleMatrix oneInput = factory.createMatrix(new double[][]{{6.1 ,2.9, 4.7, 1.4}});
		BumbleMatrix twoInput = factory.createMatrix(new double[][]{{6.2, 2.8, 4.8, 1.8}});
		
		assertThat(network.predict(zeroInput).getAnswer(), is(equalTo(0)));
		assertThat(network.predict(oneInput).getAnswer(), is(equalTo(1)));
		assertThat(network.predict(twoInput).getAnswer(), is(equalTo(2)));
	}
	
	@Test
	public void ensureCanSaveProgress() {
		String sep = System.getProperty("file.separator");
		String saveDir = System.getProperty("user.home") + sep+"tmp"+sep+"bumble"+sep+"save";
		NetworkDSLTrainer trainingData = usingTrainingData("./test-data/iris.csv")
							 				.havingLayers(4, 4, 3)
							 				.savingProgress(saveDir);
		
		NetworkTrainerConfiguration dslConfig = trainingData.getConfiguration();
		
		assertThat(dslConfig.getProgressSaveDirectory().getAbsolutePath(), is(equalTo(saveDir)));
	}
	
	@Test
	public void ensureCanSaveAfterTraining() {
		String sep = System.getProperty("file.separator");
		String saveDir = System.getProperty("user.home") + sep+"tmp"+sep+"bumble"+sep+"complete";
		NetworkDSLTrainer trainingData = usingTrainingData("./test-data/iris.csv")
							 				.havingLayers(4, 4, 3)
							 				.savingWhenComplete(saveDir);
		
		NetworkTrainerConfiguration dslConfig = trainingData.getConfiguration();
		
		assertThat(dslConfig.getCompleteSaveDirectory().getAbsolutePath(), is(equalTo(saveDir)));
	}
	
	public static void main(String[] args) {
		String sep = System.getProperty("file.separator");
		String saveDir = System.getProperty("user.home") + sep+"tmp"+sep+"bumble"+sep+"complete";
		NeuralNet network = usingTrainingData("./test-data/iris.csv")
							 				.havingLayers(4, 4, 3)
							 				.savingProgress(saveDir)
							 				.savingWhenComplete(saveDir)
							 				.train(true);
	}

}
