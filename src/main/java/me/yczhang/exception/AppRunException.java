package me.yczhang.exception;

/**
 * Created by YCZhang on 6/25/15.
 */
public class AppRunException extends RuntimeException {
	public AppRunException() {
	}

	public AppRunException(String message) {
		super(message);
	}

	public AppRunException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppRunException(Throwable cause) {
		super(cause);
	}
}
