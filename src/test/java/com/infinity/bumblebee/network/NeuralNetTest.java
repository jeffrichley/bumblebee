package com.infinity.bumblebee.network;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.util.DataReader;

public class NeuralNetTest {
	
	private NeuralNet cut;
	private BumbleMatrix X;
	private BumbleMatrix y;

	@Before
	public void setup() {
		DataReader reader = new DataReader();
		BumbleMatrix theta1 = reader.getMatrixFromFile("./test-data/Theta1.csv");
		BumbleMatrix theta2 = reader.getMatrixFromFile("./test-data/Theta2.csv");
		X = reader.getMatrixFromFile("./test-data/X.csv");
		y = reader.getMatrixFromFile("./test-data/y.csv");
		
		// original y was coded from 1-10 and we need to make it 0-9
		for (int i = 0; i < y.getRowDimension(); i++) {
			double val = y.getRow(i)[0]-1;
			y.setEntry(i, 0, val);
		}

		List<BumbleMatrix> thetas = new ArrayList<BumbleMatrix>();
		thetas.add(theta1);
		thetas.add(theta2);
		
		cut = new NeuralNet(thetas);
	}

	@Test
	public void ensureCanGiveOneNumberAsOutput() {
		BumbleMatrixFactory factory = new BumbleMatrixFactory();
		
		int correctCount = 0;
		for (int i = 0; i < X.getRowDimension(); i++) {
			double[] values = X.getRow(i);
			BumbleMatrix input = factory.createMatrix(new double[][]{values});
			Prediction prediction = cut.predict(input);
			
			if (prediction.getAnswer() == (int)y.getEntry(i, 0)) {
				correctCount++;
			}
		}
		
		assertThat(correctCount, is(greaterThan(4500)));
	}
	
	@Test
	public void ensureCanDetermineHandwrittenNumbers() {
		BumbleMatrixFactory factory = new BumbleMatrixFactory();
		
		int correctCount = 0;
		for (int i = 0; i < X.getRowDimension(); i++) {
			double[] values = X.getRow(i);
			BumbleMatrix input = factory.createMatrix(new double[][]{values});
			BumbleMatrix prediction = cut.calculate(input);
			
			if (isCorrect(prediction, (int) (y.getEntry(i, 0)))) {
				correctCount++;
			}
		}
		
		assertThat(correctCount, is(greaterThan(4500)));
	}

	private boolean isCorrect(BumbleMatrix prediction, int entry) {
		double val = prediction.getEntry(0, entry);
		for (int i = 0; i < prediction.getColumnDimension(); i++) {
			double tmp = prediction.getEntry(0, i);
			if (tmp > val) {
				return false;
			}
		}
		return true;
	}
}
