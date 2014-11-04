package com.infinity.bumblebee.training.net;

import java.io.File;
import java.util.Arrays;

public class NetworkTrainerConfiguration {

	private String testDataFileName;
	private String progressReportFileName;
	private int columnsOfInputs;
	private int columnsOfExpectedOuputs;
	private int[] layers;
	private double lambda;
	private int maxTrainingIterations;
	private File progressSaveDirectory;
	private File completeSaveDirectory;
	private double percentageForTraining = 1;
	private double percentabgeForTesting = 0;
	private int testingEveryNumberOfIterations = Integer.MAX_VALUE;
	
	public void setTrainingData(String testDataFileName, int maxTrainingIterations, double lambda, int... layers) {
		this.testDataFileName = testDataFileName;
		this.columnsOfInputs = layers[0];
		this.columnsOfExpectedOuputs = layers[layers.length - 1];
		this.layers = Arrays.copyOf(layers, layers.length);
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
		return Arrays.copyOf(layers, layers.length);
	}

	public double getLambda() {
		return lambda;
	}

	public int getMaxTrainingIterations() {
		return maxTrainingIterations;
	}

	void setTestDataFileName(String testDataFileName) {
		this.testDataFileName = testDataFileName;
	}

	void setColumnsOfInputs(int columnsOfInputs) {
		this.columnsOfInputs = columnsOfInputs;
	}

	void setColumnsOfExpectedOuputs(int columnsOfExpectedOuputs) {
		this.columnsOfExpectedOuputs = columnsOfExpectedOuputs;
	}

	void setLayers(int[] layers) {
		this.layers = layers;
	}

	void setLambda(double lambda) {
		this.lambda = lambda;
	}

	void setMaxTrainingIterations(int maxTrainingIterations) {
		this.maxTrainingIterations = maxTrainingIterations;
	}

	public File getProgressSaveDirectory() {
		return progressSaveDirectory;
	}

	void setProgressSaveDirectory(File progressSaveDirectory) {
		this.progressSaveDirectory = progressSaveDirectory;
	}

	public File getCompleteSaveDirectory() {
		return completeSaveDirectory;
	}

	void setCompleteSaveDirectory(File completeSaveDirectory) {
		this.completeSaveDirectory = completeSaveDirectory;
	}

	String getProgressReportFileName() {
		return progressReportFileName;
	}

	void setProgressReportFileName(String progressReportFileName) {
		this.progressReportFileName = progressReportFileName;
	}

	double getPercentageForTraining() {
		return percentageForTraining;
	}

	void setPercentageForTraining(double percentageForTraining) {
		this.percentageForTraining = percentageForTraining;
	}

	double getPercentabgeForTesting() {
		return percentabgeForTesting;
	}

	void setPercentabgeForTesting(double percentabgeForTesting) {
		this.percentabgeForTesting = percentabgeForTesting;
	}

	int getTestingEveryNumberOfIterations() {
		return testingEveryNumberOfIterations;
	}

	void setTestingEveryNumberOfIterations(
			int testingEveryNumberOfIterations) {
		this.testingEveryNumberOfIterations = testingEveryNumberOfIterations;
	}

}
