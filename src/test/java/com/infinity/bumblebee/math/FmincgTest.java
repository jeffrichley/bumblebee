package com.infinity.bumblebee.math;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.network.NeuralNet;
import com.infinity.bumblebee.training.NeuralNetTrainer;
import com.infinity.bumblebee.training.NeuralNetTrainerCostFunction;
import com.infinity.bumblebee.util.BumbleMatrixUtils;
import com.infinity.bumblebee.util.DataReader;
import com.infinity.bumblebee.util.MathBridge;

public class FmincgTest {
	
	private CostFunction costFunction = null;
	private DoubleVector thetas;
	private BumbleMatrix X; 
	private BumbleMatrix y;
	
	@Before
	public void setup() {
		DataReader reader = new DataReader();
		BumbleMatrixUtils bmu = new BumbleMatrixUtils();
		
//		BumbleMatrix theta1 = reader.getMatrixFromFile("./test-data/Theta1.csv");
//		BumbleMatrix theta2 = reader.getMatrixFromFile("./test-data/Theta2.csv");
//		bmu.printMatrixDetails("read theta1", theta1);
//		bmu.printMatrixDetails("read theta2", theta2);
		
		NeuralNetTrainer t = new NeuralNetTrainer(400, 25, 10);
		BumbleMatrix theta1 = t.getThetas().get(0);
		BumbleMatrix theta2 = t.getThetas().get(1);
//		bmu.printMatrixDetails("new theta1", theta1);
//		bmu.printMatrixDetails("new theta2", theta2);
		
		X = reader.getMatrixFromFile("./test-data/X.csv");
		y = reader.getMatrixFromFile("./test-data/y.csv");
		
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
		
		thetas = mb.convert(bmu.unroll(theta1, theta2));
		
		
	}

	@Test
	@Ignore
	public void ensureRuns() {
		Fmincg min = new Fmincg();
		
		min.addIterationCompletionCallback(new IterationCompletionListener() {
			@Override
			public void onIterationFinished(int iteration, double cost, DoubleVector currentWeights) {
				System.out.println("Iteration #" + iteration + ": cost = " + cost);
			}
		});
		
		DoubleVector minimized = min.minimize(costFunction, thetas, 1000, true);
		
		BumbleMatrixUtils bmu = new BumbleMatrixUtils();
		MathBridge mb = new MathBridge();
		List<BumbleMatrix> ts = bmu.reshape(mb.convert(minimized), new int[]{25, 401, 10, 26});
		
//		new theta1: 25x401
//		------------------------
//		new theta2: 10x26
		
		NeuralNet net = new NeuralNet(ts);
		BumbleMatrixFactory factory = new BumbleMatrixFactory();
		int correctCount = 0;
		for (int i = 0; i < X.getRowDimension(); i++) {
			double[] values = X.getRow(i);
			BumbleMatrix input = factory.createMatrix(new double[][]{values});
			BumbleMatrix prediction = net.calculate(input);
			
			if (isCorrect(prediction, (int) (y.getEntry(i, 0)))) {
				correctCount++;
			}
		}
		
		System.out.println(correctCount);
		System.out.println(X.getRowDimension());
		System.out.println((double)correctCount / (double)X.getRowDimension());
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
