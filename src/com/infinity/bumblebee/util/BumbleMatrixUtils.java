package com.infinity.bumblebee.util;

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

}
