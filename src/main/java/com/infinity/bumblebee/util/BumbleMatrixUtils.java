package com.infinity.bumblebee.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;

public class BumbleMatrixUtils {

	public BumbleMatrix onesColumnAdded(BumbleMatrix matrix) {
		double[][] data = new double[matrix.getRowDimension()][matrix.getColumnDimension()+1];
		
		for (int i = 0; i < data.length; i++) {
			double[] oldRow = matrix.getRow(i);
			double[] newRow = data[i];
			newRow[0] = 1;
			for (int j = 1; j < data[i].length; j++) {
				newRow[j] =  oldRow[j-1];
			}
		}
		
		return new BumbleMatrixFactory().createMatrix(data);
	}

	public void printMatrixDetails(String name, BumbleMatrix matrix) {
		System.out.println("------------------------");
		System.out.println(name + ": " + matrix.getRowDimension() + "x" + matrix.getColumnDimension());
	}
	
	public void printMatrixValues(String name, BumbleMatrix matrix) {
		System.out.println("------------------------");
		System.out.println(name);
		for (int row = 0; row < matrix.getRowDimension(); row++) {
			for (int column = 0; column < matrix.getColumnDimension(); column++) {
				System.out.print(matrix.getEntry(row, column) + " ");
			}
			System.out.println();
		}
	}

	public BumbleMatrix log(BumbleMatrix original) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(original.getRowDimension(), original.getColumnDimension());
		
		for (int i = 0; i < original.getRowDimension(); i++) {
			for (int j = 0; j < original.getColumnDimension(); j++) {
				matrix.setEntry(i, j, Math.log(original.getEntry(i, j)));
			}
		}
		
