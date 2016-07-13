package me.yczhang.data_structure.wrapper.primitive;

/**
 * Created by YCZhang on 7/7/16.
 */
public class IntWrapper {

	public int value;

	public IntWrapper() {

	}

	public IntWrapper(int value) {
		this.value = value;
	}

	public static IntWrapper of(int value) {
		return new IntWrapper(value);
	}
}
