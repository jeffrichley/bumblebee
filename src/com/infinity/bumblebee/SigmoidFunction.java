package com.infinity.bumblebee;

import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class SigmoidFunction implements Function {

	@Override
	public RealMatrix calculate(RealMatrix z) {
		Exp e = new Exp();
		z = z.scalarMultiply(-1);
		
		// TODO: vectorize this
		double[][] data = z.getData();
		for (int i = 0; i < data.length; i++) {
			double[] row = data[i];
			for (int j = 0; j < row.length; j++) {
				double newVal = 1 / (1 + e.value(data[i][j]));
				data[i][j] = newVal;
			}
		}
		
		return MatrixUtils.createRealMatrix(data);
	}

}
