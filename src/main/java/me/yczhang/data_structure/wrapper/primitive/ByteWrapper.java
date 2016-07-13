package me.yczhang.data_structure.wrapper.primitive;

/**
 * Created by YCZhang on 7/7/16.
 */
public class ByteWrapper {

	public byte value;

	public ByteWrapper() {

	}

	public ByteWrapper(byte value) {
		this.value = value;
	}

	public static ByteWrapper of(byte value) {
		return new ByteWrapper(value);
	}
}
