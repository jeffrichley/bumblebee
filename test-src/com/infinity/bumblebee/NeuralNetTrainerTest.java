package com.infinity.bumblebee;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
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
	public void ensureCostCalculation() {
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
		double cost = cut.calculateCost(X, y, 10);
		
		assertEquals(cost, 0.287629, 0.000001);
	}

}
