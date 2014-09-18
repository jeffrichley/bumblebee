package com.infinity.bumblebee.math;

/**
 * Minimizer interface for various function minimizers.
 * 
 * @author thomas.jungblut
 * 
 */
public interface Minimizer {

  /**
   * Minimizes the given costfunction with the starting parameter theta.
   * 
   * @param f the costfunction to minimize.
   * @param theta the starting parameters.
   * @param maxIterations the number of iterations to do.
   * @param verbose if TRUE it will print progress.
   * @return the optimized theta parameters.
   */
  DoubleVector minimize(CostFunction f, DoubleVector theta,
      int maxIterations, boolean verbose);

}