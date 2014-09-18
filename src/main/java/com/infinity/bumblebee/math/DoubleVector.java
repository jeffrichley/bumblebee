package com.infinity.bumblebee.math;

import java.util.Iterator;

/**
 * Vector with doubles. Some of the operations are mutable, unlike the apply and
 * math functions, they return a fresh instance every time.
 * 
 */
public interface DoubleVector {

	/**
	 * Retrieves the value at given index.
	 * 
	 * @param index
	 *            the index.
	 * @return a double value at the index.
	 */
	double get(int index);

	/**
	 * Get the length of a vector, for sparse instance it is the actual length.
	 * (not the dimension!) Always a constant time operation.
	 * 
	 * @return the length of the vector.
	 */
	int getLength();

	/**
	 * Get the dimension of a vector, for dense instance it is the same like the
	 * length, for sparse instances it is usually not the same. Always a
	 * constant time operation.
	 * 
	 * @return the dimension of the vector.
	 */
	int getDimension();

	/**
	 * Set a value at the given index.
	 * 
	 * @param index
	 *            the index of the vector to set.
	 * @param value
	 *            the value at the index of the vector to set.
	 */
	void set(int index, double value);

	/**
	 * Apply a given {@link DoubleVectorFunction} to this vector and return a
	 * new one.
	 * 
	 * @param func
	 *            the function to apply.
	 * @return a new vector with the applied function.
	 */
	DoubleVector apply(DoubleVectorFunction func);

	/**
	 * Apply a given {@link DoubleDoubleVectorFunction} to this vector and the
	 * other given vector. Both vectors must match in dimensions.
	 * 
	 * @param other
	 *            the other vector.
	 * @param func
	 *            the function to apply on this and the other vector.
	 * @return a new vector with the result of the function of the two vectors.
	 */
	DoubleVector apply(DoubleVector other, DoubleDoubleVectorFunction func);

	/**
	 * Adds the given {@link DoubleVector} to this vector.
	 * 
	 * @param v
	 *            the other vector.
	 * @return a new vector with the sum of both vectors at each element index.
	 */
	DoubleVector add(DoubleVector v);

	/**
	 * Adds the given scalar to this vector.
	 * 
	 * @param scalar
	 *            the scalar.
	 * @return a new vector with the result at each element index.
	 */
	DoubleVector add(double scalar);

	/**
	 * Subtracts this vector by the given {@link DoubleVector}.
	 * 
	 * @param v
	 *            the other vector.
	 * @return a new vector with the difference of both vectors.
	 */
	DoubleVector subtract(DoubleVector v);

	/**
	 * Subtracts the given scalar to this vector. (vector - scalar).
	 * 
	 * @param scalar
	 *            the scalar.
	 * @return a new vector with the result at each element index.
	 */
	DoubleVector subtract(double scalar);

	/**
	 * Subtracts the given scalar from this vector. (scalar - vector).
	 * 
	 * @param scalar
	 *            the scalar.
	 * @return a new vector with the result at each element index.
	 */
	DoubleVector subtractFrom(double scalar);

	/**
	 * Multiplies the given scalar to this vector.
	 * 
	 * @param scalar
	 *            the scalar.
	 * @return a new vector with the result of the operation.
	 */
	DoubleVector multiply(double scalar);

	/**
	 * Multiplies the given {@link DoubleVector} with this vector.
	 * 
	 * @param vector
	 *            the other vector.
	 * @return a new vector with the result of the operation.
	 */
	DoubleVector multiply(DoubleVector vector);

	/**
	 * Divides this vector by the given scalar. (= vector/scalar).
	 * 
	 * @param scalar
	 *            the given scalar.
	 * @return a new vector with the result of the operation.
	 */
	DoubleVector divide(double scalar);

	/**
	 * Divides the given scalar by this vector. (= scalar/vector).
	 * 
	 * @param scalar
	 *            the given scalar.
	 * @return a new vector with the result of the operation.
	 */
	DoubleVector divideFrom(double scalar);

	/**
	 * Divides the given vector by this vector (= parameter vector / this
	 * vector).
	 * 
	 * @param vector
	 *            the given vector.
	 * @return a new vector with the result of the operation.
	 */
	DoubleVector divideFrom(DoubleVector vector);

	/**
	 * Divides this vector by the given vector. (= vector/parameter vector).
	 * 
	 * @param vector
	 *            the given vector.
	 * @return a new vector with the result of the operation.
	 */
	DoubleVector divide(DoubleVector vector);

