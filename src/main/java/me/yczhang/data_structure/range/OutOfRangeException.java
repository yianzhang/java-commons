package me.yczhang.data_structure.range;

/**
 * Created by YCZhang on 1/28/16.
 */
public class OutOfRangeException extends RuntimeException {
	private static final long serialVersionUID = 6209100451323675548L;

	public OutOfRangeException(int leftInclusive, int rightExclusive, int value) {
		super(String.format("%d is out of range [%d, %d)", value, leftInclusive, rightExclusive));
	}

	public OutOfRangeException(long leftInclusive, long rightExclusive, long value) {
		super(String.format("%d is out of range [%d, %d)", value, leftInclusive, rightExclusive));
	}
}
