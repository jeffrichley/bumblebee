package com.infinity.bumblebee.functions;

import org.apache.commons.math3.analysis.function.Exp;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;

public class SigmoidFunction implements MatrixFunction {
	
	private final BumbleMatrixFactory factory = new BumbleMatrixFactory();

	@Override
	public BumbleMatrix calculate(BumbleMatrix z) {
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
		
		return factory.createMatrix(data);
	}

}
