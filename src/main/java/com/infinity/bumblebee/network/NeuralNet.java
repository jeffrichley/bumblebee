package com.infinity.bumblebee.network;

import java.util.Iterator;
import java.util.List;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.functions.MatrixFunction;
import com.infinity.bumblebee.functions.SigmoidFunction;
import com.infinity.bumblebee.util.BumbleMatrixUtils;

public class NeuralNet {

	private final List<BumbleMatrix> thetas;
	private final MatrixFunction function = new SigmoidFunction();

	public NeuralNet(List<BumbleMatrix> thetas) {
		this.thetas = thetas;
	}

	public BumbleMatrix calculate(BumbleMatrix input) {
		BumbleMatrixUtils bmu = new BumbleMatrixUtils();

		BumbleMatrix a = bmu.onesColumnAdded(input);
		// bmu.printMatrixDetails("a", a);

		Iterator<BumbleMatrix> iter = thetas.iterator();
		while (iter.hasNext()) {
			BumbleMatrix theta = iter.next();

			BumbleMatrix z = theta.multiply(a.transpose());
			a = function.calculate(z);

			if (iter.hasNext()) {
				a = bmu.onesColumnAdded(a.transpose());
			}
		}

		a = a.transpose();
		
		return a;
	}

	public Prediction predict(BumbleMatrix input) {
		BumbleMatrix calculated = calculate(input);
		
		int winner = Integer.MIN_VALUE;
		double score = Integer.MIN_VALUE;
		
		for (int i = 0; i < calculated.getColumnDimension(); i++) {
			double tmpScore = calculated.getEntry(0, i);
			if (tmpScore > score) {
				score = tmpScore;
				winner = i;
			}
		}
		
		return new Prediction(winner, score);
	}
}
