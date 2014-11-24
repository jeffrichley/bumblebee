package com.infinity.bumblebee.training;
import static com.infinity.bumblebee.training.net.NetworkDSL.usingTrainingData;

import java.io.IOException;

import com.infinity.bumblebee.training.net.NetworkTrainerConfiguration.TrainingDataProviderType;

public class Train {

	public static void main(String[] args) throws IOException {
		double scale = 0.5;
		System.out.println("Using " + scale*100 + "% of the training set");
		
		usingTrainingData("/Users/jeffreyrichley/Documents/workspace/raven/data/3030/quad-training-house-only-30x30.csv")
//			.startingWithPreviousTraining("/Users/jeffreyrichley/Documents/workspace/raven/data/3030/tmp/trained-0.22844436780672028.bnet")
 				.havingLayers(900, 600, 1)
// 				.atMostIterations(2500)
 				.withLearningRate(0.3)
// 				.withPercentageForTraining(0.1)
// 				.withPercentageForTesting(0.05)
// 				.withPercentageForCrossValidation(0.05)
 				.withPercentageForTraining(0.6 * scale)
 				.withPercentageForTesting(0.2 * scale)
 				.withPercentageForCrossValidation(0.2 * scale)
// 				.withLearningType(TrainingDataProviderType.STOCHASTIC_GRADIENT_DECENT)
 				.withLearningType(TrainingDataProviderType.GRADIENT_DECENT)
 				.testingEveryNumberOfIterations(5)
//				.printingProgressReport("/Users/jeffreyrichley/Documents/workspace/raven/data/training-report.html")
//				.savingProgress("/Volumes/TANK/lidar/training/bumble/houses")
//				.savingWhenComplete("/Volumes/TANK/lidar/training/bumble/houses/final")
//				.savingProgress("/Volumes/TANK/lidar/training/bumble/3030/working")
//				.savingWhenComplete("/Volumes/TANK/lidar/training/bumble/3030")
				.savingProgress("/Users/jeffreyrichley/Documents/workspace/raven/data/3030/tmp/")
				.savingWhenComplete("/Users/jeffreyrichley/Documents/workspace/raven/data/3030")
				.train(true);
		
		new Ding().ding();
	}

}
