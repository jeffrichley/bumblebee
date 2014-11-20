package com.infinity.bumblebee.training;

/**
 * Responsible for being able to calculate the cost during training cycles
 * @author jeffreyrichley
 */
public interface CostCalculation {

	/**
	 * Get the cost of the given cycle of training
	 * @return The cost of the given cycle of training
	 */
	double getCost();
	
	/**
	 * Get the regularization value of the given cycle of training.  This is not guaranteed to be implemented.
	 * @return The regularization value of the given cycle of training.
	 */
	double getRegularization();
	
}
