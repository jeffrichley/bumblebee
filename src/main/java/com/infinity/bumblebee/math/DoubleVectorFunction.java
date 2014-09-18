package com.infinity.bumblebee.math;

/**
 * A function that can be applied to a double vector via {@link DoubleVector}
 * #apply({@link DoubleVectorFunction} f);
 */
public interface DoubleVectorFunction {

  /**
   * Calculates the result with a given index and value of a vector.
   */
  double calculate(int index, double value);

}