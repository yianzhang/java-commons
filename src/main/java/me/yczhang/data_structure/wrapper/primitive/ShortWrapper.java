package me.yczhang.data_structure.wrapper.primitive;

/**
 * Created by YCZhang on 7/7/16.
 */
public class ShortWrapper {

	public short value;

	public ShortWrapper() {

	}

	public ShortWrapper(short value) {
		this.value = value;
	}

	public static ShortWrapper of(short value) {
		return new ShortWrapper(value);
	}
}
