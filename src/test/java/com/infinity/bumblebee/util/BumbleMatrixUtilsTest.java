package com.infinity.bumblebee.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;

public class BumbleMatrixUtilsTest {
	
	private BumbleMatrixUtils cut;

	@Before
	public void setup() {
		this.cut = new BumbleMatrixUtils();
	}

	@Test
	public void ensureOnesColumnAdded() {
		// original is a 2x3
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(new double[][]{{2,2,2}, {3,3,3}});
		BumbleMatrix withOnes = cut.onesColumnAdded(matrix);
		
		// resulting matrix should now be 2x4 with ones in the first column
		assertThat(withOnes.getRowDimension(), is(equalTo(2)));
		assertThat(withOnes.getColumnDimension(), is(equalTo(4)));
		
		assertThat(withOnes.getColumn(0)[0], is(equalTo(1d)));
		assertThat(withOnes.getColumn(0)[1], is(equalTo(1d)));
		
		assertThat(withOnes.getColumn(1)[0], is(equalTo(2d)));
		assertThat(withOnes.getColumn(1)[1], is(equalTo(3d)));
	}
	
	@Test
	public void ensureLog() {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(new double[][]{{.1, .5, .9}});
		BumbleMatrix log = cut.log(matrix);
		
		assertEquals(log.getEntry(0, 0), -2.3025850929940455, 0.0000001);
		assertEquals(log.getEntry(0, 1), -0.6931471805599453, 0.0000001);
		assertEquals(log.getEntry(0, 2), -0.10536051565782628, 0.0000001);
	}

	@Test
	public void ensureElementwiseSubtract() {
		BumbleMatrix one = new BumbleMatrixFactory().createMatrix(new double[][]{{2, 4}});
		BumbleMatrix two = new BumbleMatrixFactory().createMatrix(new double[][]{{1, 2}});
		BumbleMatrix subtract = cut.elementWiseSubtract(one, two);
		
		assertThat(subtract.getEntry(0, 0), is(equalTo(1d)));
		assertThat(subtract.getEntry(0, 1), is(equalTo(2d)));
	}
	
	@Test
	public void ensureElementwiseAddition() {
		BumbleMatrix one = new BumbleMatrixFactory().createMatrix(new double[][]{{2, 4}});
		BumbleMatrix two = new BumbleMatrixFactory().createMatrix(new double[][]{{1, 2}});
		BumbleMatrix addition = cut.elementWiseAddition(one, two);
		
		assertThat(addition.getEntry(0, 0), is(equalTo(3d)));
		assertThat(addition.getEntry(0, 1), is(equalTo(6d)));
	}

	@Test
	public void ensureScalarDivide() {
		BumbleMatrix one = new BumbleMatrixFactory().createMatrix(new double[][]{{2, 4}});
		BumbleMatrix divide = cut.scalarDivide(one, 2);
		
		assertThat(divide.getEntry(0, 0), is(equalTo(1d)));
		assertThat(divide.getEntry(0, 1), is(equalTo(2d)));
	}
	
	@Test
	public void ensureScalarMultiply() {
		BumbleMatrix one = new BumbleMatrixFactory().createMatrix(new double[][]{{2, 4}, {1, .5}});
		BumbleMatrix divide = cut.scalarMultiply(one, 2);
		
		assertThat(divide.getEntry(0, 0), is(equalTo(4d)));
		assertThat(divide.getEntry(0, 1), is(equalTo(8d)));
	}

	@Test
	public void ensureElementwiseSquare() {
		BumbleMatrix one = new BumbleMatrixFactory().createMatrix(new double[][]{{1,2}, {3,4}});
		BumbleMatrix square = cut.elementWiseSquare(one);
		
		assertThat(square.getEntry(0, 0), is(equalTo(1d)));
		assertThat(square.getEntry(0, 1), is(equalTo(4d)));
		assertThat(square.getEntry(1, 0), is(equalTo(9d)));
		assertThat(square.getEntry(1, 1), is(equalTo(16d)));
	}
	
	@Test
	public void ensureElementWiseSubtractArrays() {
		double[] one = new double[]{1,2};
		double[] two = new double[]{1,1};
		
		BumbleMatrix answer = cut.elementWiseSubtract(one, two);
		
		assertThat(answer.getRowDimension(), is(equalTo(1)));
		assertThat(answer.getColumnDimension(), is(equalTo(2)));
		
		assertThat(answer.getEntry(0, 0), is(equalTo(0d)));
		assertThat(answer.getEntry(0, 1), is(equalTo(1d)));
	}
	
	@Test
	public void ensureRemovingFirstColumn() {
		BumbleMatrix twoColumns = new BumbleMatrixFactory().createMatrix(new double[][]{{1,2}, {3,4}});
		BumbleMatrix oneColumn = cut.removeFirstColumn(twoColumns);
		
		assertThat(oneColumn.getColumnDimension(), is(equalTo(1)));
		assertThat(oneColumn.getRowDimension(), is(equalTo(2)));
		
		assertThat(oneColumn.getEntry(0, 0), is(equalTo(2d)));
		assertThat(oneColumn.getEntry(1, 0), is(equalTo(4d)));
	}
	
