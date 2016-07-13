package me.yczhang.data_structure.wrapper.primitive;

/**
 * Created by YCZhang on 7/7/16.
 */
public class FloatWrapper {

	public float value;

	public FloatWrapper() {

	}

	public FloatWrapper(float value) {
		this.value = value;
	}

	public static FloatWrapper of(float value) {
		return new FloatWrapper(value);
	}
}
