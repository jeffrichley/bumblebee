package com.infinity.bumblebee.util;

import org.apache.commons.math3.analysis.function.Log;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class BumbleMatrixUtils {

	public RealMatrix onesColumnAdded(RealMatrix matrix) {
		double[][] data = new double[matrix.getRowDimension()][matrix.getColumnDimension()+1];
		
		for (int i = 0; i < data.length; i++) {
			double[] oldRow = matrix.getRow(i);
			double[] newRow = data[i];
			newRow[0] = 1;
			for (int j = 1; j < data[i].length; j++) {
				newRow[j] =  oldRow[j-1];
			}
		}
		
		return MatrixUtils.createRealMatrix(data);
	}

	public void printMatrixDetails(String name, RealMatrix matrix) {
		System.out.println("------------------------");
		System.out.println(name + ": " + matrix.getRowDimension() + "x" + matrix.getColumnDimension());
	}

	public RealMatrix log(RealMatrix original) {
		Log log = new Log();
		RealMatrix matrix = MatrixUtils.createRealMatrix(original.getRowDimension(), original.getColumnDimension());
		
		for (int i = 0; i < original.getRowDimension(); i++) {
			for (int j = 0; j < original.getColumnDimension(); j++) {
				matrix.setEntry(i, j, log.value(original.getEntry(i, j)));
			}
		}
		
		return matrix;
	}
	
	public RealMatrix elementWiseMutilply(RealMatrix one, RealMatrix two) {
		RealMatrix matrix = MatrixUtils.createRealMatrix(one.getRowDimension(), one.getColumnDimension());
		
		for (int i = 0; i < one.getRowDimension(); i++) {
			for (int j = 0; j < one.getColumnDimension(); j++) {
				double oneVal = one.getEntry(i, j);
				double twoVal = two.getEntry(i, j);
				matrix.setEntry(i, j, oneVal * twoVal);
			}
		}
		
		return matrix;
	}
	
	public RealMatrix elementWiseSubstract(RealMatrix one, RealMatrix two) {
		printMatrixDetails("one", one);
		printMatrixDetails("two", two);
		
		RealMatrix matrix = MatrixUtils.createRealMatrix(one.getRowDimension(), one.getColumnDimension());
		
		for (int i = 0; i < one.getRowDimension(); i++) {
			for (int j = 0; j < one.getColumnDimension(); j++) {
				double oneVal = one.getEntry(i, j);
				double twoVal = two.getEntry(i, j);
				matrix.setEntry(i, j, oneVal - twoVal);
			}
		}
		
		return matrix;
	}
	
	public RealMatrix elementWiseAddition(RealMatrix one, RealMatrix two) {
		RealMatrix matrix = MatrixUtils.createRealMatrix(one.getRowDimension(), one.getColumnDimension());
		
		for (int i = 0; i < one.getRowDimension(); i++) {
			for (int j = 0; j < one.getColumnDimension(); j++) {
				double oneVal = one.getEntry(i, j);
				double twoVal = two.getEntry(i, j);
				matrix.setEntry(i, j, oneVal + twoVal);
			}
		}
		
		return matrix;
	}

	public double sum(RealMatrix matrix) {
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
