package com.infinity.bumblebee.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.network.NeuralNet;

public class PersistenceTest {

	private BumbleMatrixMarshaller marshaller;
	private BumbleMatrixUnmarshaller unmarshaller;
	private NeuralNet network;

	@Before
	public void setup() {
		marshaller = new BumbleMatrixMarshaller();
		unmarshaller = new BumbleMatrixUnmarshaller();

		BumbleMatrixFactory factory = new BumbleMatrixFactory();
		
		BumbleMatrix theta1 = factory.createMatrix(new double[][]{{1,2}, {3,4}});
		BumbleMatrix theta2 = factory.createMatrix(new double[][]{{5,6,7}, {8,9,10}, {11,12,13}});
		
		List<BumbleMatrix> thetas = new ArrayList<>();
		thetas.add(theta1);
		thetas.add(theta2);
		
		network = new NeuralNet(thetas);
	}
	
	@Test
	public void ensureSavesAndLoads() {
		StringWriter saved = new StringWriter();
		marshaller.marshal(network, saved);
		System.out.println(saved.toString());

		NeuralNet readNetwork = unmarshaller.unmarshal(new StringReader(saved.toString()));
		
		BumbleMatrix theta1 = readNetwork.getThetas().get(0);
		BumbleMatrix theta2 = readNetwork.getThetas().get(1);
		
		// make sure they are the correct size
		assertThat(theta1.getRowDimension(), is(equalTo(2)));
		assertThat(theta1.getColumnDimension(), is(equalTo(2)));
		assertThat(theta2.getRowDimension(), is(equalTo(3)));
		assertThat(theta2.getColumnDimension(), is(equalTo(3)));
		
		// make sure that theta1 has the correct values
		assertThat(theta1.getEntry(0, 0), is(closeTo(1, 0)));
		assertThat(theta1.getEntry(0, 1), is(closeTo(2, 0)));
		assertThat(theta1.getEntry(1, 0), is(closeTo(3, 0)));
		assertThat(theta1.getEntry(1, 1), is(closeTo(4, 0)));
		
		// make sure that theta2 has the correct values
		assertThat(theta2.getEntry(0, 0), is(closeTo(5, 0)));
		assertThat(theta2.getEntry(0, 1), is(closeTo(6, 0)));
		assertThat(theta2.getEntry(0, 2), is(closeTo(7, 0)));
		assertThat(theta2.getEntry(1, 0), is(closeTo(8, 0)));
		assertThat(theta2.getEntry(1, 1), is(closeTo(9, 0)));
		assertThat(theta2.getEntry(1, 2), is(closeTo(10, 0)));
		assertThat(theta2.getEntry(2, 0), is(closeTo(11, 0)));
		assertThat(theta2.getEntry(2, 1), is(closeTo(12, 0)));
		assertThat(theta2.getEntry(2, 2), is(closeTo(13, 0)));
	}

}
