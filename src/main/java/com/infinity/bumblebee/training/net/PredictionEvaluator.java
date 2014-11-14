package com.infinity.bumblebee.training.net;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.network.NeuralNet;

public class PredictionEvaluator {

	private int answerCorrect = 0;
	private int aswersAttempted = 0;

	public PredictionEvaluator(BumbleMatrix input, BumbleMatrix output, NeuralNet net) {
		final BumbleMatrixFactory factory = new BumbleMatrixFactory();
		
		for (int row = 0; row < input.getRowDimension(); row++) {
			double[] inputArray = input.getRow(row);
			BumbleMatrix oneInput = factory.createMatrix(new double[][]{inputArray});
			
			//TODO: need to implement for multiple outputs also
			BumbleMatrix answer = net.calculate(oneInput);
			if (answer.getEntry(0, 0) >= .5 && output.getEntry(row, 0) > 0.99) {
				answerCorrect++;
			} else if (answer.getEntry(0, 0) < .5 && output.getEntry(row, 0) < 0.01) {
				answerCorrect++;
			}
			
			aswersAttempted++;
		}
	}

	public double getPercentageCorrect() {
		return ((double) answerCorrect / (double) aswersAttempted) * 100;
	}
	
}
