package com.infinity.bumblebee.util;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;

public class BumbleMatrixUtils {

	public BumbleMatrix onesColumnAdded(BumbleMatrix matrix) {
		double[][] data = new double[matrix.getRowDimension()][matrix.getColumnDimension()+1];
		
		for (int i = 0; i < data.length; i++) {
			double[] oldRow = matrix.getRow(i);
			double[] newRow = data[i];
			newRow[0] = 1;
			for (int j = 1; j < data[i].length; j++) {
				newRow[j] =  oldRow[j-1];
			}
		}
		
		return new BumbleMatrixFactory().createMatrix(data);
	}

	public void printMatrixDetails(String name, BumbleMatrix matrix) {
		System.out.println("------------------------");
		System.out.println(name + ": " + matrix.getRowDimension() + "x" + matrix.getColumnDimension());
	}

	public BumbleMatrix log(BumbleMatrix original) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(original.getRowDimension(), original.getColumnDimension());
		
		for (int i = 0; i < original.getRowDimension(); i++) {
			for (int j = 0; j < original.getColumnDimension(); j++) {
				matrix.setEntry(i, j, Math.log(original.getEntry(i, j)));
			}
		}
		
		return matrix;
	}
	
	public BumbleMatrix elementWiseMutilply(BumbleMatrix one, BumbleMatrix two) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(one.getRowDimension(), one.getColumnDimension());
		
		for (int i = 0; i < one.getRowDimension(); i++) {
			for (int j = 0; j < one.getColumnDimension(); j++) {
				double oneVal = one.getEntry(i, j);
				double twoVal = two.getEntry(i, j);
				matrix.setEntry(i, j, oneVal * twoVal);
			}
		}
		
		return matrix;
	}
	
	public BumbleMatrix elementWiseSubtract(BumbleMatrix one, BumbleMatrix two) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(one.getRowDimension(), one.getColumnDimension());
		
		for (int i = 0; i < one.getRowDimension(); i++) {
			for (int j = 0; j < one.getColumnDimension(); j++) {
				double oneVal = one.getEntry(i, j);
				double twoVal = two.getEntry(i, j);
				matrix.setEntry(i, j, oneVal - twoVal);
			}
		}
		
		return matrix;
	}
	
	public BumbleMatrix elementWiseAddition(BumbleMatrix one, BumbleMatrix two) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(one.getRowDimension(), one.getColumnDimension());
		
		for (int i = 0; i < one.getRowDimension(); i++) {
			for (int j = 0; j < one.getColumnDimension(); j++) {
				double oneVal = one.getEntry(i, j);
				double twoVal = two.getEntry(i, j);
				matrix.setEntry(i, j, oneVal + twoVal);
			}
		}
		
		return matrix;
	}

	public double sum(BumbleMatrix matrix) {
		double answer = 0;
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getColumnDimension(); j++) {
				double oneVal = matrix.getEntry(i, j);
				answer += oneVal;
			}
		}
		return answer;
	}

	public BumbleMatrix elementWiseSquare(BumbleMatrix theta) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(theta.getRowDimension(), theta.getColumnDimension());
		for (int row = 0; row < theta.getRowDimension(); row++) {
			for (int column = 0; column < theta.getColumnDimension(); column++) {
				double val = theta.getEntry(row, column);
				matrix.setEntry(row, column, val * val);
			}
		}
		return matrix;
	}

	public BumbleMatrix elementWiseSubtract(double[] one, double[] two) {
		double[][] answer = new double[1][one.length];
		
		for (int column = 0; column < one.length; column++) {
			double value = one[column] - two[column];
			answer[0][column] = value;
		}
		
		return new BumbleMatrixFactory().createMatrix(answer);
	}
}
