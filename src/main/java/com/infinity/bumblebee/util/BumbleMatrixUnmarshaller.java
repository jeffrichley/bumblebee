package com.infinity.bumblebee.util;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.exceptions.BumbleException;
import com.infinity.bumblebee.network.NeuralNet;

public class BumbleMatrixUnmarshaller {

	public NeuralNet unmarshal(Reader in) {
		LineNumberReader lnin = new LineNumberReader(in);
		List<BumbleMatrix> thetas = new ArrayList<>();
		
		try {
			String line = lnin.readLine();
			String[] sizes = line.split(" ");
			
			BumbleMatrixFactory factory = new BumbleMatrixFactory();
			
			for (int i = 0; i < sizes.length/2; i++) {
				int rowSize = Integer.parseInt(sizes[i*2]);
				int columnSize = Integer.parseInt(sizes[i*2 + 1]);
				
				BumbleMatrix theta = factory.createMatrix(rowSize, columnSize);
				
				for (int row = 0; row < rowSize; row++) {
					line = lnin.readLine();
					String[] values = line.split(" ");
					for (int column = 0; column < columnSize; column++) {
						double value = Double.parseDouble(values[column]);
						theta.setEntry(row, column, value);
					}
				}
				
				thetas.add(theta);
			}
			
		} catch (IOException e) {
			throw new BumbleException("Unable to read network information from the Reader", e);
		}
		
		return new NeuralNet(thetas);
	}

	public NeuralNet unmarshalMutilFiles(Reader... theta1Reader) {
		return null;
	}

}
