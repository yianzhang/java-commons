package me.yczhang.data_structure.wrapper.primitive;

/**
 * Created by YCZhang on 7/7/16.
 */
public class CharWrapper {

	public char value;

	public CharWrapper() {

	}

	public CharWrapper(char value) {
		this.value = value;
	}

	public static CharWrapper of(char value) {
		return new CharWrapper(value);
	}
}