	/**
	 * Powers this vector by the given amount. (=vector^x).
	 * 
	 * @param x
	 *            the given exponent.
	 * @return a new vector with the result of the operation.
	 */
	DoubleVector pow(double x);

	/**
	 * Absolutes the vector at each element.
	 * 
	 * @return a new vector that does not contain negative values anymore.
	 */
	DoubleVector abs();

	/**
	 * Square-roots each element.
	 * 
	 * @return a new vector.
	 */
	DoubleVector sqrt();

	/**
	 * Log base e for each element.
	 * 
	 * @return a new vector.
	 */
	DoubleVector log();

	/**
	 * Exponentiates every element with base e.
	 * 
	 * @return a new vector.
	 */
	DoubleVector exp();

	/**
	 * @return the sum of all elements in this vector.
	 */
	double sum();

	/**
	 * Calculates the dot product between this vector and the given vector.
	 * 
	 * @param s
	 *            the given vector s.
	 * @return the dot product as a double.
	 */
	double dot(DoubleVector s);

	/**
	 * Slices this vector from index 0 to the end index.
	 * 
	 * @param end
	 *            must be > 0 and smaller than the dimension of the vector.
	 * @return a new vector that is only (0:end) long.
	 */
	DoubleVector slice(int end);

	/**
	 * Slices this vector from start index to the given end index (excluding).
	 * Length of the new vector is than (end-start)
	 * 
	 * @param start
	 *            must be > 0 and smaller than the dimension of the vector
	 * @param end
	 *            must be > 0 and smaller than the dimension of the vector. This
	 *            must be greater than the start.
	 * @return a new vector that is only (end-start) long.
	 */
	DoubleVector slice(int start, int end);

	/**
	 * Slices this vector from start index with the given length. So you end at
	 * the upper bound of (start+length-1 as as the last value in your new
	 * vector).
	 * 
	 * @param start
	 *            must be > 0 and smaller than the dimension of the vector
	 * @param length
	 *            number of elements to take, start+length must be <= the vector
	 *            length
	 * @return a new vector that is only (length) long.
	 */
	DoubleVector sliceByLength(int start, int length);

	/**
	 * @return the maximum element value in this vector. Note that on sparse
	 *         instances you may not see zero as the maximum.
	 */
	double max();

	/**
	 * @return the minimum element value in this vector. Note that on sparse
	 *         instances you may not see zero as the minimum.
	 */
	double min();

	/**
	 * @return the index where the element value in this vector is the maximum.
	 *         Note that on sparse instances you may not see indices that
	 *         contain a zero as the maximum.
	 */
	int maxIndex();

	/**
	 * @return the index where the element value in this vector is the
	 *         minimum.Note that on sparse instances you may not see indices
	 *         that contain a zero as the minimum.
	 */
	int minIndex();

	/**
	 * @return an array representation of this vector.
	 */
	double[] toArray();

	/**
	 * @return a fresh new copy of this vector, copies all elements to a new
	 *         vector. (Does not reuse references or stuff).
	 */
	DoubleVector deepCopy();

	/**
	 * @return an iterator that only iterates over non zero elements.
	 */
	Iterator<DoubleVectorElement> iterateNonZero();

	/**
	 * @return an iterator that iterates over all elements.
	 */
	Iterator<DoubleVectorElement> iterate();

	/**
	 * @return true if this instance is a sparse vector. Smarter and faster than
	 *         instanceof.
	 */
	boolean isSparse();

	/**
	 * @return true if this instance is a named vector. Smarter and faster than
	 *         instanceof.
	 */
	boolean isNamed();

	/**
	 * @return true if this instance is a single entry vector. Smarter and
	 *         faster than instanceof.
	 */
	boolean isSingle();

	/**
	 * @return If this vector is a named instance, this will return its name. Or
	 *         null if this is not a named instance.
	 * 
	 */
	String getName();

	/**
	 * Class for iteration of elements, consists of an index and a value at this
	 * index. May be reused for performance/GC purposes.
	 */
	public static final class DoubleVectorElement {

		private int index;
		private double value;

		public DoubleVectorElement() {
			super();
		}

		public int getIndex() {
			return index;
		}

		public double getValue() {
			return value;
		}

		public void setIndex(int in) {
			this.index = in;
		}

		public void setValue(double in) {
			this.value = in;
		}

		@Override
		public String toString() {
			return index + " -> " + value;
		}
	}

}
