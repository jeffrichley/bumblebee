package com.infinity.bumblebee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.data.IntegerTuple;
import com.infinity.bumblebee.functions.Function;
import com.infinity.bumblebee.functions.SigmoidFunction;
import com.infinity.bumblebee.util.BumbleMatrixUtils;

/**
 * Takes a definition of a Neural Network and trains the Theta values to 
 * properly predict values.  Once the Theta values are trained, it can
 * generate a fully configured Neural Network for use.
 * 
 * @author Jeffrey.Richley
 */
public class NeuralNetTrainer {

	private final List<BumbleMatrix> thetas;
	private double lambda = 0;
	private final Function function = new SigmoidFunction();
	private final BumbleMatrixFactory factory = new BumbleMatrixFactory();
	
	public NeuralNetTrainer(int... layers) {
		this.thetas = new ArrayList<BumbleMatrix>();
		
		// we need to make a Theta parameter between each layer
		// which comes up to one less that the number of layers
		for (int i = 0; i < layers.length - 1; i++) {
			// make sure that there is one more column that the 
			// specified layer due to the 1's bias
			BumbleMatrix theta = factory.createMatrix(layers[i+1], layers[i] + 1);
			thetas.add(theta);
		}
	}
	
	public NeuralNetTrainer(List<BumbleMatrix> thetas) {
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
		BumbleMatrix theta = thetas.get(index);
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
	 * @param lambda The regularization parameter.  A reasponable default would be 0.
	 * @return The cost of the network for the given training and label data
	 */
	protected double calculateCost(BumbleMatrix X, BumbleMatrix y, int numLabels, double lambda) {
		// nnCostFunction.m from mlclass-ex4-005
		
		// set up helpful variables
		// m = size(X, 1);
		int m = X.getRowDimension();
		
		BumbleMatrixUtils bmu = new BumbleMatrixUtils();
		
		BumbleMatrix a = bmu.onesColumnAdded(X);
//		bmu.printMatrixDetails("a", a);
		
		Iterator<BumbleMatrix> iter = thetas.iterator();
		while (iter.hasNext()) {
			BumbleMatrix theta = iter.next();
			
			a = function.calculate(theta.multiply(a.transpose()));
			
			if (iter.hasNext()) {
				a = bmu.onesColumnAdded(a.transpose());
			}
		}
		
		a = a.transpose();
		
//		bmu.printMatrixDetails("a", a);
		
		// Code Marker #1
		
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
		BumbleMatrix yMatrix = factory.createMatrix(yVecData);
		
//		bmu.printMatrixDetails("yMatrix", yMatrix);
		
		BumbleMatrix myOnes = factory.createMatrix(a.getRowDimension(), a.getColumnDimension());
//		bmu.printMatrixDetails("myOnes", myOnes);
//		for (int i = 0; i < myOnes.getRowDimension(); i++) {
//			double[] row = myOnes.getRow(i);
//			Arrays.fill(row, 1);
//		}
		myOnes.fill(1d);
		
		double sumForM = 0;
		for (int i = 0; i < m; i++) {
			double[] yRowValue = yMatrix.getRow(i);
			// check: yVec is good
			BumbleMatrix yVec = factory.createMatrix(new double[][]{yRowValue});
//			bmu.printMatrixDetails("yVec", yVec);
			
			double[] a3RowValue = a.getRow(i);
			// check: a3 is good
			BumbleMatrix a3 = factory.createMatrix(new double[][]{a3RowValue});
			
			double[] onesValue = myOnes.getRow(i);
			BumbleMatrix ones = factory.createMatrix(new double[][]{onesValue});
			
//			myone = (-yVec(i,:) .* log(A3(i,:)));   	% spot on values
			
			// a3, log(a3), -yVec is fine
			// myOne looks good - i hope
			BumbleMatrix myOne = bmu.elementWiseMutilply(yVec.scalarMultiply(-1), bmu.log(a3));
			
//			mytwo = myOnes(i,:) .- yVec(i,:);       	% spot on values
			// myTwo looks good
			BumbleMatrix myTwo = bmu.elementWiseSubstract(ones, yVec);
			
//			mythree = log(myOnes(i,:) .- A3(i,:));    % spot on values
			
			BumbleMatrix myThree = bmu.log(bmu.elementWiseSubstract(ones, a3));

//			sumForM = sumForM + sum(myone - mytwo .* mythree);
			sumForM += bmu.sum(bmu.elementWiseSubstract(myOne, bmu.elementWiseMutilply(myTwo, myThree)));
		}
		
		double cost = sumForM / m;
		double regularization = 0;
		double regularizatonSummations = 0;
		if (lambda != 0) {
			for (BumbleMatrix theta : thetas) {
				BumbleMatrix squared = bmu.elementWiseSquare(theta);
				regularizatonSummations += bmu.sum(squared);
			}
			
			regularization = (lambda / (2 * m)) * regularizatonSummations;
		}
		
		return cost + regularization;
	}

}
