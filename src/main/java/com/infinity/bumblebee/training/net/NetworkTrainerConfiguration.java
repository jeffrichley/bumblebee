package com.infinity.bumblebee.training.net;

public class NetworkTrainerConfiguration {

	private String testDataFileName;
	private int columnsOfInputs;
	private int columnsOfExpectedOuputs;
	private int[] layers;
	private double lambda;
	private int maxTrainingIterations;
	
	public void setTrainingData(String testDataFileName, int maxTrainingIterations, double lambda, int... layers) {
		this.testDataFileName = testDataFileName;
		this.columnsOfInputs = layers[0];
		this.columnsOfExpectedOuputs = layers[layers.length - 1];
		this.layers = layers;
		this.lambda = lambda;
		this.maxTrainingIterations = maxTrainingIterations;
	}

	public String getTestDataFileName() {
		return testDataFileName;
	}

	public int getColumnsOfInputs() {
		return columnsOfInputs;
	}

	public int getColumnsOfExpectedOuputs() {
		return columnsOfExpectedOuputs;
	}

	public int[] getLayers() {
		return layers;
	}

	public double getLambda() {
		return lambda;
	}

	public int getMaxTrainingIterations() {
		return maxTrainingIterations;
	}

}
