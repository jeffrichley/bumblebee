package com.infinity.bumblebee.training;
import static com.infinity.bumblebee.training.net.NetworkDSL.usingTrainingData;
import static com.infinity.bumblebee.training.net.NetworkDSL.usingPreviousTrainingFile;

import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.data.BumbleMatrixFactory;
import com.infinity.bumblebee.training.net.NormalizerMethod;
import com.infinity.bumblebee.training.net.NetworkTrainerConfiguration.TrainingDataProviderType;

public class Train {

	public static void main(String[] args) throws IOException {
		double scale = 1;
		System.out.println("Using " + scale*100 + "% of the training set");
		
		NormalizerMethod normalizer = new NormalizerMethod() {
			@Override
			public BumbleMatrix normalize(BumbleMatrix original) {
				BumbleMatrixFactory factory = new BumbleMatrixFactory();
				BumbleMatrix normalized = factory.createMatrix(original.getRowDimension(), original.getColumnDimension());
				DescriptiveStatistics stats = new DescriptiveStatistics();
				
				for (int row = 0; row < original.getRowDimension(); row++) {
					for (int column = 0; column < original.getColumnDimension(); column++) {
						double value = original.getEntry(row, column);
						if (Double.isInfinite(value) || Double.isNaN(value)) {
							value = 0;
						} else if (value > 100) {
							value = 100;
						} else if (value < -25) {
							value = -25;
						}
						stats.addValue(value);
					}
				}
				
				double mean = stats.getMean();
				double std = stats.getStandardDeviation();
				
				for (int row = 0; row < original.getRowDimension(); row++) {
					for (int column = 0; column < original.getColumnDimension(); column++) {
						double entry = original.getEntry(row, column);
						double value = (entry - mean) / std;
						normalized.setEntry(row, column, value);
					}
				}
				
				return normalized;
			}
		};
		
//		usingTrainingData("/Volumes/TANK/lidar/training/bumble/quad-training-house-only-30x30.csv")
		usingPreviousTrainingFile("/Volumes/TANK/lidar/training/bumble/30x30-all/quad-training-house-only-all-normalized-30x30.bee")
		
			.startingWithPreviousTraining("/Volumes/TANK/lidar/training/bumble/30x30-all/networks/trained-0.13159958808077582.bnet")
 				.havingLayers(900, 600, 1)
// 				.atMostIterations(2500)
 				.withLearningRate(0.3)
 				.withPercentageForTraining(0.6 * scale)
 				.withPercentageForTesting(0.2 * scale)
 				.withPercentageForCrossValidation(0.2 * scale)
 				.withLearningType(TrainingDataProviderType.GRADIENT_DECENT)
 				.withNormalizerMethod(normalizer )
 				.testingEveryNumberOfIterations(5)
//				.printingProgressReport("/Users/jeffreyrichley/Documents/workspace/raven/data/training-report.html")
				.savingProgress("/Volumes/TANK/lidar/training/bumble/30x30-all/networks")
//				.savingTrainingAs("/Users/jeffreyrichley/Documents/workspace/raven/data/3030/quad-training-house-only-all-30x30.bee")
				.savingWhenComplete("/Volumes/TANK/lidar/training/bumble/30x30-all")
				.train(true);
		
		new Ding().ding();
	}

}
