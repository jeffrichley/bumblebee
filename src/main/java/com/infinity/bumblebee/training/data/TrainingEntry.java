package com.infinity.bumblebee.training.data;

public class TrainingEntry {

	private final double[] input;
	private final double[] output;

	public TrainingEntry(double[] input, double[] output) {
		this.input = input;
		this.output = output;
	}

	public double[] getInput() {
		return input;
	}

	public double[] getOutput() {
		return output;
	}
	
}
