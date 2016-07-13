package me.yczhang.data_structure.wrapper;

/**
 * Created by YCZhang on 7/7/16.
 */
public class StringWrapper {

	public String value;

	public StringWrapper() {

	}

	public StringWrapper(String value) {
		this.value = value;
	}

	public static StringWrapper of(String value) {
		return new StringWrapper(value);
	}
}
