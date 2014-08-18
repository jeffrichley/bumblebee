package com.infinity.bumblebee.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class BumbleMatrixUtilsTest {
	
	private BumbleMatrixUtils cut;

	@Before
	public void setup() {
		this.cut = new BumbleMatrixUtils();
	}

	@Test
	public void ensureOnesColumnAdded() {
		// original is a 2x3
		RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2,2,2}, {3,3,3}});
		RealMatrix withOnes = cut.onesColumnAdded(matrix);
		
		// resulting matrix should now be 2x4 with ones in the first column
		assertThat(withOnes.getRowDimension(), is(equalTo(2)));
		assertThat(withOnes.getColumnDimension(), is(equalTo(4)));
		
		assertThat(withOnes.getColumn(0)[0], is(equalTo(1d)));
		assertThat(withOnes.getColumn(0)[1], is(equalTo(1d)));
		
		assertThat(withOnes.getColumn(1)[0], is(equalTo(2d)));
		assertThat(withOnes.getColumn(1)[1], is(equalTo(3d)));
	}

}
