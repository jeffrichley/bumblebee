package com.infinity.bumblebee.network;

public class Prediction {

	private final int answer;

	public Prediction(int answer, double score) {
		this.answer = answer;
		this.score = score;
	}

	public int getAnswer() {
		return answer;
	}

	public double getScore() {
		return score;
	}

	private final double score;

}
