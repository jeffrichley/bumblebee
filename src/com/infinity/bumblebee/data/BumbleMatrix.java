package com.infinity.bumblebee.data;

/**
 * Representation of matrix functionalities used in the library.  This interface was introduced
 * to bridge the usage of a matrix and the underlying implementation.
 * @author jeffreyrichley
 */
public interface BumbleMatrix {

	/**
	 * Get the dimension of the rows of the <code>BumbleMatrix</code>
	 * @return The dimension of the rows
	 */
	int getRowDimension();

	/**
	 * Get the dimension of the columns of the <code>BumbleMatrix</code>
	 * @return The dimension of the columns
	 */
	int getColumnDimension();
	
	/**
	 * Multiplies each entry in the <code>BumbleMatrix</code> by the scalar value
	 * @param scalar The scalar value to multiply each element by
	 * @return A new <code>BumbleMatrix</code> representing the original multiplied by the scalar
	 */
	BumbleMatrix scalarMultiply(double scalar);

	/**
	 * Create a new <code>BumbleMatrix</code> by multiplying the current <code>BumbleMatrix</code> by the supplied <code>BumbleMatrix</code>
	 * @param other The <code>BumbleMatrix</code> to multiply with the current <code>BumbleMatrix</code>
	 * @return A new <code>BumbleMatrix</code> the is the result of multiplying the original and other <code>BumbleMatrix</code>s
	 */
	BumbleMatrix multiply(BumbleMatrix other);

	/**
	 * Create a new <code>BumbleMatrix</code> that is the transpose of the current <code>BumbleMatrix</code>
	 * @return A new <code>BumbleMatrix</code> that is the transpose of the current <code>BumbleMatrix</code>
	 */
	BumbleMatrix transpose();

	/**
	 * Get the double value entry for the given row and column 
	 * @param row The row of the <code>BumbleMatrix</code> to retrieve from
	 * @param column The column of the <code>BumbleMatrix</code> to retrieve from
	 * @return The double value of the entry at the given row and column
	 */
	double getEntry(int row, int column);

	/**
	 * Set the double value entry for the given row and column
	 * @param row The row of the <code>BumbleMatrix</code> to set
	 * @param column The column of the <code>BumbleMatrix</code> to set
	 * @param value The double value to set at the given row and column
	 */
	void setEntry(int row, int column, double value);
	
	/**
	 * Fills the <code>BumbleMatrix</code> with the given value
	 * @param value The value to fill the matrix with
	 */
	void fill(double value);

	/**
	 * Get a column from the <code>BumbleMatrix</code> 
	 * @param columnNumber The number of the column to return
	 * @return The column identified by the columnNumber
	 */
	double[] getColumn(int columnNumber);

	/**
	 * Get a row from the <code>BumbleMatrix</code>
	 * @param rowNumber The number of the row to return
	 * @return The row identified by the rowNumber
	 */
	double[] getRow(int rowNumber);

	/**
	 * Get the entire internal data structure as an array of arrays of doubles
	 * @return The entire internal data structure an array of arrays of doubles
	 */
	double[][] getData();

}
