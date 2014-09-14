package com.infinity.bumblebee.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;

public class DataReaderTest {

	private DataReader cut;

	@Before
	public void setup() {
		cut = new DataReader();
	}
	
	@Test
	public void ensureCanReadCsv() {
		BumbleMatrix matrix = cut.getMatrixFromFile("./test-data/X.csv");
		
		assertThat(matrix, is(not(nullValue())));
		assertThat(matrix.getRowDimension(), is(equalTo(5000)));
		assertThat(matrix.getColumnDimension(), is(equalTo(400)));
	}
	
	@Test(expected=RuntimeException.class)
	public void ensureIOExceptionNotFoundFile(){
		cut.getMatrixFromFile("some-non-existent-file.csv");
	}
}
