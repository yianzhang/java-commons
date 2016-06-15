package me.yczhang.kit.bank;

/**
 * Created by YCZhang on 2/4/16.
 */
public class BeanBankException extends RuntimeException{
	private static final long serialVersionUID = -6387531135185576438L;

	public BeanBankException() {
	}

	public BeanBankException(String message) {
		super(message);
	}

	public BeanBankException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanBankException(Throwable cause) {
		super(cause);
	}

	public BeanBankException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
