package com.infinity.bumblebee.training;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.math.CostFunction;
import com.infinity.bumblebee.math.DoubleVector;
import com.infinity.bumblebee.math.Fmincg;
import com.infinity.bumblebee.math.IterationCompletionListener;
import com.infinity.bumblebee.network.NeuralNet;
import com.infinity.bumblebee.util.BumbleMatrixUtils;
import com.infinity.bumblebee.util.MathBridge;

public class LearningIntegrationTest {
	
	// utilities
	private MathBridge mb = new MathBridge();
	private BumbleMatrixUtils bmu = new BumbleMatrixUtils();
	private BumbleMatrixFactory factory = new BumbleMatrixFactory();

	@Test
	@Ignore
	// TODO: for some reason large scale problems work like recognizing handwritten digits, but XOR does not work
	public void test() {
		// we need to be able to minimize the thetas
		Fmincg min = new Fmincg();
		min.addIterationCompletionCallback(new IterationCompletionListener() {
			@Override
			public void onIterationFinished(int iteration, double cost, DoubleVector currentWeights) {
				System.out.println("Iteration #" + iteration + ": cost = " + cost);
			}
		});
		
		// the data to learn on - x is the input and y is the expected output
		BumbleMatrix X = factory.createMatrix(new double[][]{{1,1}, {1,0}, {0,1}, {0,0}});
		BumbleMatrix y = factory.createMatrix(new double[][]{{0}, {1}, {1}, {0}});
		
		// theta parameters
		NeuralNetTrainer t = new NeuralNetTrainer(2, 2, 1);
		BumbleMatrix theta1 = t.getThetas().get(0);
		BumbleMatrix theta2 = t.getThetas().get(1);
		List<BumbleMatrix> thetaList = new ArrayList<BumbleMatrix>();
		thetaList.add(theta1);
		thetaList.add(theta2);
		
		NeuralNet original = new NeuralNet(thetaList);
		printPredictions(original);
		
		bmu.printMatrixDetails("theta1", theta1);
		bmu.printMatrixDetails("theta2", theta2);
		
		// unroll the thetas
		DoubleVector thetas = mb.convert(bmu.unroll(theta1, theta2));
		
		// used to calculate the cost for minimization
		CostFunction costFunction = new NeuralNetTrainerCostFunction(/*X, y,*/ 0.3, 1, thetaList, new GradientDecentTrainingDataProvider(X, y));
		DoubleVector minimized = min.minimize(costFunction, thetas, 1000, true);
		
//		List<BumbleMatrix> ts = bmu.reshape(mb.convert(minimized), new int[]{4, 2, 4, 1});
		List<BumbleMatrix> ts = bmu.reshape(mb.convert(minimized), new int[]{2, 3, 1, 3});
//		List<BumbleMatrix> ts = bmu.reshape(mb.convert(minimized), new int[]{2, 3, 2, 3});
		NeuralNet net = new NeuralNet(ts);
		
		// now that we actually have a trained network, lets make sure that it works
		BumbleMatrix oneone = factory.createMatrix(new double[][]{{1,1}});
		BumbleMatrix onezero = factory.createMatrix(new double[][]{{1,0}});
		BumbleMatrix zeroone = factory.createMatrix(new double[][]{{0,1}});
		BumbleMatrix zerozero = factory.createMatrix(new double[][]{{0,0}});
		
		printPredictions(net);
		
		// here are our answers
		BumbleMatrix oneoneAnswer = net.calculate(oneone);
		BumbleMatrix onezeroAnswer = net.calculate(onezero);
		BumbleMatrix zerooneAnswer = net.calculate(zeroone);
		BumbleMatrix zerozeroAnswer = net.calculate(zerozero);
		
		assertThat(oneoneAnswer.getEntry(0, 0), is(lessThan(0.1)));
		assertThat(onezeroAnswer.getEntry(0, 0), is(greaterThan(0.9)));
		assertThat(zerooneAnswer.getEntry(0, 0), is(greaterThan(0.9)));
		assertThat(zerozeroAnswer.getEntry(0, 0), is(lessThan(0.1)));
	}
	
	private void printPredictions(NeuralNet net) {
		// now that we actually have a trained network, lets make sure that it works
		BumbleMatrix oneone = factory.createMatrix(new double[][]{{1,1}});
		BumbleMatrix onezero = factory.createMatrix(new double[][]{{1,0}});
		BumbleMatrix zeroone = factory.createMatrix(new double[][]{{0,1}});
		BumbleMatrix zerozero = factory.createMatrix(new double[][]{{0,0}});
		
		// here are our answers
		BumbleMatrix oneoneAnswer = net.calculate(oneone);
		BumbleMatrix onezeroAnswer = net.calculate(onezero);
		BumbleMatrix zerooneAnswer = net.calculate(zeroone);
		BumbleMatrix zerozeroAnswer = net.calculate(zerozero);
		
		System.out.println(oneone.getEntry(0, 0) + ":" + oneone.getEntry(0, 1) + " -> " + oneoneAnswer.getEntry(0, 0));
		System.out.println(onezero.getEntry(0, 0) + ":" + onezero.getEntry(0, 1) + " -> " + onezeroAnswer.getEntry(0, 0));
		System.out.println(zeroone.getEntry(0, 0) + ":" + zeroone.getEntry(0, 1) + " -> " + zerooneAnswer.getEntry(0, 0));
		System.out.println(zerozero.getEntry(0, 0) + ":" + zerozero.getEntry(0, 1) + " -> " + zerozeroAnswer.getEntry(0, 0));
	}

}
