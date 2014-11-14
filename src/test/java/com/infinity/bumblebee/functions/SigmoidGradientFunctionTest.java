package com.infinity.bumblebee.functions;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;

public class SigmoidGradientFunctionTest {

	private SigmoidGradientFunction cut;

	@Before
	public void setUp() throws Exception {
		cut = new SigmoidGradientFunction();
	}

	@Test
	public void ensureCalculate() {
		BumbleMatrix m = new BumbleMatrixFactory().createMatrix(new double[][]{{1, -0.5, 0, 0.5, 1}});
		BumbleMatrix sigGrad = cut.calculate(m);
		
		// 1, -0.5, 0, 0.5, 1
		// turns into
		// 0.19661   0.23500   0.25000   0.23500   0.19661
		assertEquals(0.19661, sigGrad.getEntry(0, 0), 0.0001);
		assertEquals(0.23500, sigGrad.getEntry(0, 1), 0.0001);
		assertEquals(0.25000, sigGrad.getEntry(0, 2), 0.0001);
		assertEquals(0.23500, sigGrad.getEntry(0, 3), 0.0001);
		assertEquals(0.19661, sigGrad.getEntry(0, 4), 0.0001);
	}

}
