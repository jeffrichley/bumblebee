package com.infinity.bumblebee.training.data;

import com.infinity.bumblebee.network.NeuralNet;

public class TrainingResult {

	private NeuralNet network;
	
	private int trainingSize;
	private int testingSize;
	private int crossValidationSize;
	
	private double trainingCost;
	private double trainingPercent;
	private double trainingPrecision;
	private double trainingRecall;
	private double trainingF1;
	
	private double testCost;
	private double testPercent;
	private double testPrecision;
	private double testRecall;
	private double testF1;
	
	private double crossValidationCost;
	private double crossValidationPercent;
	private double crossValidationPrecision;
	private double crossValidationRecall;
	private double crossValidationF1;

	public NeuralNet getNetwork() {
		return network;
	}

	public void setNetwork(NeuralNet network) {
		this.network = network;
	}

	public double getTrainingCost() {
		return trainingCost;
	}

	public void setTrainingCost(double trainingCost) {
		this.trainingCost = trainingCost;
	}

	public double getTestCost() {
		return testCost;
	}

	public void setTestCost(double testCost) {
		this.testCost = testCost;
	}

	public double getCrossValidationCost() {
		return crossValidationCost;
	}

	public void setCrossValidationCost(double crossValidationCost) {
		this.crossValidationCost = crossValidationCost;
	}

	public double getTrainingPercent() {
		return trainingPercent;
	}

	public void setTrainingPercent(double trainingPercent) {
		this.trainingPercent = trainingPercent;
	}

	public double getTestPercent() {
		return testPercent;
	}

	public void setTestPercent(double testPercent) {
		this.testPercent = testPercent;
	}

	public double getCrossValidationPercent() {
		return crossValidationPercent;
	}

	public void setCrossValidationPercent(double crossValidationPercent) {
		this.crossValidationPercent = crossValidationPercent;
	}

	public double getTrainingPrecision() {
		return trainingPrecision;
	}

	public void setTrainingPrecision(double trainingPrecision) {
		this.trainingPrecision = trainingPrecision;
	}

	public double getTrainingRecall() {
		return trainingRecall;
	}

	public void setTrainingRecall(double trainingRecall) {
		this.trainingRecall = trainingRecall;
	}

	public double getTrainingF1() {
		return trainingF1;
	}

	public void setTrainingF1(double trainingF1) {
		this.trainingF1 = trainingF1;
	}

	public double getTestPrecision() {
		return testPrecision;
	}

	public void setTestPrecision(double testPrecision) {
		this.testPrecision = testPrecision;
	}

	public double getTestRecall() {
		return testRecall;
	}

	public void setTestRecall(double testRecall) {
		this.testRecall = testRecall;
	}

	public double getTestF1() {
		return testF1;
	}

	public void setTestF1(double testF1) {
		this.testF1 = testF1;
	}

	public double getCrossValidationPrecision() {
		return crossValidationPrecision;
	}

	public void setCrossValidationPrecision(double crossValidationPrecision) {
		this.crossValidationPrecision = crossValidationPrecision;
	}

	public double getCrossValidationRecall() {
		return crossValidationRecall;
	}

	public void setCrossValidationRecall(double crossValidationRecall) {
		this.crossValidationRecall = crossValidationRecall;
	}

	public double getCrossValidationF1() {
		return crossValidationF1;
	}

	public void setCrossValidationF1(double crossValidationF1) {
		this.crossValidationF1 = crossValidationF1;
	}

	public int getTrainingSize() {
		return trainingSize;
	}

	public void setTrainingSize(int trainingSize) {
		this.trainingSize = trainingSize;
	}

	public int getTestingSize() {
		return testingSize;
	}

	public void setTestingSize(int testingSize) {
		this.testingSize = testingSize;
	}

	public int getCrossValidationSize() {
		return crossValidationSize;
	}

	public void setCrossValidationSize(int crossValidationSize) {
		this.crossValidationSize = crossValidationSize;
	}
	
}
