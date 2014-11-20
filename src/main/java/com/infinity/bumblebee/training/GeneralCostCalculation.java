package com.infinity.bumblebee.training;

public class GeneralCostCalculation implements CostCalculation {
	
	private final double cost;
	private final double regularization;

	public GeneralCostCalculation() {

		
		cost = 0;
		regularization = 0;
	}
	
	@Override
	public double getCost() {
		return cost;
	}

	@Override
	public double getRegularization() {
		return regularization;
	}

}
