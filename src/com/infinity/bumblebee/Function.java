package com.infinity.bumblebee;

import com.infinity.bumblebee.data.BumbleMatrix;

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
	public BumbleMatrix calculate(BumbleMatrix z);
	
}
