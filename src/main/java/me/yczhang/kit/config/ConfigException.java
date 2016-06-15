package me.yczhang.kit.config;

/**
 * Created by YCZhang on 9/14/15.
 */
public class ConfigException extends RuntimeException {
	private static final long serialVersionUID = -5062851594871184436L;

	public ConfigException() {
		super();
	}

	public ConfigException(String message) {
		super(message);
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigException(Throwable cause) {
		super(cause);
	}

	protected ConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
