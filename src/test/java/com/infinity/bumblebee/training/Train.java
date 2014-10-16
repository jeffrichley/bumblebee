package com.infinity.bumblebee.training;
import static com.infinity.bumblebee.training.net.NetworkDSL.usingTrainingData;

import com.infinity.bumblebee.math.DoubleVector;
import com.infinity.bumblebee.network.NeuralNet;
import com.infinity.bumblebee.training.net.TrainingListener;

public class Train {

	public static void main(String[] args) {
		final long start = System.currentTimeMillis();
		
		NeuralNet network = usingTrainingData("/Users/jeffreyrichley/Documents/workspace/raven/data/big-training-set.csv")
 				.havingLayers(8100, 4000, 4)
 				.atMostIterations(1000)
 				.withLearningRate(0.3)
 				.withListener(new TrainingListener() {
					@Override
					public void onIterationFinished(int iteration, double cost, DoubleVector currentWeights) {
						System.out.println("Iteration: " + iteration + " Cost: " + cost);
						long end = System.currentTimeMillis();
						
						long time = (end - start) / iteration;
						System.out.println("Time Per: " + time);
					}
				}).train(true);
	}

}
