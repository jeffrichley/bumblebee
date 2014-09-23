package com.infinity.bumblebee.training.net;

public class NetworkTrainerConfiguration {

	private String testDataFileName;
	private int columnsOfInputs;
	private int columnsOfExpectedOuputs;
	
	public void setTrainingData(String testDataFileName, int columnsOfInputs, int columnsOfExpectedOuputs) {
		this.testDataFileName = testDataFileName;
		this.columnsOfInputs = columnsOfInputs;
		this.columnsOfExpectedOuputs = columnsOfExpectedOuputs;
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

	

}
