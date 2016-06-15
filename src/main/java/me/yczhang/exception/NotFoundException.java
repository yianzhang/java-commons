package me.yczhang.exception;

/**
 * Created by YCZhang on 11/28/15.
 */
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6989031382911959753L;

	public NotFoundException() {
		super();
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}
}
