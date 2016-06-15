package me.yczhang.exception;

/**
 * Created by YCZhang on 2/4/16.
 */
public class TooMuchException extends RuntimeException {
	private static final long serialVersionUID = -8401741766786889531L;

	public TooMuchException() {
	}

	public TooMuchException(String message) {
		super(message);
	}

	public TooMuchException(String message, Throwable cause) {
		super(message, cause);
	}

	public TooMuchException(Throwable cause) {
		super(cause);
	}

	public TooMuchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
