package me.yczhang.util;

/**
 * Created by YCZhang on 10/9/15.
 */
@Deprecated
public class MathUtil {

	public static double dotProduct(double x1, double y1, double x2, double y2) {
		return x1 * x2 + y1 * y2;
	}

	public static double crossProduct(double x1, double y1, double x2, double y2) {
		return x1 * y2 - x2 * y1;
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

}
