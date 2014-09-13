package com.infinity.bumblebee.data;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class CommonsDenseMatrix implements BumbleMatrix {
	
	private final RealMatrix matrix;

	public CommonsDenseMatrix(double[][] ds) {
		matrix = MatrixUtils.createRealMatrix(ds);
	}

	public CommonsDenseMatrix(int rowDimension, int columnDimension) {
		matrix = MatrixUtils.createRealMatrix(rowDimension, columnDimension);
	}
	
	public CommonsDenseMatrix(RealMatrix matrix) {
		this.matrix = matrix;
	}

	@Override
	public int getRowDimension() {
		return matrix.getRowDimension();
	}

	@Override
	public int getColumnDimension() {
		return matrix.getColumnDimension();
	}

	@Override
	public BumbleMatrix scalarMultiply(double scalar) {
		return new CommonsDenseMatrix(matrix.scalarMultiply(scalar));
	}

	@Override
	public BumbleMatrix multiply(BumbleMatrix other) {
		return new CommonsDenseMatrix(matrix.multiply(((CommonsDenseMatrix)other).matrix));
	}

	@Override
	public BumbleMatrix transpose() {
		return new CommonsDenseMatrix(matrix.transpose());
	}

	@Override
	public double getEntry(int row, int column) {
		return matrix.getEntry(row, column);
	}

	@Override
	public void setEntry(int row, int column, double value) {
		matrix.setEntry(row, column, value);
	}

	@Override
	public double[] getColumn(int columnNumber) {
		return matrix.getColumn(columnNumber);
	}

	@Override
	public double[] getRow(int rowNumber) {
		return matrix.getRow(rowNumber);
	}

	@Override
	public double[][] getData() {
		return matrix.getData();
	}

}
