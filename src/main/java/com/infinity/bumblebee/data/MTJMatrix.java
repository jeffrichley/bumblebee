package com.infinity.bumblebee.data;

import java.util.Random;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.MatrixEntry;

public class MTJMatrix implements BumbleMatrix {

	private final DenseMatrix matrix;
	
	public MTJMatrix(double[][] ds) {
		matrix = new DenseMatrix(ds);
	}

	public MTJMatrix(int rowDimension, int columnDimension) {
		matrix = new DenseMatrix(rowDimension, columnDimension);
	}

	public MTJMatrix(DenseMatrix matrix) {
		this.matrix = matrix;
	}

	@Override
	public int getRowDimension() {
		return matrix.numRows();
	}

	@Override
	public int getColumnDimension() {
		return matrix.numColumns();
	}

	@Override
	public BumbleMatrix scalarMultiply(double scalar) {
		DenseMatrix scale = (DenseMatrix) new DenseMatrix(matrix).scale(scalar);
		return new MTJMatrix(scale);
	}

	@Override
	public BumbleMatrix multiply(BumbleMatrix other) {
		DenseMatrix otherMatrix = ((MTJMatrix) other).matrix;
		DenseMatrix answer = new DenseMatrix(matrix.numRows(), otherMatrix.numColumns());
		matrix.mult(otherMatrix, answer);
		return new MTJMatrix(answer);
	}

	@Override
	public BumbleMatrix transpose() {
		DenseMatrix t = new DenseMatrix(matrix.numColumns(), matrix.numRows());
		for (MatrixEntry e : matrix) {
            t.set(e.column(), e.row(), e.get());
		}
		return new MTJMatrix(t);
	}

	@Override
	public double getEntry(int row, int column) {
		return matrix.get(row, column);
	}

	@Override
	public void setEntry(int row, int column, double value) {
		matrix.set(row, column, value);
	}

	@Override
	public double[] getColumn(int columnNumber) {
		double[] column = new double[matrix.numRows()];
		for (int i = 0; i < matrix.numRows(); i++) {
			column[i] = matrix.get(i, columnNumber);
		}
		return column;
	}

	@Override
	public double[] getRow(int rowNumber) {
		double[] row = new double[matrix.numColumns()];
		for (int i = 0; i < matrix.numColumns(); i++) {
			row[i] = matrix.get(rowNumber, i);
		}
		return row ;
	}

	@Override
	public double[][] getData() {
		double[][] data = new double[matrix.numRows()][matrix.numColumns()];
		for (int row = 0; row < matrix.numRows(); row++) {
			for (int column = 0; column < matrix.numColumns(); column++) {
				data[row][column] = matrix.get(row, column);
			}
		}
		return data;
	}

	@Override
	public void fill(double value) {
		for (int row = 0; row < matrix.numRows(); row++) {
			for (int column = 0; column < matrix.numColumns(); column++) {
				matrix.set(row, column, value);
			}
		}
	}

	@Override
	public void randomizeWithEpsilon() {
		double epsilon = Math.sqrt(6) / Math.sqrt(matrix.numRows() + matrix.numColumns());
		if (epsilon > 0.12) {
			epsilon = 0.12;
		}
		Random r = new Random(System.currentTimeMillis());
		for (int row = 0; row < matrix.numRows(); row++) {
			for (int column = 0; column < matrix.numColumns(); column++) {
				double value = r.nextDouble() * epsilon;
				if (r.nextDouble() < 0.5) {
					value = value * -1;
				}
				matrix.set(row, column, value);
			}
		}
	}

}
