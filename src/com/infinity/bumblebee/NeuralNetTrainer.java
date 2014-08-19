package com.infinity.bumblebee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.analysis.function.Log;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.infinity.bumblebee.util.BumbleMatrixUtils;

/**
 * Takes a definition of a Neural Network and trains the Theta values to 
 * properly predict values.  Once the Theta values are trained, it can
 * generate a fully configured Neural Network for use.
 * 
 * @author Jeffrey.Richley
 */
public class NeuralNetTrainer {

	private final List<RealMatrix> thetas;
	private double lambda = 0;
	private final Function function = new SigmoidFunction();
	
	public NeuralNetTrainer(int... layers) {
		this.thetas = new ArrayList<RealMatrix>();
		
		// we need to make a Theta parameter between each layer
		// which comes up to one less that the number of layers
		for (int i = 0; i < layers.length - 1; i++) {
			// make sure that there is one more column that the 
			// specified layer due to the 1's bias
			RealMatrix theta = MatrixUtils.createRealMatrix(layers[i+1], layers[i] + 1);
			thetas.add(theta);
		}
	}
	
	public NeuralNetTrainer(List<RealMatrix> thetas) {
		this.thetas = thetas;

	}

	/**
	 * Get the number of Theta parameters the trainer is working with
	 * @return The number of Theta parameters the trainer is working with
	 */
	public int getNumberOfThetas() {
		return thetas.size();
	}

	/**
	 * Get the size of a given Theta parameter
	 * @param index Index of the Theta to get information about
	 * @return The row and column size of the Theta matrix
	 */
	public IntegerTuple getSizeOfTheta(int index) {
		RealMatrix theta = thetas.get(index);
		IntegerTuple size = new IntegerTuple(theta.getRowDimension(), theta.getColumnDimension());
		return size ;
	}

	/**
	 * Set the lambda property
	 * @param lambda The new value of lambda
	 */
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	
	/**
	 * Get the lambda property value
	 * @return The value of lambda
	 */
	public double getLambda() {
		return lambda;
	}

	/**
	 * Computes the cost of the network
	 * @param X The training data
	 * @param y The desired labels for the training data
	 * @param numLabels The number of distinct labels for the training data
	 * @return The cost of the network for the given training and label data
	 */
	protected double calculateCost(RealMatrix X, RealMatrix y, int numLabels) {
		// nnCostFunction.m from mlclass-ex4-005
		
		int m = X.getRowDimension();
		
		BumbleMatrixUtils bmu = new BumbleMatrixUtils();
		
		RealMatrix a = bmu.onesColumnAdded(X);
		
		Iterator<RealMatrix> iter = thetas.iterator();
		while (iter.hasNext()) {
			RealMatrix theta = iter.next();
			
			a = function.calculate(theta.multiply(a.transpose()));
			
			if (iter.hasNext()) {
				a = bmu.onesColumnAdded(a.transpose());
			}
		}
		
		a = a.transpose();
		
		bmu.printMatrixDetails("a", a);
		
		double[][] yVecData = new double[m][numLabels];
		// fill it with 0's
		for (double[] ds : yVecData) {
			Arrays.fill(ds, 0);
		}
		// set the value from the original y into the yk's indexed element
		for (int i = 0; i < y.getRowDimension(); i++) {
			int val = (int) y.getRow(i)[0];
			yVecData[i][val] = 1.0;
		}
		RealMatrix yMatrix = MatrixUtils.createRealMatrix(yVecData);
		bmu.printMatrixDetails("yMatrix", yMatrix);
		
		RealMatrix myOnes = MatrixUtils.createRealMatrix(a.getRowDimension(), a.getColumnDimension());
		bmu.printMatrixDetails("myOnes", myOnes);
		for (int i = 0; i < myOnes.getRowDimension(); i++) {
			double[] row = myOnes.getRow(i);
			Arrays.fill(row, 1);
		}
		
		double sumForM = 0;
		for (int i = 0; i < m; i++) {
			double[] yRowValue = yMatrix.getRow(i);
			RealMatrix yVec = MatrixUtils.createRealMatrix(new double[][]{yRowValue});
			bmu.printMatrixDetails("yVec", yVec);
			
			double[] a3RowValue = a.getRow(i);
			RealMatrix a3 = MatrixUtils.createRealMatrix(new double[][]{a3RowValue});
			
			double[] onesValue = myOnes.getRow(i);
			RealMatrix ones = MatrixUtils.createRealMatrix(new double[][]{onesValue});
			
//			myone = (-yVec(i,:) .* log(A3(i,:)));   	% spot on values
			RealMatrix myOne = bmu.elementWiseMutilply(yVec.scalarMultiply(-1), bmu.log(a3));
			
//			mytwo = myOnes(i,:) .- yVec(i,:);       	% spot on values
			RealMatrix myTwo = bmu.elementWiseSubstract(ones, yVec);
			
//			mythree = log(myOnes(i,:) .- A3(i,:));    % spot on values
			RealMatrix myThree = bmu.log(bmu.elementWiseSubstract(myOne, a3));

//			sumForM = sumForM + sum(myone - mytwo .* mythree);
			sumForM += bmu.sum(bmu.elementWiseSubstract(myOne, bmu.elementWiseMutilply(myTwo, myThree)));
		}
		
		return sumForM;
	}

}
