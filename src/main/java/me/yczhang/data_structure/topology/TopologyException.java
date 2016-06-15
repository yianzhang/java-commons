package me.yczhang.data_structure.topology;

/**
 * Created by YCZhang on 1/15/16.
 */
public class TopologyException extends RuntimeException {

	private static final long serialVersionUID = 7124445652696336693L;

	public TopologyException() {
	}

	public TopologyException(String message) {
		super(message);
	}

	public TopologyException(String message, Throwable cause) {
		super(message, cause);
	}

	public TopologyException(Throwable cause) {
		super(cause);
	}

	public TopologyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
