package me.yczhang.trait;

/**
 * Created by YCZhang on 9/21/15.
 */
public interface Filter<T> {
	public boolean accept(T t);
}
