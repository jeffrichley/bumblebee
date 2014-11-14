package com.infinity.bumblebee.util;

/**
 * Gives time since created, usually used in logging
 * @author Jeffrey.Richley
 */
public class TimerUtil {
	
	/**
	 * Instant in time this timer was created or last marked
	 */
	private long start;

	/**
	 * Constructor that sets when this timer was created
	 */
	public TimerUtil() {
		this.start = System.currentTimeMillis();
	}
	
	/**
	 * Gives the number of milliseconds since this timer was created or last marked
	 * @return The number of milliseconds since this timer was created or last marked
	 */
	public long markMillis() {
		long currentTimeMillis = System.currentTimeMillis();
		long mark = currentTimeMillis - start;
		start = currentTimeMillis;
		return mark;
	}
	
	/**
	 * Gives the number of seconds since this timer was created or last marked
	 * @return The number of seconds since this timer was created or last marked
	 */
	public long markSeconds() {
		return markMillis() / 60;
	}
}
