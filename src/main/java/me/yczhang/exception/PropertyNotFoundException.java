package me.yczhang.exception;

/**
 * Created by YCZhang on 15/3/31.
 */
public class PropertyNotFoundException extends Exception {
	public PropertyNotFoundException() {
		super();
	}

	public PropertyNotFoundException(String message) {
		super(message);
	}

	public PropertyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyNotFoundException(Throwable cause) {
		super(cause);
	}

	protected PropertyNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
