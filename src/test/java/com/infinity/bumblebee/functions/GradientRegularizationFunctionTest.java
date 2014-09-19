package com.infinity.bumblebee.functions;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;

public class GradientRegularizationFunctionTest {

	private Object cut;

	@Before
	public void setUp() throws Exception {
		cut = new GradientRegularizationFunction();
	}

	@Test
	public void testCalculate() {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(new double[][]{{1, 2, 3}, {1, 5, 6}});
	}

}
