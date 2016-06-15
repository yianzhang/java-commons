package me.yczhang.exception;

/**
 * Created by YCZhang on 2/4/16.
 */
public class RepeatingInvokeException extends RuntimeException {
	private static final long serialVersionUID = -9167881726798307613L;

	public RepeatingInvokeException() {
	}

	public RepeatingInvokeException(String message) {
		super(message);
	}

	public RepeatingInvokeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepeatingInvokeException(Throwable cause) {
		super(cause);
	}

	public RepeatingInvokeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
