package com.infinity.bumblebee;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Takes a definition of a Neural Network and trains the Theta values to 
 * properly predict values.  Once the Theta values are trained, it can
 * generate a fully configured Neural Network for use.
 * 
 * @author Jeffrey.Richley
 */
public class NeuralNetTrainer {

	private final List<RealMatrix> thetas;
	private double lambda = 0;
	private final Function function = new SigmoidFunction();
	
	public NeuralNetTrainer(int... layers) {
		this(new ArrayList<RealMatrix>(), layers);
	}
	
	public NeuralNetTrainer(List<RealMatrix> thetas, int... layers) {
		this.thetas = thetas;

		// we need to make a Theta parameter between each layer
		// which comes up to one less that the number of layers
		for (int i = 0; i < layers.length - 1; i++) {
			// make sure that there is one more column that the 
			// specified layer due to the 1's bias
			RealMatrix theta = MatrixUtils.createRealMatrix(layers[i+1], layers[i] + 1);
			thetas.add(theta);
		}
	}

	/**
	 * Get the number of Theta parameters the trainer is working with
	 * @return The number of Theta parameters the trainer is working with
	 */
	public int getNumberOfThetas() {
		return thetas.size();
	}

	/**
	 * Get the size of a given Theta parameter
	 * @param index Index of the Theta to get information about
	 * @return The row and column size of the Theta matrix
	 */
	public IntegerTuple getSizeOfTheta(int index) {
		RealMatrix theta = thetas.get(index);
		IntegerTuple size = new IntegerTuple(theta.getRowDimension(), theta.getColumnDimension());
		return size ;
	}

	/**
	 * Set the lambda property
	 * @param lambda The new value of lambda
	 */
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	
	/**
	 * Get the lambda property value
	 * @return The value of lambda
	 */
	public double getLambda() {
		return lambda;
	}

	public double calculateCost(RealMatrix x, RealMatrix y) {
		return 0;
	}

}
