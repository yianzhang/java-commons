package me.yczhang.data_structure.wrapper.primitive;

/**
 * Created by YCZhang on 7/7/16.
 */
public class DoubleWrapper {

	public double value;

	public DoubleWrapper() {

	}

	public DoubleWrapper(double value) {
		this.value = value;
	}

	public static DoubleWrapper of(double value) {
		return new DoubleWrapper(value);
	}
}
