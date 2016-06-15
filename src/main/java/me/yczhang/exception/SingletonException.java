package me.yczhang.exception;

/**
 * Created by YCZhang on 4/21/15.
 */
public class SingletonException extends RuntimeException {
	public SingletonException() {
		super();
	}

	public SingletonException(String message) {
		super(message);
	}

	public SingletonException(String message, Throwable cause) {
		super(message, cause);
	}

	public SingletonException(Throwable cause) {
		super(cause);
	}
}
