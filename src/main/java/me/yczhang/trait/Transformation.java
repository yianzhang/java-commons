package me.yczhang.trait;

/**
 * Created by YCZhang on 11/25/15.
 */
public interface Transformation<S, T> {

	public T transform(S source);

}
