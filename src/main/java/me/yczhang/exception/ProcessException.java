package me.yczhang.exception;

/**
 * Created by YCZhang on 4/21/15.
 */
public class ProcessException extends Exception {
	public ProcessException() {
		super();
	}

	public ProcessException(String message) {
		super(message);
	}

	public ProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessException(Throwable cause) {
		super(cause);
	}
}
