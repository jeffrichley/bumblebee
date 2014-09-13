package com.infinity.bumblebee.data;

/**
 * All creation of matrices should be done using this factory.  This ensures
 * that there is a bridge between the underlying matrix implementation and the
 * rest of the system.
 * @author jeffreyrichley
 */
public class BumbleMatrixFactory {

	/**
	 * Create a new <code>BumbleMatrix</code>
	 * @param ds The data source of the matrix
	 * @return A new <code>BumbleMatrix</code> using the given data as the initial values
	 */
	public BumbleMatrix createMatrix(double[][] ds) {
//		return new CommonsDenseMatrix(ds);
		return new MTJMatrix(ds);
	}

	/**
	 * Create a new <code>BumbleMatrix</code>
	 * @param rowDimension The dimension of the rows
	 * @param columnDimension The dimension of the columns
	 * @return A new <code>BumbleMatrix</code> with the given dimensions
	 */
	public BumbleMatrix createMatrix(int rowDimension, int columnDimension) {
//		return new CommonsDenseMatrix(rowDimension, columnDimension);
		return new MTJMatrix(rowDimension, columnDimension);
	}

}
