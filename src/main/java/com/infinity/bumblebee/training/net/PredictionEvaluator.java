package com.infinity.bumblebee.training.net;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.network.NeuralNet;

/**
 * Gives various statistics about the evaluation of a <code>NeuralNet</code>'s ability for prediction
 * @author Jeffrey.Richley
 */
public class PredictionEvaluator {

	private int truePositives = 0;
	private int trueNegatives = 0;
	private int falsePositives = 0;
	private int falseNegatives = 0;
	private int aswersAttempted = 0;
	private double cost = 0;

	/**
	 * Constructor that performs evaluation
	 * @param input The input examples to predict with
	 * @param output The expected output of the predictions
	 * @param net The <code>NeuralNet</code> that is used to perform predictions
	 */
	public PredictionEvaluator(BumbleMatrix input, BumbleMatrix output, NeuralNet net) {
		final BumbleMatrixFactory factory = new BumbleMatrixFactory();
		double distance = 0;
		
		int m = input.getRowDimension();
		for (int row = 0; row < m; row++) {
			double[] inputArray = input.getRow(row);
			BumbleMatrix oneInput = factory.createMatrix(new double[][]{inputArray});
			
			//TODO: need to implement for multiple outputs also
			BumbleMatrix answer = net.calculate(oneInput);
			double guess = answer.getEntry(0, 0);
			double correct = output.getEntry(row, 0);
			if (guess >= .5 && correct >= 0.5) {
				truePositives++;
			} else if (guess < .5 && correct < 0.5) {
				trueNegatives++;
			} else if (guess >= .5 && correct <= 0.5) {
				falsePositives++;
			} else if (guess < .5 && correct > 0.5) {
				falseNegatives++;
			}
			
			distance += (guess - correct) * (guess - correct);
			
			aswersAttempted++;
		}
		
		cost = distance / (2 * m);
	}

	/**
	 * Gives a straight percentage of predictions that were correct
	 * @return A straight percentage of predictions that were correct
	 */
	public double getPercentageCorrect() {
		return ((double) (truePositives + trueNegatives) / (double) aswersAttempted) * 100;
	}
	
	/**
	 * Gives the precision of the network.  True Positives / (True Positives + False Positives)
	 * @return The precision of the network
	 */
	public double getPrecision() {
		return truePositives / (truePositives + falsePositives);
	}
	
	/**
	 * Gives the recall of the network.  True Positives / (True Positives + False Negatives)
	 * @return The recall of the network
	 */
	public double getRecall() {
		return truePositives / (truePositives + falseNegatives);
	}
	
	public double getF1() {
		double precision = getPrecision();
		double recall = getRecall();
		return 2 * ((precision * recall) / (precision + recall));
	}

	public double getCost() {
		return cost;
	}

}
