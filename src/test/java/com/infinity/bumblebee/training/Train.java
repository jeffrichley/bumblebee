package com.infinity.bumblebee.training;
import static com.infinity.bumblebee.training.net.NetworkDSL.usingTrainingData;

import java.io.IOException;

public class Train {

	public static void main(String[] args) throws IOException {
		/*usingTrainingData("/Users/jeffreyrichley/Documents/workspace/raven/data/crunched-training.csv")*/
		usingTrainingData("/Users/jeffreyrichley/Documents/workspace/raven/data/crunched-training-houses.csv")
 				.havingLayers(250, 150, 1)
 				.atMostIterations(2500)
 				.withLearningRate(0.3)
 				.withPercentageForTraining(0.8)
 				.withPercentageForTesting(0.2)
 				.testingEveryNumberOfIterations(1)
				.printingProgressReport("/Users/jeffreyrichley/Documents/workspace/raven/data/training-report.html")
				.savingProgress("/Volumes/TANK/lidar/training/bumble/houses")
				.savingWhenComplete("/Volumes/TANK/lidar/training/bumble/houses/final")
				.train(true);
	}

}
