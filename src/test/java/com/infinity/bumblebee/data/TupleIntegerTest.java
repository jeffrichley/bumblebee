package com.infinity.bumblebee.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.infinity.bumblebee.data.IntegerTuple;

public class TupleIntegerTest {

	private IntegerTuple cut;

	@Before
	public void setUp() throws Exception {
		cut = new IntegerTuple(2, 1);
	}

	@Test
	public void ensureCanGetOne() {
		assertThat(cut.getOne(), is(equalTo(2)));
	}

	@Test
	public void ensureCanGetTwo() {
		assertThat(cut.getTwo(), is(equalTo(1)));
	}
}
