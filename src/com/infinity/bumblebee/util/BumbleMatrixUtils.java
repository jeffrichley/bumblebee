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
	
	public BumbleMatrix elementWiseSubstract(BumbleMatrix one, BumbleMatrix two) {
//		printMatrixDetails("one", one);
//		printMatrixDetails("two", two);
		
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

}
