package com.infinity.bumblebee.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BumbleMatrixFactoryTest {

	private BumbleMatrixFactory cut;

	@Before
	public void setUp() throws Exception {
		cut = new BumbleMatrixFactory();
	}

	@Test
	public void testCreateMatrixDoubleArrayArray() {
		BumbleMatrix matrix = cut.createMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
		
		assertThat(2, is(equalTo(matrix.getRowDimension())));
		assertThat(3, is(equalTo(matrix.getColumnDimension())));
	}

	@Test
	public void testCreateMatrixIntInt() {
		BumbleMatrix matrix = cut.createMatrix(2, 3);
		
		assertThat(2, is(equalTo(matrix.getRowDimension())));
		assertThat(3, is(equalTo(matrix.getColumnDimension())));
	}
	
	@Test
	public void testCreateOnesMatrix() {
		BumbleMatrix ones = cut.createOnes(2, 3);
		
		assertThat(ones.getEntry(0, 0), is(equalTo(1d)));
		assertThat(ones.getEntry(0, 1), is(equalTo(1d)));
		assertThat(ones.getEntry(0, 2), is(equalTo(1d)));
		assertThat(ones.getEntry(1, 0), is(equalTo(1d)));
		assertThat(ones.getEntry(1, 1), is(equalTo(1d)));
		assertThat(ones.getEntry(1, 2), is(equalTo(1d)));
	}

}
