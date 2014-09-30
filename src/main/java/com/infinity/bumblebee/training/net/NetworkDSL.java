package com.infinity.bumblebee.training.net;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.infinity.bumblebee.exceptions.BumbleException;
import com.infinity.bumblebee.math.DoubleVector;
import com.infinity.bumblebee.math.IterationCompletionListener;
import com.infinity.bumblebee.network.NeuralNet;
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

		public NeuralNet train() {
			return train(false);
		}
		
		public NeuralNet train(boolean verbose) {
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
								File saveFileName = new File(configuration.getProgressSaveDirectory().getAbsolutePath() + System.getProperty("file.separator") + "trained-" + cost + ".network");
								marshaller.marshal(trainer.getCurrentNetwork(), new FileWriter(saveFileName));
							} catch (IOException e) {
								throw new BumbleException("Unable to save the network", e);
							}
						}
					}
				});
			}
			
			NeuralNet network = trainer.train(verbose);
			
			if (configuration.getCompleteSaveDirectory() != null) {
				BumbleMatrixMarshaller marshaller = new BumbleMatrixMarshaller();
				File saveFileName = new File(configuration.getCompleteSaveDirectory().getAbsolutePath() + System.getProperty("file.separator") + "trained-final.network");
				try {
					marshaller.marshal(trainer.getCurrentNetwork(), new FileWriter(saveFileName));
				} catch (IOException e) {
					throw new BumbleException("Unable to save the network", e);
				}
			}
			
			return network;
		}
		
	}
}
