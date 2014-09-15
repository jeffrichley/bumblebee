package com.infinity.bumblebee.functions;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.util.BumbleMatrixUtils;

public class SigmoidGradientFunction implements MatrixFunction {

	private BumbleMatrixFactory factory = new BumbleMatrixFactory();
	private BumbleMatrixUtils utils = new BumbleMatrixUtils();
	private SigmoidFunction sigFunction = new SigmoidFunction();
	
	@Override
	public BumbleMatrix calculate(BumbleMatrix original) {
//		g = sigmoid(z);
//		g2 = 1 .- g;
//		g = g .* g2;
		
		BumbleMatrix g = sigFunction.calculate(original);
		BumbleMatrix ones = factory.createOnes(g.getRowDimension(), g.getColumnDimension());
		BumbleMatrix g2 = utils.elementWiseSubstract(ones, g);
		BumbleMatrix sigGrad = utils.elementWiseMutilply(g, g2); 
		
		return sigGrad;
	}

}
