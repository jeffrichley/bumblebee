package com.infinity.bumblebee.util;

import java.io.IOException;
import java.io.Writer;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.exceptions.BumbleException;
import com.infinity.bumblebee.network.NeuralNet;

public class BumbleMatrixMarshaller {

	public void marshal(NeuralNet network, Writer out) {
		try {
			StringBuilder header = new StringBuilder();
			for (BumbleMatrix theta : network.getThetas()) {
				header.append(theta.getRowDimension()).append(" ").append(theta.getColumnDimension()).append(" ");
			}
			header.append("\n");
			out.write(header.toString());
			
			for (BumbleMatrix theta : network.getThetas()) {
				// we need to put this in the loop to help save on memory with big matrices
				StringBuilder buff = new StringBuilder();
				
				for (int row = 0; row < theta.getRowDimension(); row++) {
					double[] rowValues = theta.getRow(row);
					for (double value : rowValues) {
						buff.append(Double.toString(value)).append(" ");
					}
					buff.append("\n");
				}
				out.write(buff.toString());
			}
		} catch (IOException e) {
			throw new BumbleException("Unable to write the network to the Writer", e);
		} finally {
			try {
				// we don't want people playing with the stream so flush and close it
				out.flush();
				out.close();
			} catch (IOException e) {
				throw new BumbleException("Unable to flush and close the Writer", e);
			}
		}
	}

}
