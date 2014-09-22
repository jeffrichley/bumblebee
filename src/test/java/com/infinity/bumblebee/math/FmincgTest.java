package com.infinity.bumblebee.math;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.training.NeuralNetTrainerCostFunction;
import com.infinity.bumblebee.util.BumbleMatrixUtils;
import com.infinity.bumblebee.util.DataReader;
import com.infinity.bumblebee.util.MathBridge;

public class FmincgTest {
	
	private CostFunction costFunction = null;
	private DoubleVector thetas;
	
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

		List<BumbleMatrix> thetaList = new ArrayList<BumbleMatrix>();
		thetaList.add(theta1);
		thetaList.add(theta2);
		
//		NeuralNetTrainer nnt = new NeuralNetTrainer(thetaList);
		costFunction = new NeuralNetTrainerCostFunction(X, y, 1, 10, thetaList);
		
		MathBridge mb = new MathBridge();
		BumbleMatrixUtils bmu = new BumbleMatrixUtils();
		
		thetas = mb.convert(bmu.unroll(theta1, theta2));
	}

	@Test
	public void ensureRuns() {
		Fmincg min = new Fmincg();
		
		min.addIterationCompletionCallback(new IterationCompletionListener() {
			
			@Override
			public void onIterationFinished(int iteration, double cost, DoubleVector currentWeights) {
				System.out.println("Iteration #" + iteration + ": cost = " + cost);
			}
		});
		
		DoubleVector minimized = min.minimize(costFunction, thetas, 1000, true);
	}

}
