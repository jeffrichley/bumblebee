package com.infinity.bumblebee.training;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.TrainingTuple;
import com.infinity.bumblebee.util.DataReader;

public class NeuralNetTrainerTest {

	private NeuralNetTrainer twoOne;
	private NeuralNetTrainer threeTwoOne;
	
	@Before
	public void setUp() throws Exception {
		twoOne = new NeuralNetTrainer(2, 1);
		threeTwoOne = new NeuralNetTrainer(3, 2, 1);
	}

	@Test
	public void ensureNumberOfLayers() {
		assertThat(twoOne.getNumberOfThetas(), is(equalTo(1)));
		assertThat(threeTwoOne.getNumberOfThetas(), is(equalTo(2)));
	}
	
	@Test
	public void ensureThetasAreRandomized() {
		List<BumbleMatrix> thetas = twoOne.getThetas();
		for (BumbleMatrix theta : thetas) {
			for (int row = 0; row < theta.getRowDimension(); row++) {
				for (int column = 0; column < theta.getColumnDimension(); column++) {
					assertThat(theta.getEntry(row, column), is(not(equalTo(0d))));
				}
			}
		}
	}
	
	@Test
	public void ensureProperSizingOfTheta() {
		// a 2 input and 1 input should have a theta of 1x3
		assertThat(twoOne.getSizeOfTheta(0).getOne(), is(equalTo(1)));
		assertThat(twoOne.getSizeOfTheta(0).getTwo(), is(equalTo(3)));
		
		// a 3 input 2 hidden and 1 output should have
		// two thetas of size 2x4 and 1x3
		assertThat(threeTwoOne.getSizeOfTheta(0).getOne(), is(equalTo(2)));
		assertThat(threeTwoOne.getSizeOfTheta(0).getTwo(), is(equalTo(4)));
		assertThat(threeTwoOne.getSizeOfTheta(1).getOne(), is(equalTo(1)));
		assertThat(threeTwoOne.getSizeOfTheta(1).getTwo(), is(equalTo(3)));
	}
	
	@Test
	public void ensureLambdaSettable() {
		// lambda should start at 0
		assertThat(twoOne.getLambda(), is(equalTo(0d)));
		
		twoOne.setLambda(1.1);
		assertThat(twoOne.getLambda(), is(equalTo(1.1d)));
	}
	
	@Test
	public void ensureCostCalculationWithZeroLambda() {
		TrainingTuple training = process(0);
		
		assertEquals(training.getCost(), 0.287629, 0.000001);
	}
	
	@Test
	public void ensureCostCalculationWithNonZeroLambda() {
		TrainingTuple training = process(1);
		assertEquals(0.383770, training.getCost(), 0.001);
		
		training = process(3);
		assertEquals(0.578, training.getCost(), 0.001);
	}
	
	@Test
	public void ensureBackprop() {
		TrainingTuple training = process(1);
		
//		without regularization
//		assertEquals(6.1871e-05, training.getGradients().get(0).getEntry(0, 0), 0.000001);
//		assertEquals(6.2874e-04, training.getGradients().get(1).getEntry(0, 0), 0.000001);
//		assertEquals(0, training.getGradients().get(0).getEntry(0, 1), 0.000001);
//		assertEquals(7.5095e-04, training.getGradients().get(1).getEntry(0, 1), 0.000001);
		
		// with regularization
		assertEquals(6.1871e-05, training.getGradients().get(0).getEntry(0, 0), 0.00000001);
		assertEquals(-2.1125e-12, training.getGradients().get(0).getEntry(0, 1), 0.00000001);
		assertEquals(6.2874e-04, training.getGradients().get(1).getEntry(0, 0), 0.00000001);
		assertEquals(5.0846e-04, training.getGradients().get(1).getEntry(0, 1), 0.00000001);
	}

	private TrainingTuple process(double lambda) {
		DataReader reader = new DataReader();
		BumbleMatrix theta1 = reader.getMatrixFromFile("./test-data/Theta1.csv");
		BumbleMatrix theta2 = reader.getMatrixFromFile("./test-data/Theta2.csv");
		BumbleMatrix X = reader.getMatrixFromFile("./test-data/X.csv");
		BumbleMatrix y = reader.getMatrixFromFile("./test-data/y.csv");
		
		// original y was coded from 1-10 and we need to make it 0-9
		for (int i = 0; i < y.getRowDimension(); i++) {
			double val = y.getRow(i)[0]-1;
			y.setEntry(i, 0, val);
		}

		List<BumbleMatrix> thetas = new ArrayList<BumbleMatrix>();
		thetas.add(theta1);
		thetas.add(theta2);
		
		NeuralNetTrainer cut = new NeuralNetTrainer(thetas);
		return cut.calculateCost(X, y, 10, lambda);
	}

}
