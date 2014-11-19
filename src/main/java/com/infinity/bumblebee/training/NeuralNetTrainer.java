package com.infinity.bumblebee.training;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.data.IntegerTuple;
import com.infinity.bumblebee.data.TrainingTuple;
import com.infinity.bumblebee.functions.GradientRegularizationFunction;
import com.infinity.bumblebee.functions.MatrixFunction;
import com.infinity.bumblebee.functions.SigmoidFunction;
import com.infinity.bumblebee.functions.SigmoidGradientFunction;
import com.infinity.bumblebee.util.BumbleMatrixUtils;
import com.infinity.bumblebee.util.TimerUtil;

/**
 * Takes a definition of a Neural Network and trains the Theta values to
 * properly predict values. Once the Theta values are trained, it can generate a
 * fully configured Neural Network for use.
 * 
 * @author Jeffrey.Richley
 */
public class NeuralNetTrainer {
	
	private final static Logger LOGGER = Logger.getLogger(NeuralNetTrainer.class.getName()); 

	private List<BumbleMatrix> thetas;
	private double lambda = 0;
	private final MatrixFunction function = new SigmoidFunction();
	private final MatrixFunction sigmoidGradientFunction = new SigmoidGradientFunction();
	private final BumbleMatrixFactory factory = new BumbleMatrixFactory();

	public NeuralNetTrainer(int... layers) {
		this.thetas = new ArrayList<BumbleMatrix>();

		// we need to make a Theta parameter between each layer
		// which comes up to one less that the number of layers
		for (int i = 0; i < layers.length - 1; i++) {
			// make sure that there is one more column that the
			// specified layer due to the 1's bias
			BumbleMatrix theta = factory.createMatrix(layers[i + 1], layers[i] + 1);
			theta.randomizeWithEpsilon();
			thetas.add(theta);
		}
	}

	public NeuralNetTrainer(List<BumbleMatrix> thetas) {
		this.thetas = thetas;
	}

	/**
	 * Get the number of Theta parameters the trainer is working with
	 * 
	 * @return The number of Theta parameters the trainer is working with
	 */
	public int getNumberOfThetas() {
		return thetas.size();
	}

	/**
	 * Get the size of a given Theta parameter
	 * 
	 * @param index
	 *            Index of the Theta to get information about
	 * @return The row and column size of the Theta matrix
	 */
	public IntegerTuple getSizeOfTheta(int index) {
		BumbleMatrix theta = thetas.get(index);
		IntegerTuple size = new IntegerTuple(theta.getRowDimension(),
				theta.getColumnDimension());
		return size;
	}

	/**
	 * Set the lambda property
	 * 
	 * @param lambda
	 *            The new value of lambda
	 */
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	/**
	 * Get the lambda property value
	 * 
	 * @return The value of lambda
	 */
	public double getLambda() {
		return lambda;
	}

