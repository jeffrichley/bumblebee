package com.infinity.bumblebee.training;
import static com.infinity.bumblebee.training.net.NetworkDSL.usingTrainingData;

import java.io.IOException;

import com.infinity.bumblebee.training.net.NetworkTrainerConfiguration.TrainingDataProviderType;

public class Train {

	public static void main(String[] args) throws IOException {
		/*usingTrainingData("/Users/jeffreyrichley/Documents/workspace/raven/data/crunched-training.csv")*/
		/*usingTrainingData("/Users/jeffreyrichley/Documents/workspace/raven/data/crunched-training-houses.csv")*/
		/*usingTrainingData("/Volumes/TANK/lidar/data/hampton/quad-training-samples-house-only.csv")*/
//		usingTrainingData("C:/Users/Jeffrey.RICHLEY/Downloads/quad-training-house-only.csv")
		usingTrainingData("C:/Users/Jeffrey.RICHLEY/Downloads/quad-training-house-only-30x30.csv")
//			.startingWithPreviousTraining("/Volumes/TANK/lidar/training/bumble/houses/trained-804.8695337527753.bnet")
			.startingWithPreviousTraining("C:/Users/Jeffrey.RICHLEY/Downloads/progress/trained-0.1907606428082836.bnet")
 				.havingLayers(900, 600, 1)
// 				.atMostIterations(2500)
 				.withLearningRate(0.3)
 				.withPercentageForTraining(0.6)
 				.withPercentageForTesting(0.2)
 				.withPercentageForCrossValidation(0.2)
// 				.withLearningType(TrainingDataProviderType.STOCHASTIC_GRADIENT_DECENT)
 				.withLearningType(TrainingDataProviderType.GRADIENT_DECENT)
 				.testingEveryNumberOfIterations(5)
//				.printingProgressReport("/Users/jeffreyrichley/Documents/workspace/raven/data/training-report.html")
//				.savingProgress("/Volumes/TANK/lidar/training/bumble/houses")
//				.savingWhenComplete("/Volumes/TANK/lidar/training/bumble/houses/final")
				.savingProgress("C:/Users/Jeffrey.RICHLEY/Downloads/progress")
				.savingWhenComplete("C:/Users/Jeffrey.RICHLEY/Downloads")
				.train(true);
	}

}
