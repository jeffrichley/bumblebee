package com.infinity.bumblebee.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.math.DenseDoubleVector;
import com.infinity.bumblebee.math.DoubleVector;

public class MathBridgeTest {

	private MathBridge cut;

	@Before
	public void setUp() throws Exception {
		cut = new MathBridge();
	}

	@Test
	public void ensureConvertingFromBumbleMatrixToDoubleVector() {
		BumbleMatrix m = new BumbleMatrixFactory().createMatrix(new double[][]{{1d}, {2d}, {3d}});
		DoubleVector v = cut.convert(m);
		
		assertThat(v.get(0), is(equalTo(1d)));
		assertThat(v.get(1), is(equalTo(2d)));
		assertThat(v.get(2), is(equalTo(3d)));
	}

	@Test
	public void ensureConvertingFromDoubleVectorToBumbleMatrix() {
		DoubleVector v = new DenseDoubleVector(new double[]{1, 2, 3});
		BumbleMatrix m = cut.convert(v);
		
		assertThat(m.getEntry(0, 0), is(equalTo(1d)));
		assertThat(m.getEntry(1, 0), is(equalTo(2d)));
		assertThat(m.getEntry(2, 0), is(equalTo(3d)));
	}
}