	/**
	 * Computes the cost of the network
	 * 
	 * @param X
	 *            The training data
	 * @param y
	 *            The desired labels for the training data
	 * @param numLabels
	 *            The number of distinct labels for the training data
	 * @param lambda
	 *            The regularization parameter. A reasponable default would be
	 *            0.
	 * @return The cost of the network for the given training and label data
	 */
	protected TrainingTuple calculateCost(BumbleMatrix X, BumbleMatrix y, int numLabels, double lambda) {
		LOGGER.fine("Starting cost calculation");
		TimerUtil timer = new TimerUtil();
		// nnCostFunction.m from mlclass-ex4-005

		// set up helpful variables
		// m = size(X, 1);
		int m = X.getRowDimension();

		BumbleMatrixUtils bmu = new BumbleMatrixUtils();

		BumbleMatrix a = bmu.onesColumnAdded(X);
		// bmu.printMatrixDetails("a", a);

		List<BumbleMatrix> as = new ArrayList<>();
		as.add(bmu.onesColumnAdded(X));
		List<BumbleMatrix> zs = new ArrayList<>();
		Iterator<BumbleMatrix> iter = thetas.iterator();
		while (iter.hasNext()) {
			BumbleMatrix theta = iter.next();

			BumbleMatrix z = theta.multiply(a.transpose());
			a = function.calculate(z);

			if (iter.hasNext()) {
				as.add(bmu.onesColumnAdded(a.transpose()));
				zs.add(bmu.onesColumnAdded(z.transpose()));
				a = bmu.onesColumnAdded(a.transpose());
			} else {
				as.add(a.transpose());
				zs.add(z.transpose());
			}
		}

		a = a.transpose();
		
		// Code Marker #1

		double[][] yVecData = new double[m][numLabels];
		// fill it with 0's
		for (double[] ds : yVecData) {
			Arrays.fill(ds, 0);
		}
		
		// We have different behaviors if there is one output vs many outputs.
		// If there are many outputs, we need to put a one in the y vector
		// at the index of the value given, but if there is only one output,
		// the one value needs to be set to the value sent into this function
		// in the y matrix.
		
		if (numLabels > 1) {
			// set the value from the original y into the yk's indexed element
			for (int i = 0; i < y.getRowDimension(); i++) {
				int val = (int) y.getRow(i)[0];
				yVecData[i][val] = 1.0;
			}
		} else {
			// there is only one output, so we put the y value into the first element
			for (int i = 0; i < y.getRowDimension(); i++) {
				int val = (int) y.getRow(i)[0];
				yVecData[i][0] = val;
			}
		}
		BumbleMatrix yMatrix = factory.createMatrix(yVecData);
		
		BumbleMatrix myOnes = factory.createMatrix(a.getRowDimension(), a.getColumnDimension());
		myOnes.fill(1d);
		
		LOGGER.fine("Starting forward propagation");
		
		double sumForM = 0;
		for (int i = 0; i < m; i++) {
			double[] yRowValue = yMatrix.getRow(i);
			// check: yVec is good
			BumbleMatrix yVec = factory.createMatrix(new double[][] { yRowValue });
			// bmu.printMatrixDetails("yVec", yVec);

			double[] a3RowValue = a.getRow(i);
			// check: a3 is good
			BumbleMatrix a3 = factory.createMatrix(new double[][] { a3RowValue });

			double[] onesValue = myOnes.getRow(i);
			BumbleMatrix ones = factory.createMatrix(new double[][] { onesValue });

			// myone = (-yVec(i,:) .* log(A3(i,:))); % spot on values

			// a3, log(a3), -yVec is fine
			// myOne looks good - i hope
			BumbleMatrix myOne = bmu.elementWiseMutilply(yVec.scalarMultiply(-1), bmu.log(a3));

			// mytwo = myOnes(i,:) .- yVec(i,:); % spot on values
			// myTwo looks good
			BumbleMatrix myTwo = bmu.elementWiseSubtract(ones, yVec);

			// mythree = log(myOnes(i,:) .- A3(i,:)); % spot on values

			BumbleMatrix myThree = bmu.log(bmu.elementWiseSubtract(ones, a3));

			// sumForM = sumForM + sum(myone - mytwo .* mythree);
			sumForM += bmu.sumAll(bmu.elementWiseSubtract(myOne, bmu.elementWiseMutilply(myTwo, myThree)));
		}
		
		LOGGER.fine("Finished forward propagation " + timer.markSeconds());
		LOGGER.fine("Starting regularization");

		double cost = sumForM / m;
		double regularization = 0;
		double regularizatonSummations = 0;
		if (lambda != 0) {
			for (BumbleMatrix theta : thetas) {
				BumbleMatrix squared = bmu.elementWiseSquare(theta);
				regularizatonSummations += bmu.sumAll(squared);
			}

			regularization = (lambda / (2 * m)) * regularizatonSummations;
		}

		LOGGER.fine("Finished regularization " + timer.markSeconds());
		LOGGER.fine("Starting back propagation");
		
		// back propagation here
		List<BumbleMatrix> deltas = new ArrayList<>();
		
		int zIndex = zs.size() - 2;
		int thetaIndex = thetas.size() - 1;
		int alphaIndex = as.size() - 1;
		
		// calculate the deltas
		LOGGER.fine("Starting delta calculation");
		BumbleMatrix previousDelta = bmu.elementWiseSubtract(as.get(alphaIndex), yMatrix);
		deltas.add(previousDelta);
		while (thetaIndex > 0) {
			BumbleMatrix theta = thetas.get(thetaIndex--);
			BumbleMatrix sigGrad = sigmoidGradientFunction.calculate(zs.get(zIndex--));
			previousDelta = bmu.elementWiseMutilply(theta.transpose().multiply(previousDelta.transpose()).transpose(), sigGrad);
			deltas.add(previousDelta);
		}
		LOGGER.fine("Finished delta calculation " + timer.markSeconds());
		Collections.reverse(deltas);
		
		// calculate gradients
		LOGGER.fine("Starting gradient calculation");
		List<BumbleMatrix> gradients = new ArrayList<>();
		for (int index = as.size() - 2; index >= 0; index--) {
			BumbleMatrix alpha = as.get(index);
			BumbleMatrix delta = deltas.get(index);
			
			// if this is the first, we need to strip off the first
			// column of ones to fit the dimensions of the input
			if (index == 0) {
				delta = bmu.removeFirstColumn(delta);
			}
			
			BumbleMatrix grad = null;
			for (int i = 0; i < m; i++) {
				BumbleMatrix tmpd = factory.createMatrix(new double[][] {delta.getRow(i)});
				BumbleMatrix tmpa = factory.createMatrix(new double[][] {alpha.getRow(i)});
				
				if (grad == null) {
					grad = tmpd.transpose().multiply(tmpa);
				} else {
					grad = bmu.elementWiseAddition(grad, tmpd.transpose().multiply(tmpa));
				}
			}
			
			gradients.add(grad);
		}
		LOGGER.fine("Finished gradient calculation " + timer.markSeconds());

		Collections.reverse(gradients);
		GradientRegularizationFunction gradReg = new GradientRegularizationFunction(m, lambda);

		LOGGER.fine("Starting to applying gradients");
		List<BumbleMatrix> finalGradients = new ArrayList<>();
		for (int i = 0; i < thetas.size(); i++) {
			BumbleMatrix theta = thetas.get(i);
			BumbleMatrix gradient = gradients.get(i);
			
			BumbleMatrix finalGradient = gradReg.calculate(theta, gradient);
			finalGradients.add(finalGradient);
		}
		LOGGER.fine("Completed applying gradients " + timer.markSeconds());
		LOGGER.fine("Finished back propagation");
		
		return new TrainingTuple(cost + regularization, finalGradients);
	}

	public List<BumbleMatrix> getThetas() {
		return thetas;
	}

	public void setThetas(List<BumbleMatrix> thetas) {
		this.thetas = thetas;
	}
}