	@Test
	public void ensureSum() {
		BumbleMatrix original = new BumbleMatrixFactory().createMatrix(new double[][]{{1,2}, {3,4}});
		BumbleMatrix sum = cut.sum(original);
		
		assertThat(sum.getRowDimension(), is(equalTo(1)));
		assertThat(sum.getColumnDimension(), is(equalTo(2)));
		
		assertThat(sum.getEntry(0, 0), is(equalTo(4d)));
		assertThat(sum.getEntry(0, 1), is(equalTo(6d)));
	}
	
	@Test
	public void ensureUnrolling() {
		BumbleMatrix original = new BumbleMatrixFactory().createMatrix(new double[][]{{1,2}, {3,4}});
		BumbleMatrix unrolled = cut.unroll(original);
		
		assertThat(unrolled.getRowDimension(), is(equalTo(4)));
		assertThat(unrolled.getColumnDimension(), is(equalTo(1)));
		
		assertThat(unrolled.getEntry(0, 0), is(equalTo(1d)));
		assertThat(unrolled.getEntry(1, 0), is(equalTo(3d)));
		assertThat(unrolled.getEntry(2, 0), is(equalTo(2d)));
		assertThat(unrolled.getEntry(3, 0), is(equalTo(4d)));
	}
	
	@Test
	public void ensureUnrollingMultipleMatrices() {
		BumbleMatrix one = new BumbleMatrixFactory().createMatrix(new double[][]{{1,2}, {3,4}});
		BumbleMatrix two = new BumbleMatrixFactory().createMatrix(new double[][]{{5,6}, {7,8}});
		BumbleMatrix unrolled = cut.unroll(one, two);
		
		assertThat(unrolled.getRowDimension(), is(equalTo(8)));
		assertThat(unrolled.getColumnDimension(), is(equalTo(1)));
		
		assertThat(unrolled.getEntry(0, 0), is(equalTo(1d)));
		assertThat(unrolled.getEntry(1, 0), is(equalTo(3d)));
		assertThat(unrolled.getEntry(2, 0), is(equalTo(2d)));
		assertThat(unrolled.getEntry(3, 0), is(equalTo(4d)));
		
		assertThat(unrolled.getEntry(4, 0), is(equalTo(5d)));
		assertThat(unrolled.getEntry(5, 0), is(equalTo(7d)));
		assertThat(unrolled.getEntry(6, 0), is(equalTo(6d)));
		assertThat(unrolled.getEntry(7, 0), is(equalTo(8d)));
	}
	
	@Test
	public void ensureReshaping() {
		BumbleMatrix original = new BumbleMatrixFactory().createMatrix(new double[][]{{1},{2},{3},{4},{5},{6}});
		BumbleMatrix reshape = cut.reshape(original, 2, 3);
		
		assertThat(reshape.getRowDimension(), is(equalTo(2)));
		assertThat(reshape.getColumnDimension(), is(equalTo(3)));
		
		assertThat(reshape.getEntry(0, 0), is(equalTo(1d)));
		assertThat(reshape.getEntry(0, 1), is(equalTo(3d)));
		assertThat(reshape.getEntry(0, 2), is(equalTo(5d)));
		assertThat(reshape.getEntry(1, 0), is(equalTo(2d)));
		assertThat(reshape.getEntry(1, 1), is(equalTo(4d)));
		assertThat(reshape.getEntry(1, 2), is(equalTo(6d)));
	}
	
	@Test
	public void ensureReshapingMultipleMatrices() {
		BumbleMatrix original = new BumbleMatrixFactory().createMatrix(new double[][]{{1},{3},{2},{4},{5},{7},{6},{8}});
		List<BumbleMatrix> reshapes = cut.reshape(original, new int[]{2, 2, 2, 2});
		
		assertThat(reshapes.size(), is(equalTo(2)));
		
		assertThat(reshapes.get(0).getRowDimension(), is(equalTo(2)));
		assertThat(reshapes.get(0).getColumnDimension(), is(equalTo(2)));
		assertThat(reshapes.get(1).getRowDimension(), is(equalTo(2)));
		assertThat(reshapes.get(1).getColumnDimension(), is(equalTo(2)));

		assertThat(reshapes.get(0).getEntry(0, 0), is(equalTo(1d)));
		assertThat(reshapes.get(0).getEntry(0, 1), is(equalTo(2d)));
		assertThat(reshapes.get(0).getEntry(1, 0), is(equalTo(3d)));
		assertThat(reshapes.get(0).getEntry(1, 1), is(equalTo(4d)));
		
		assertThat(reshapes.get(1).getEntry(0, 0), is(equalTo(5d)));
		assertThat(reshapes.get(1).getEntry(0, 1), is(equalTo(6d)));
		assertThat(reshapes.get(1).getEntry(1, 0), is(equalTo(7d)));
		assertThat(reshapes.get(1).getEntry(1, 1), is(equalTo(8d)));

	}
}
