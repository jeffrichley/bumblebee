Bumblebee Artificial Intelligence Library
=========

Bumblebee is an easy to use neural network library created with speed and ease of training in mind.

An Example Usage
---------

```java
import static com.infinity.bumblebee.training.net.NetworkDSL.usingTrainingData;

// Step 1: train the network
NeuralNet network = usingTrainingData("./test-data/iris.csv")  // (required) gives the file with training data
				 				.havingLayers(4, 4, 3)         // (required) gives the shape of the neural network
				 				.atMostIterations(1000)        // (optional) sets the maximum number of iterations to train (default: 100)
				 				.withLearningRate(0.3)         // (optional) sets the learning rate (default: 0.3)
				 				.train();                      // executes the training mechanism
		
// Step 2: create your input for prediction
BumbleMatrixFactory factory = new BumbleMatrixFactory();
BumbleMatrix input = factory.createMatrix(new double[][]{{5.1, 3.5, 1.4, 0.2}});

// Step 3 (option 1): ask the network for its prediction
int answer = network.predict(input).getAnswer()

// Step 3 (option 2): get the raw prediction array
BumbleMatrix answer = network.calculate(oneone);
```