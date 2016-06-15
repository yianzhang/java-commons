package me.yczhang.util;

/**
 * Created by YCZhang on 7/27/15.
 */
public class ArrayUtil {

	/**
	 * 将数组用分隔符连接成字符串
	 * @param src 数组
	 * @param sep 分隔符
	 * @return
	 */
	public static String join(Object[] src, String sep) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < src.length; ++i) {
			if (i > 0)
				ret.append(sep);
			ret.append(src[i].toString());
		}

		return ret.toString();
	}
}
