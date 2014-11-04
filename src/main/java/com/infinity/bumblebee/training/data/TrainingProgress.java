package com.infinity.bumblebee.training.data;

public class TrainingProgress {

	private final int iteration;
	private final double cost;

	public TrainingProgress(int iteration, double cost) {
		this.iteration = iteration;
		this.cost = cost;
	}

	public int getIteration() {
		return iteration;
	}

	public double getCost() {
		return cost;
	}
	
}
