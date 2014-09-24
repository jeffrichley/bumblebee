package com.infinity.bumblebee.training.net;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.MatrixTuple;

public class TrainingDataLoaderTest {

	@Test
	public void ensureLoadsData() {
		NetworkTrainerConfiguration config = new NetworkTrainerConfiguration();
		config.setTrainingData("./test-data/iris.csv", 100, 0.3, 4, 4, 3);
		
		TrainingDataLoader loader = new TrainingDataLoader();
		MatrixTuple tuple = loader.loadData(config);
		BumbleMatrix input = tuple.getOne();
		BumbleMatrix output = tuple.getTwo();
		
		assertThat(tuple, is(notNullValue()));
		assertThat(input, is(notNullValue()));
		assertThat(output, is(notNullValue()));
		
		assertThat(input.getRowDimension(), is(equalTo(150)));
		assertThat(input.getColumnDimension(), is(equalTo(4)));
		assertThat(output.getRowDimension(), is(equalTo(150)));
		assertThat(output.getColumnDimension(), is(equalTo(3)));
		
		assertThat(input.getEntry(0, 0), is(equalTo(5.1)));
		assertThat(input.getEntry(0, 1), is(equalTo(3.5)));
		assertThat(input.getEntry(0, 2), is(equalTo(1.4)));
		assertThat(input.getEntry(0, 3), is(equalTo(0.2)));
		
		assertThat(output.getEntry(0, 0), is(equalTo(0d)));
		assertThat(output.getEntry(51, 0), is(equalTo(1d)));
		assertThat(output.getEntry(149, 0), is(equalTo(2d)));
	}
	
}
