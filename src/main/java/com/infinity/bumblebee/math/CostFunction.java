package com.infinity.bumblebee.math;

import com.infinity.bumblebee.network.NeuralNet;

/**
 * Cost function interface to be implemented when using with a optimizer like
 * conjugate gradient for example.
 * 
 * @author thomas.jungblut
 * 
 */
public interface CostFunction {

	/**
	 * Evaluation for the cost function to retrieve cost and gradient.
	 * 
	 * @param input
	 *            a given input vector
	 * @return a tuple consists of J (cost) and a vector X which is the gradient
	 *         of the input.
	 */
	CostGradientTuple evaluateCost(DoubleVector input);

	/**
	 * Get the current network as the training as set it
	 * @return The current network as the training as set it
	 */
	NeuralNet getCurrentNetwork();

}