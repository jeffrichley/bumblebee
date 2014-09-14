package com.infinity.bumblebee.util;

public class Timer {

	private long start;
	private long end;
	private String name;

	public Timer(String name) {
		this.name = name;
	}
	
	public void start() {
		this.start = System.currentTimeMillis();
	}
	
	public void end() {
		this.end = System.currentTimeMillis();
	}
	
	public void log() {
		long millis = end-start;
		System.out.println(name + " time millis: " + millis + " time seconds: " + (double)millis/(double)1000 + " time minutes: " + (double)millis/(double)60000);
	}
	
	public void logDifference(Timer other) {
		long mine = end - start;
		long theirs = other.end - other.start;
		long diff = mine - theirs;
		double ratio = (double)mine / (double)theirs;
		System.out.println("Difference between " + name + " and " + other.name + " time millis: " + diff + " time seconds: " + (double)diff/(double)1000 + " time minutes: " + (double)diff/(double)60000);
		System.out.println("\tDifference ratio: " + ratio);
	}
	
	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public String getName() {
		return name;
	}
}
