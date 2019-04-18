package com.washnow.admin.exception;

import java.io.IOException;

public class HandlerException extends IOException {

	public HandlerException() {
		super();
	}

	public HandlerException(String message) {
		super(message);
	}

	public HandlerException(String message, Throwable cause) {
		super(message);
		initCause(cause);
	}

	@Override
	public String toString() {
		if (getCause() != null) {
			return getLocalizedMessage() + ": " + getCause();
		} else {
			return getLocalizedMessage();
		}
	}

	public static class UnauthorizedException extends HandlerException {
		public UnauthorizedException() {
		}

		public UnauthorizedException(String message) {
			super(message);
		}

		public UnauthorizedException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public static class NoDevsiteProfileException extends HandlerException {
		public NoDevsiteProfileException() {
		}

		public NoDevsiteProfileException(String message) {
			super(message);
		}

		public NoDevsiteProfileException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
