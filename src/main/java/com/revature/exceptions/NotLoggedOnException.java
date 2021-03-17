package com.revature.exceptions;

public class NotLoggedOnException extends RuntimeException {

	public NotLoggedOnException() {
	}

	public NotLoggedOnException(String message) {
		super(message);
	}

	public NotLoggedOnException(Throwable cause) {
		super(cause);
	}

	public NotLoggedOnException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotLoggedOnException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

