package me.yczhang.data_structure.wrapper.primitive;

/**
 * Created by YCZhang on 7/7/16.
 */
public class LongWrapper {

	public long value;

	public LongWrapper() {

	}

	public LongWrapper(long value) {
		this.value = value;
	}

	public static LongWrapper of(long value) {
		return new LongWrapper(value);
	}
}
