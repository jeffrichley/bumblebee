package com.infinity.bumblebee.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;

/**
 * Reads a csv file into a matrix
 * @author Jeffrey.Richley
 */
public class DataReader {
	
	/**
	 * The <code>BumbleMatrixFactory</code> for creating a <code>BumbleMatrix</code>
	 */
	private BumbleMatrixFactory factory = new BumbleMatrixFactory();

	/**
	 * Read in the given filename and convert the columns and rows into a matrix
	 * @param fileName The name of the file to read data from
	 * @return A <code>RealMatrix</code> created from the file contents
	 */
	public BumbleMatrix getMatrixFromFile(String fileName) {
		List<double[]> rows = new ArrayList<double[]>();
		
		LineNumberReader in = null;
		try {
			in = new LineNumberReader(new FileReader(fileName));
			
			String line = in.readLine();
			while (line != null) {
				String[] vals = line.split(",");
				double[] row = new double[vals.length];
				for (int i = 0; i < vals.length; i++) {
					String val = vals[i];
					double d = Double.parseDouble(val);
					row[i] = d;
				}
				rows.add(row);
				line = in.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to read " + fileName, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new RuntimeException("Unable to close " + fileName, e);
				}
			}
		}
		
		return factory.createMatrix(rows.toArray(new double[rows.size()][]));
	}
	
}
