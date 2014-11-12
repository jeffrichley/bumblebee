package com.infinity.bumblebee.training;
import static com.infinity.bumblebee.training.net.NetworkDSL.usingTrainingData;

import java.io.IOException;

import com.infinity.bumblebee.training.net.NetworkTrainerConfiguration.TrainingDataProviderType;

public class Train {

	public static void main(String[] args) throws IOException {
		/*usingTrainingData("/Users/jeffreyrichley/Documents/workspace/raven/data/crunched-training.csv")*/
		/*usingTrainingData("/Users/jeffreyrichley/Documents/workspace/raven/data/crunched-training-houses.csv")*/
		usingTrainingData("/Volumes/TANK/lidar/data/hampton/quad-training-samples-house-only.csv")
//			.startingWithPreviousTraining("/Volumes/TANK/lidar/training/bumble/houses/trained-804.8695337527753.bnet")
 				.havingLayers(8100, 4000, 1)
// 				.atMostIterations(2500)
 				.withLearningRate(0.3)
 				.withPercentageForTraining(0.8)
 				.withPercentageForTesting(0.2)
 				.withLearningType(TrainingDataProviderType.STOCASTIC_GRADIENT_DECENT)
 				.testingEveryNumberOfIterations(5)
				.printingProgressReport("/Users/jeffreyrichley/Documents/workspace/raven/data/training-report.html")
				.savingProgress("/Volumes/TANK/lidar/training/bumble/houses")
				.savingWhenComplete("/Volumes/TANK/lidar/training/bumble/houses/final")
				.train(true);
	}

}
