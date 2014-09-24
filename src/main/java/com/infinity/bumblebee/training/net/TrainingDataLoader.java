package com.infinity.bumblebee.training.net;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.data.MatrixTuple;
import com.infinity.bumblebee.util.DataReader;

public class TrainingDataLoader {
	
	public MatrixTuple loadData(NetworkTrainerConfiguration config) {
		DataReader reader = new DataReader();
		BumbleMatrix matrixFromFile = reader.getMatrixFromFile(config.getTestDataFileName());
		
		BumbleMatrixFactory factory = new BumbleMatrixFactory();
		BumbleMatrix inputs = factory.createMatrix(matrixFromFile.getRowDimension(), config.getColumnsOfInputs());
		BumbleMatrix outputs = factory.createMatrix(matrixFromFile.getRowDimension(), config.getColumnsOfExpectedOuputs());
		
		for (int row = 0; row < matrixFromFile.getRowDimension(); row++) {
			int count = 0;
			for (int i = 0; i < config.getColumnsOfInputs(); i++) {
				double value = matrixFromFile.getEntry(row, count++);
				inputs.setEntry(row, i, value);
			}
			
			double expected = matrixFromFile.getEntry(row, count);
			outputs.setEntry(row, 0, expected);
		}
		
		MatrixTuple tuple = new MatrixTuple(inputs, outputs);
		
		return tuple;
	}
	
}
