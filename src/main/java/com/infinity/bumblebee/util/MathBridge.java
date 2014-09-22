package com.infinity.bumblebee.util;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.math.DenseDoubleVector;
import com.infinity.bumblebee.math.DoubleVector;

public class MathBridge {

	private final BumbleMatrixFactory factory = new BumbleMatrixFactory();
	
	public BumbleMatrix convert(DoubleVector v) {
		BumbleMatrix m = factory.createMatrix(v.getLength(), 1);
		
		for (int i = 0; i < v.getLength(); i++) {
			m.setEntry(i, 0, v.get(i));
		}
		
		return m;
	}

	public DoubleVector convert(BumbleMatrix m) {
		DenseDoubleVector v = new DenseDoubleVector(m.getRowDimension());
		
		for (int i = 0; i < m.getRowDimension(); i++) {
			v.set(i, m.getEntry(i, 0));
		}
		
		return v;
	}

}
