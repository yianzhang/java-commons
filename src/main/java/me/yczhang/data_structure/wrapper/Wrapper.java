package me.yczhang.data_structure.wrapper;

/**
 * Created by YCZhang on 7/7/16.
 */
public class Wrapper<E> {

	public E value;

	public Wrapper() {

	}

	public Wrapper(E value) {
		this.value = value;
	}

	public static <E> Wrapper of(E value) {
		return new Wrapper<>(value);
	}
}
