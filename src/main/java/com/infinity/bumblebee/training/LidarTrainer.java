package com.infinity.bumblebee.training;

import static com.infinity.bumblebee.training.net.NetworkDSL.usingTrainingData;

public class LidarTrainer {

	public static void main(String[] args) {
				usingTrainingData("/Users/jeffreyrichley/Documents/workspace/raven/data/las_s23_1503_10.2011.fema-normalized.csv")
//				usingTrainingData("/Users/jeffreyrichley/Documents/workspace/raven/data/las_s23_1503_30.2011.fema-normalized.csv")
//				usingTrainingData("/Volumes/TANK/lidar/data/hampton/training-files/las_s23_1503_30.2011.fema-normalized.csv")
//				usingTrainingData("/Volumes/TANK/lidar/data/hampton/all-training-samples.csv")  	// (required) gives the file with training data
                .havingLayers(8100, 4000, 4)         				// (required) gives the shape of the neural network
                .atMostIterations(1000)        				// (optional) sets the maximum number of iterations to train (default: 100)
                .withLearningRate(0.3)         				// (optional) sets the learning rate (default: 0.3)
//                .savingProgress("/Volumes/TANK/lidar/training")
                .savingProgress("/Users/jeffreyrichley/Documents/workspace/raven/data")
//                .savingWhenComplete("/Volumes/TANK/lidar/training")
                .savingWhenComplete("/Users/jeffreyrichley/Documents/workspace/raven/data")
                .train(true);                      				// executes the training mechanism
	}

}
