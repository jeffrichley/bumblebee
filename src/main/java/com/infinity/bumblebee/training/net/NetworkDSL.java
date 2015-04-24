package com.infinity.bumblebee.training.net;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.infinity.bumblebee.data.BumbleMatrix;
import com.infinity.bumblebee.exceptions.BumbleException;
import com.infinity.bumblebee.math.DoubleVector;
import com.infinity.bumblebee.math.IterationCompletionListener;
import com.infinity.bumblebee.network.NeuralNet;
import com.infinity.bumblebee.training.data.TrainingProgress;
import com.infinity.bumblebee.training.data.TrainingResult;
import com.infinity.bumblebee.training.net.NetworkTrainerConfiguration.TrainingDataProviderType;
import com.infinity.bumblebee.util.BumbleMatrixMarshaller;

public class NetworkDSL {
	
	private NetworkTrainerConfiguration configuration = new NetworkTrainerConfiguration();
	
	private NetworkDSL() { 
		configuration.setMaxTrainingIterations(100);
		configuration.setLambda(0.3);
	}
	
	public static NetworkDSL usingTrainingData(String fileName) {
		NetworkDSL dsl = new NetworkDSL();
		dsl.configuration.setTestDataFileName(fileName);
		return dsl;
	}
	
	public static NetworkDSL usingPreviousTrainingFile(String fileName) {
		NetworkDSL dsl = new NetworkDSL();
		dsl.configuration.setTrainingGroupsFile(fileName);
		return dsl;
	}
	
	public NetworkDSL startingWithPreviousTraining(String fileName) {
		configuration.setPreviousTrainingFile(fileName);
		return this;
	}

	public NetworkDSLTrainer havingLayers(int... layers) {
		configuration.setLayers(layers);
		configuration.setColumnsOfInputs(layers[0]);
		configuration.setColumnsOfExpectedOuputs(layers[layers.length - 1]);
		
		return new NetworkDSLTrainer();
	}
	
	public class NetworkDSLTrainer {
		
		private List<TrainingListener> listeners = new ArrayList<TrainingListener>();
		
		private NetworkDSLTrainer() {
			// private so we can't be instatiated from somewhere else
		}
		
		NetworkTrainerConfiguration getConfiguration() {
			return configuration;
		}

		public NetworkDSLTrainer atMostIterations(int trainingIterations) {
			configuration.setMaxTrainingIterations(trainingIterations);
			return this;
		}

		public NetworkDSLTrainer withLearningRate(double learningRate) {
			configuration.setLambda(learningRate);
			return this;
		}
		
		public NetworkDSLTrainer withListener(TrainingListener listener) {
			listeners.add(listener);
			return this;
		}
		
		public NetworkDSLTrainer savingProgress(String directoryName) {
			return savingProgress(new File(directoryName));
		}
		
		public NetworkDSLTrainer savingProgress(File directory) {
			directory.mkdirs();
			configuration.setProgressSaveDirectory(directory);
			return this;
		}

		public NetworkDSLTrainer savingWhenComplete(String saveDir) {
			return savingWhenComplete(new File(saveDir));
		}
		
		public NetworkDSLTrainer savingWhenComplete(File saveDir) {
			saveDir.mkdirs();
			configuration.setCompleteSaveDirectory(saveDir);
			return this;
		}
		
		public NetworkDSLTrainer printingProgressReport(String progressReportFileName) {
			configuration.setProgressReportFileName(progressReportFileName);
			return this;
		}

		public TrainingResult train() {
			return train(false);
		}
		
