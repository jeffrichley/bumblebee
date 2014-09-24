package com.infinity.bumblebee.data;

/**
 * A <code>BumbleMatrix</code> version of a <code>Tuple</code>
 * @author Jeffrey.Richley
 */
public class MatrixTuple  {

	private final BumbleMatrix one;
	private final BumbleMatrix two;

	public MatrixTuple(BumbleMatrix one, BumbleMatrix two) {
		this.one = one;
		this.two = two;
	}

	public BumbleMatrix getOne() {
		return one;
	}

	public BumbleMatrix getTwo() {
		return two;
	}
	
}
