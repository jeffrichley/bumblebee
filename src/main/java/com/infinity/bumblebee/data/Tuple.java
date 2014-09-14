package com.infinity.bumblebee.data;


/**
 * A tuple object holding two distinct values
 * @author Jeffrey.Richley
 */
public class Tuple<T extends Number> {

	private final T one;
	private final T two;

	public Tuple(T one, T two) {
		this.one = one;
		this.two = two;
	}

	public T getOne() {
		return one;
	}

	public T getTwo() {
		return two;
	}
	
}