		public TrainingResult train(boolean verbose) {
			final NetworkTrainer trainer = new NetworkTrainer(configuration);
			for (final TrainingListener listener : listeners) {
				trainer.addListener(new IterationCompletionListener() {
					@Override
					public void onIterationFinished(int iteration, double cost, DoubleVector currentWeights) {
						listener.onIterationFinished(iteration, cost, currentWeights);
					}
				});
			}
			
			// if we are wanting to save every progressive training
			if (configuration.getProgressSaveDirectory() != null) {
				trainer.addListener(new IterationCompletionListener() {
					
					private double lowestCost = Double.MAX_VALUE;
					private BumbleMatrixMarshaller marshaller = new BumbleMatrixMarshaller();
					
					@Override
					public void onIterationFinished(int iteration, double cost, DoubleVector currentWeights) {
						if (cost < lowestCost) {
							lowestCost = cost;
							try {
								File saveFileName = new File(configuration.getProgressSaveDirectory().getAbsolutePath() + System.getProperty("file.separator") + "trained-" + cost + ".bnet");
								marshaller.marshal(trainer.getCurrentNetwork(), new FileWriter(saveFileName));
							} catch (IOException e) {
								throw new BumbleException("Unable to save the network", e);
							}
						}
					}
				});
			}
			
			// if we are wanting to print out a progress report
			if (configuration.getProgressReportFileName() != null) {
				trainer.addListener(new IterationCompletionListener() {
					
					private final List<TrainingProgress> progressHistory = new ArrayList<>();
					private final Gson gson = new Gson();
					
					@Override
					public void onIterationFinished(int iteration, double cost, DoubleVector currentWeights) {
						TrainingProgress progress = new TrainingProgress(iteration, cost);
						progressHistory.add(progress);
						String json = gson.toJson(progressHistory);
						String progressReportFileName = configuration.getProgressReportFileName();
//						progressReportFileName.replace(".html", ".json");
						String dataFile = progressReportFileName.substring(0, progressReportFileName.lastIndexOf(System.getProperty("file.separator")) + 1) + "progress.json";
						try (FileWriter out = new FileWriter(dataFile)) {
							out.write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}

			// every once in a while we need to check how the error rate is
			trainer.addListener(new IterationCompletionListener() {
				
				@Override
				public void onIterationFinished(int iteration, double cost, DoubleVector currentWeights) {
					if (iteration != 0 && iteration % configuration.getTestingEveryNumberOfIterations() == 0) {
						NeuralNet net = trainer.getCurrentNetwork();
						
						BumbleMatrix testingInput = trainer.getTestingData();
						BumbleMatrix testingOutput = trainer.getTestingOutputData();
						PredictionEvaluator testingEvaluator = new PredictionEvaluator(testingInput, testingOutput, net);
						double testingPercentageCorrect = testingEvaluator.getPercentageCorrect();
						
						BumbleMatrix trainingInput = trainer.getInputData();
						BumbleMatrix trainingOutput = trainer.getOutputData();
						PredictionEvaluator trainingEvaluator = new PredictionEvaluator(trainingInput, trainingOutput, net);
						double trainingPercentageCorrect = trainingEvaluator.getPercentageCorrect();
						
						System.out.println("Percentage correct for iteration (train/test) " + iteration + ": " + trainingPercentageCorrect + " / " + testingPercentageCorrect);
					}
				}
			});
			
			final NeuralNet network = trainer.train(verbose);
			
			TrainingResult result = new TrainingResult();
			result.setNetwork(network);
			result.setTrainingSize(trainer.getInputData().getRowDimension());
			result.setTestingSize(trainer.getTestingData().getRowDimension());
			result.setCrossValidationSize(trainer.getCrossValidationData().getRowDimension());
			
			if (configuration.getCompleteSaveDirectory() != null) {
				BumbleMatrixMarshaller marshaller = new BumbleMatrixMarshaller();
				File saveFileName = new File(configuration.getCompleteSaveDirectory().getAbsolutePath() + System.getProperty("file.separator") + "trained-final.network");
				try {
					marshaller.marshal(trainer.getCurrentNetwork(), new FileWriter(saveFileName));
				} catch (IOException e) {
					throw new BumbleException("Unable to save the network", e);
				}
			}
			
			if (configuration.getPercentageForCrossValidation() > 0) {
				BumbleMatrix input = trainer.getCrossValidationData();
				BumbleMatrix output = trainer.getCrossValidationOutputData();
				PredictionEvaluator evaluator = new PredictionEvaluator(input, output, network);
				result.setCrossValidationPercent(evaluator.getPercentageCorrect());
				result.setCrossValidationPrecision(evaluator.getPrecision());
				result.setCrossValidationRecall(evaluator.getRecall());
				result.setCrossValidationF1(evaluator.getF1());
				result.setCrossValidationCost(evaluator.getCost());
				
				System.out.println("Final percentage correct: " + evaluator.getPercentageCorrect());
			}
			
			if (configuration.getPercentabgeForTesting() > 0) {
				BumbleMatrix testingInput = trainer.getTestingData();
				BumbleMatrix testingOutput = trainer.getTestingOutputData();
				PredictionEvaluator testingEvaluator = new PredictionEvaluator(testingInput, testingOutput, network);
				result.setTestPercent(testingEvaluator.getPercentageCorrect());
				result.setTestPrecision(testingEvaluator.getPrecision());
				result.setTestRecall(testingEvaluator.getRecall());
				result.setTestF1(testingEvaluator.getF1());
				result.setTestCost(testingEvaluator.getCost());
			}
			
			BumbleMatrix trainingInput = trainer.getInputData();
			BumbleMatrix trainingOutput = trainer.getOutputData();
			PredictionEvaluator trainingEvaluator = new PredictionEvaluator(trainingInput, trainingOutput, network);
			result.setTrainingPercent(trainingEvaluator.getPercentageCorrect());
			result.setTrainingPrecision(trainingEvaluator.getPrecision());
			result.setTrainingRecall(trainingEvaluator.getRecall());
			result.setTrainingF1(trainingEvaluator.getF1());
			result.setTrainingCost(trainingEvaluator.getCost());
			
			if (verbose) {
				System.out.println("Training is complete, the following are statistics about the result...");
				
				System.out.println("----------------------------------------------------------------------");
				System.out.println("Size:");
				System.out.println("\tTraining Set: " + result.getTrainingSize());
				System.out.println("\tTesting Set: " + result.getTestingSize());
				System.out.println("\tCross Validation Set: " + result.getCrossValidationSize());
				
				System.out.println("----------------------------------------------------------------------");
				System.out.println("Traing Set:");
				System.out.println("\tPercent Correct: " + result.getTrainingPercent() + "%");
				System.out.println("\tPrecision: " + result.getTrainingPrecision());
				System.out.println("\tRecall: " + result.getTrainingRecall());
				System.out.println("\tF1: " + result.getTrainingF1());
				System.out.println("\tCost: " + result.getTrainingCost());
				
				System.out.println("----------------------------------------------------------------------");
				System.out.println("Test Set:");
				System.out.println("\tPercent Correct: " + result.getTestPercent() + "%");
				System.out.println("\tPrecision: " + result.getTestPrecision());
				System.out.println("\tRecall: " + result.getTestRecall());
				System.out.println("\tF1: " + result.getTestF1());
				System.out.println("\tCost: " + result.getTestCost());
				
				System.out.println("----------------------------------------------------------------------");
				System.out.println("Cross Validation Set:");
				System.out.println("\tPercent Correct: " + result.getCrossValidationPercent() + "%");
				System.out.println("\tPrecision: " + result.getCrossValidationPrecision());
				System.out.println("\tRecall: " + result.getCrossValidationRecall());
				System.out.println("\tF1: " + result.getCrossValidationF1());
				System.out.println("\tCost: " + result.getCrossValidationCost());
				
				System.out.println("----------------------------------------------------------------------");
			}
			
			return result;
		}

		public NetworkDSLTrainer withPercentageForTraining(double percentage) {
			configuration.setPercentageForTraining(percentage);
			return this;
		}

		public NetworkDSLTrainer withPercentageForTesting(double percentage) {
			configuration.setPercentabgeForTesting(percentage);
			return this;
		}

		public NetworkDSLTrainer testingEveryNumberOfIterations(int iterations) {
			configuration.setTestingEveryNumberOfIterations(iterations);
			return this;
		}

		public NetworkDSLTrainer withLearningType(TrainingDataProviderType trainingDataProviderType) {
			configuration.setTrainingDataProviderType(trainingDataProviderType);
			return this;
		}
		
		public NetworkDSLTrainer withNormalizerMethod(NormalizerMethod method) {
			configuration.setNormalizationMethod(method);
			return this;
		}

		public NetworkDSLTrainer withPercentageForCrossValidation(double percentage) {
			configuration.setPercentageForCrossValidation(percentage);
			return this;
		}

		public NetworkDSLTrainer savingTrainingAs(String saveFile) {
			configuration.setTrainingDataSaveFile(saveFile);
			return this;
		}

	}

}
