package me.yczhang.util;

/**
 * Created by YCZhang on 7/16/15.
 */
public class ObjUtil {

	public static <T> T isNull(T t, T def) {
		return ifNull(t, def);
	}

	public static <T> T ifNull(T t, T def) {
		return t == null ? def : t;
	}

	public static <T> int compare(T t1, T t2) {
		return compare(t1, t2, false);
	}

	public static <T> int compare(T t1, T t2, boolean nullGreater) {
		return CompareUtil.compare(t1, t2, nullGreater);
	}
}
