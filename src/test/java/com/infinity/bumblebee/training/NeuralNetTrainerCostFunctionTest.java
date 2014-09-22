package com.infinity.bumblebee.training;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.util.DataReader;

public class NeuralNetTrainerCostFunctionTest {

	private NeuralNetTrainerCostFunction cut;

	@Before
	public void setup() {
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
		
		NeuralNetTrainer nnt = new NeuralNetTrainer(thetas);
//		cut = new NeuralNetTrainerCostFunction(nnt);
	}
	
}
