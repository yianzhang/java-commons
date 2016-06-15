package me.yczhang.exception;

/**
 * Created by YCZhang on 15/3/31.
 */
public class AppException extends Exception {

	private static final long serialVersionUID = 1036134689886422634L;

	public AppException() {
		super();
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(Throwable cause) {
		super(cause);
	}

	protected AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
