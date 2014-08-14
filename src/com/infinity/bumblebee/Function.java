package com.infinity.bumblebee;

import org.apache.commons.math3.linear.RealMatrix;

/**
 * Performs calculations during training and predictions
 * @author Jeffrey.Richley
 */
public interface Function {

	/**
	 * Calculate the information with the given value
	 * @param z The z value of the function
	 * @return The overall value
	 */
	public RealMatrix calculate(RealMatrix z);
	
}
