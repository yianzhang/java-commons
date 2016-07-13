package me.yczhang.data_structure.wrapper.primitive;

/**
 * Created by YCZhang on 7/7/16.
 */
public class BoolWrapper {

	public boolean value;

	public BoolWrapper() {

	}

	public BoolWrapper(boolean value) {
		this.value = value;
	}

	public static BoolWrapper of(boolean value) {
		return new BoolWrapper(value);
	}
}
