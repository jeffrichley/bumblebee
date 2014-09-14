package com.infinity.bumblebee.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TimerTest {

	private Timer cut;

	@Before
	public void setUp() throws Exception {
		cut = new Timer("Test Timer");
	}

	@Test
	public void testTiming() {
		assertThat(cut.getName(), is(equalTo("Test Timer")));
		
		assertThat(cut.getStart(), is(equalTo(0l)));
		assertThat(cut.getEnd(), is(equalTo(0l)));
		
		cut.start();
		cut.end();
		
		assertThat(cut.getStart(), is(not(equalTo(0l))));
		assertThat(cut.getEnd(), is(not(equalTo(0l))));
	}

}
