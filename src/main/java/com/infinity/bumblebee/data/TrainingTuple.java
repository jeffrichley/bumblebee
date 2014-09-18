package com.infinity.bumblebee.data;

import java.util.List;

/**
 * Results from one computation of training
 * @author jeffreyrichley
 */
public class TrainingTuple {

	private final double cost;
	private final List<BumbleMatrix> gradients;

	/**
	 * Constructor giving the cost and gradients for back propagation
	 * @param cost The cost of the training iteration
	 * @param gradients The calculated gradients of the training iteration
	 */
	public TrainingTuple (double cost, List<BumbleMatrix> gradients) {
		this.cost = cost;
		this.gradients = gradients;
	}

	/**
	 * Get the cost of the training iteration
	 * @return The cost of the training iteration
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Get the gradients of the training iteration
	 * @return The gradients of the training iteration
	 */
	public List<BumbleMatrix> getGradients() {
		return gradients;
	}
	
}