		return matrix;
	}
	
	public BumbleMatrix elementWiseMutilply(BumbleMatrix one, BumbleMatrix two) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(one.getRowDimension(), one.getColumnDimension());
		
		for (int i = 0; i < one.getRowDimension(); i++) {
			for (int j = 0; j < one.getColumnDimension(); j++) {
				double oneVal = one.getEntry(i, j);
				double twoVal = two.getEntry(i, j);
				matrix.setEntry(i, j, oneVal * twoVal);
			}
		}
		
		return matrix;
	}
	
	public BumbleMatrix elementWiseSubtract(BumbleMatrix one, BumbleMatrix two) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(one.getRowDimension(), one.getColumnDimension());
		
		for (int i = 0; i < one.getRowDimension(); i++) {
			for (int j = 0; j < one.getColumnDimension(); j++) {
				double oneVal = one.getEntry(i, j);
				double twoVal = two.getEntry(i, j);
				matrix.setEntry(i, j, oneVal - twoVal);
			}
		}
		
		return matrix;
	}
	
	public BumbleMatrix elementWiseAddition(BumbleMatrix one, BumbleMatrix two) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(one.getRowDimension(), one.getColumnDimension());
		
		for (int i = 0; i < one.getRowDimension(); i++) {
			for (int j = 0; j < one.getColumnDimension(); j++) {
				double oneVal = one.getEntry(i, j);
				double twoVal = two.getEntry(i, j);
				matrix.setEntry(i, j, oneVal + twoVal);
			}
		}
		
		return matrix;
	}

	public double sumAll(BumbleMatrix matrix) {
		double answer = 0;
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getColumnDimension(); j++) {
				double oneVal = matrix.getEntry(i, j);
				answer += oneVal;
			}
		}
		return answer;
	}

	public BumbleMatrix elementWiseSquare(BumbleMatrix theta) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(theta.getRowDimension(), theta.getColumnDimension());
		for (int row = 0; row < theta.getRowDimension(); row++) {
			for (int column = 0; column < theta.getColumnDimension(); column++) {
				double val = theta.getEntry(row, column);
				matrix.setEntry(row, column, val * val);
			}
		}
		return matrix;
	}

	public BumbleMatrix elementWiseSubtract(double[] one, double[] two) {
		double[][] answer = new double[1][one.length];
		
		for (int column = 0; column < one.length; column++) {
			double value = one[column] - two[column];
			answer[0][column] = value;
		}
		
		return new BumbleMatrixFactory().createMatrix(answer);
	}

	public BumbleMatrix removeFirstColumn(BumbleMatrix original) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(original.getRowDimension(), original.getColumnDimension() - 1);
		for (int row = 0; row < original.getRowDimension(); row++) {
			for (int column = 1; column < original.getColumnDimension(); column++) {
				matrix.setEntry(row, column - 1, original.getEntry(row, column));
			}
		}
		return matrix;
	}

	public BumbleMatrix sum(BumbleMatrix original) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(1, original.getColumnDimension());
		for (int column = 0; column < original.getColumnDimension(); column++) {
			double value = 0;
			for (int row = 0; row < original.getRowDimension(); row++) {
				value += original.getEntry(row, column);
			}
			matrix.setEntry(0, column, value);
		}
		return matrix;
	}

	public BumbleMatrix scalarDivide(BumbleMatrix original, double scalar) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(original.getRowDimension(), original.getColumnDimension());
		for (int row = 0; row < original.getRowDimension(); row++) {
			for (int column = 0; column < original.getColumnDimension(); column++) {
				double val = original.getEntry(row, column) / scalar;
				matrix.setEntry(row, column, val);
			}
		}
		return matrix;
	}

	public BumbleMatrix scalarMultiply(BumbleMatrix original, double scalar) {
		BumbleMatrix matrix = new BumbleMatrixFactory().createMatrix(original.getRowDimension(), original.getColumnDimension());
		for (int row = 0; row < original.getRowDimension(); row++) {
			for (int column = 0; column < original.getColumnDimension(); column++) {
				double val = original.getEntry(row, column) * scalar;
				matrix.setEntry(row, column, val);
			}
		}
		return matrix;
	}

	public BumbleMatrix unroll(BumbleMatrix original) {
		BumbleMatrix unrolled = new BumbleMatrixFactory().createMatrix(original.getRowDimension()*original.getColumnDimension(), 1);
		
		int count = 0;
		for (int column = 0; column < original.getColumnDimension(); column++) {
			for (int row = 0; row < original.getRowDimension(); row++) {
				unrolled.setEntry(count++, 0, original.getEntry(row, column));
			}
		}
		
		return unrolled;
	}

	public BumbleMatrix reshape(BumbleMatrix original, int rows, int columns) {
		BumbleMatrix reshaped = new BumbleMatrixFactory().createMatrix(rows, columns);
		
		int count = 0;
		for (int column = 0; column < reshaped.getColumnDimension(); column++) {
			for (int row = 0; row < reshaped.getRowDimension(); row++) {
				double value = original.getEntry(count++, 0);
				reshaped.setEntry(row, column, value);
			}
		}
		
		return reshaped;
	}

	public BumbleMatrix unroll(BumbleMatrix... matrices) {
		int length = 0;
		for (BumbleMatrix m : matrices) {
			length += m.getColumnDimension() * m.getRowDimension();
		}

		int count = 0;
		BumbleMatrix unrolled = new BumbleMatrixFactory().createMatrix(length, 1);
		
		for (BumbleMatrix m : matrices) {
			BumbleMatrix tmp = unroll(m);
			for (int i = 0; i < tmp.getRowDimension(); i++) {
				unrolled.setEntry(count++, 0, tmp.getEntry(i, 0));
			}
		}
		
		return unrolled;
	}
	
	public BumbleMatrix unroll(List<BumbleMatrix> list) {
		BumbleMatrix[] array = new BumbleMatrix[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return unroll(array);
	}

	public List<BumbleMatrix> reshape(BumbleMatrix original, int[] is) {
		List<BumbleMatrix> ms = new ArrayList<>();
		BumbleMatrixFactory factory = new BumbleMatrixFactory();
		
		int previousEnd = 0;
		
		for (int i = 0; i < is.length / 2; i++) {
			int rowSize = is[i * 2];
			int columnSize = is[i * 2 + 1];
			int length = rowSize * columnSize;
			
			double[] data = Arrays.copyOfRange(original.getColumn(0), previousEnd, previousEnd + length);
			BumbleMatrix sub = factory.createMatrix(length, 1);
			
			for (int j = 0; j < length; j++) {
				sub.setEntry(j, 0, data[j]);
			}
			
			BumbleMatrix m = reshape(sub, rowSize, columnSize);
			ms.add(m);
			
			previousEnd = previousEnd + length;
		}
		
		return ms;
	}

}
