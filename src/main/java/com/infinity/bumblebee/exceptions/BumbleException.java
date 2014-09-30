package com.infinity.bumblebee.exceptions;

public class BumbleException extends RuntimeException {

	/** Generated serial version */
	private static final long serialVersionUID = 2090217882715135491L;

	public BumbleException(String arg0) {
		super(arg0);
	}

	public BumbleException(Throwable arg0) {
		super(arg0);
	}

	public BumbleException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public BumbleException(String arg0, Throwable arg1, boolean arg2,boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
