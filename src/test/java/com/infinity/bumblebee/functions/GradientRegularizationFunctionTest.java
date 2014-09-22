package com.infinity.bumblebee.functions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;

public class GradientRegularizationFunctionTest {

	@Test
	public void testCalculate() {
		BumbleMatrix theta = new BumbleMatrixFactory().createMatrix(new double[][]{{.5, 1, 2}, {.5, 3, 4}});
		BumbleMatrix gradient = new BumbleMatrixFactory().createMatrix(new double[][]{{1, 2, 4}, {1, 6, 8}});
		
		GradientRegularizationFunction cut = new GradientRegularizationFunction(2, 4);
		BumbleMatrix reg = cut.calculate(theta, gradient);
		
		assertThat(reg.getEntry(0, 0), is(equalTo(0.5)));
		assertThat(reg.getEntry(0, 1), is(equalTo(3d)));
		assertThat(reg.getEntry(0, 2), is(equalTo(6d)));
		
		assertThat(reg.getEntry(1, 0), is(equalTo(0.5)));
		assertThat(reg.getEntry(1, 1), is(equalTo(9d)));
		assertThat(reg.getEntry(1, 2), is(equalTo(12d)));
	}

}
