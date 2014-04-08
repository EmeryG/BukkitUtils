package org.spacehq.bukkitutils.reflect;

public class ReflectException extends RuntimeException {

	public ReflectException() {
		super();
	}

	public ReflectException(String message) {
		super(message);
	}

	public ReflectException(Throwable cause) {
		super(cause);
	}

	public ReflectException(String message, Throwable cause) {
		super(message, cause);
	}

}
