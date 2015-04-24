package com.infinity.bumblebee.training.net;

import java.io.File;
import java.util.Arrays;

public class NetworkTrainerConfiguration {

	private String previousTrainingFile;
	private String trainingGroupsFile;
	private String trainingDataSaveFile;
	private String testDataFileName;
	private String progressReportFileName;
	private int columnsOfInputs;
	private int columnsOfExpectedOuputs;
	private int[] layers;
	private double lambda;
	private int maxTrainingIterations = 100;
	private File progressSaveDirectory;
	private File completeSaveDirectory;
	private double percentageForTraining = 1;
	private double percentabgeForTesting = 0;
	private double percentageForCrossValidation = 0;
	private int testingEveryNumberOfIterations = Integer.MAX_VALUE;
	private TrainingDataProviderType trainingDataProviderType = TrainingDataProviderType.GRADIENT_DECENT;
	private NormalizerMethod normalizationMethod;

	public enum TrainingDataProviderType {
		GRADIENT_DECENT, STOCHASTIC_GRADIENT_DECENT
	}
	
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

	String getPreviousTrainingFile() {
		return previousTrainingFile;
	}

	void setPreviousTrainingFile(String previousTrainingFile) {
		this.previousTrainingFile = previousTrainingFile;
	}

	TrainingDataProviderType getTrainingDataProviderType() {
		return trainingDataProviderType;
	}

	void setTrainingDataProviderType(
			TrainingDataProviderType trainingDataProviderType) {
		this.trainingDataProviderType = trainingDataProviderType;
	}

	double getPercentageForCrossValidation() {
		return percentageForCrossValidation;
	}

	void setPercentageForCrossValidation(double percentageForCrossValidation) {
		this.percentageForCrossValidation = percentageForCrossValidation;
	}

	public String getTrainingDataSaveFile() {
		return trainingDataSaveFile;
	}

	public void setTrainingDataSaveFile(String trainingDataSaveFile) {
		this.trainingDataSaveFile = trainingDataSaveFile;
	}

	public String getTrainingGroupsFile() {
		return trainingGroupsFile;
	}

	public void setTrainingGroupsFile(String trainingGroupsFile) {
		this.trainingGroupsFile = trainingGroupsFile;
	}

	public NormalizerMethod getNormalizationMethod() {
		return normalizationMethod;
	}

	public void setNormalizationMethod(NormalizerMethod normalizationMethod) {
		this.normalizationMethod = normalizationMethod;
	}

}
