package com.infinity.bumblebee.training.net;

import java.util.ArrayList;
import java.util.List;

import com.infinity.bumblebee.math.DoubleVector;
import com.infinity.bumblebee.math.IterationCompletionListener;
import com.infinity.bumblebee.network.NeuralNet;

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

		public NeuralNet train() {
			return train(false);
		}
		
		public NeuralNet train(boolean verbose) {
			NetworkTrainer trainer = new NetworkTrainer(configuration);
			for (final TrainingListener listener : listeners) {
				trainer.addListener(new IterationCompletionListener() {
					@Override
					public void onIterationFinished(int iteration, double cost, DoubleVector currentWeights) {
						listener.onIterationFinished(iteration, cost, currentWeights);
					}
				});
			}
			return trainer.train(verbose);
		}
		
	}
}
