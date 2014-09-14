package com.infinity.bumblebee.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MTJMatrixTest {

	private MTJMatrix cut;

	@Before
	public void setUp() throws Exception {
		cut = new MTJMatrix(new double[][]{{1,2,3},{4,5,6}});
	}


	@Test
	public void ensureGetRowDimension() {
		int rows = cut.getRowDimension();
		assertThat(2, is(equalTo(rows)));
	}

	@Test
	public void ensureGetColumnDimension() {
		int columns = cut.getColumnDimension();
		assertThat(3, is(equalTo(columns)));
	}

	@Test
	public void ensureScalarMultiply() {
		MTJMatrix m = (MTJMatrix) cut.scalarMultiply(2);
		assertThat(m.getEntry(0, 0), is(equalTo(2d)));
		assertThat(m.getEntry(1, 2), is(equalTo(12d)));
	}

	@Test
	public void ensureMultiply() {
		MTJMatrix m = (MTJMatrix) cut.multiply(cut.transpose());
		
		assertThat(m.getColumnDimension(), is(equalTo(2)));
		assertThat(m.getRowDimension(), is(equalTo(2)));
		
		assertThat(m.getEntry(0, 0), is(equalTo(14d)));
		assertThat(m.getEntry(0, 1), is(equalTo(32d)));
		assertThat(m.getEntry(1, 0), is(equalTo(32d)));
		assertThat(m.getEntry(1, 1), is(equalTo(77d)));
	}

	@Test
	public void ensureTranspose() {
		MTJMatrix m = (MTJMatrix) cut.transpose();
		
		assertThat(m.getColumnDimension(), is(equalTo(2)));
		assertThat(m.getRowDimension(), is(equalTo(3)));
		
		assertThat(m.getEntry(0, 0), is(equalTo(1d)));
		assertThat(m.getEntry(0, 1), is(equalTo(4d)));
		assertThat(m.getEntry(2, 0), is(equalTo(3d)));
		assertThat(m.getEntry(2, 1), is(equalTo(6d)));
	}

	@Test
	public void ensureGetEntry() {
		assertThat(cut.getEntry(0,0), is(equalTo(1d)));
		assertThat(cut.getEntry(1,2), is(equalTo(6d)));
	}

	@Test
	public void ensureSetEntry() {
		assertThat(cut.getEntry(0, 0), is(equalTo(1d)));
		cut.setEntry(0, 0, 20);
		assertThat(cut.getEntry(0, 0), is(equalTo(20d)));
	}

	@Test
	public void ensureGetColumn() {
		double[] columnOne = cut.getColumn(0);
		assertThat(columnOne.length, is(equalTo(2)));
		assertThat(columnOne[0], is(equalTo(1d)));
		assertThat(columnOne[1], is(equalTo(4d)));
		
		double[] columnTwo = cut.getColumn(1);
		assertThat(columnTwo.length, is(equalTo(2)));
		assertThat(columnTwo[0], is(equalTo(2d)));
		assertThat(columnTwo[1], is(equalTo(5d)));
	}

	@Test
	public void ensureGetRow() {
		double[] rowOne = cut.getRow(0);
		assertThat(rowOne.length, is(equalTo(3)));
		assertThat(rowOne[0], is(equalTo(1d)));
		assertThat(rowOne[1], is(equalTo(2d)));
		assertThat(rowOne[2], is(equalTo(3d)));
		
		double[] rowTwo = cut.getRow(1);
		assertThat(rowTwo.length, is(equalTo(3)));
		assertThat(rowTwo[0], is(equalTo(4d)));
		assertThat(rowTwo[1], is(equalTo(5d)));
		assertThat(rowTwo[2], is(equalTo(6d)));
	}

	@Test
	public void ensureGetData() {
		double[][] data = cut.getData();
		assertThat(data[0][0], is(equalTo(1d)));
		assertThat(data[0][1], is(equalTo(2d)));
		assertThat(data[0][2], is(equalTo(3d)));
		assertThat(data[1][0], is(equalTo(4d)));
		assertThat(data[1][1], is(equalTo(5d)));
		assertThat(data[1][2], is(equalTo(6d)));
	}
	
	@Test
	public void ensureFill() {
		cut.fill(9);
		
		for (int row = 0; row < cut.getRowDimension(); row++) {
			for (int column = 0; column < cut.getColumnDimension(); column++) {
				assertThat(cut.getEntry(row, column), is(equalTo(9d)));
			}
		}
	}

}
