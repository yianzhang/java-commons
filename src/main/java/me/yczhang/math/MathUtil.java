package me.yczhang.math;

import java.util.Collection;

/**
 * Created by YCZhang on 1/4/16.
 */
public class MathUtil {

	public static int sqr(int a) {
		return a * a;
	}

	public static long sqr(long a) {
		return a * a;
	}

	public static float sqr(float a) {
		return a * a;
	}

	public static double sqr(double a) {
		return a * a;
	}

	public static double sum(Collection<? extends Number> data) {
		double sum = 0;
		for (Number d : data)
			if (d != null)
				sum += d.doubleValue();
		return sum;
	}

	public static int sumInt(Collection<Integer> data) {
		int sum = 0;
		for (Integer d : data)
			if (d != null)
				sum += d.intValue();
		return sum;
	}

	public static int sumInt(int... data) {
		int sum = 0;
		for (int d : data)
			sum += d;
		return sum;
	}

	public static long sumLong(Collection<Long> data) {
		long sum = 0;
		for (Long d : data)
			if (d != null)
				sum += d.longValue();
		return sum;
	}

	public static long sumLong(long... data) {
		long sum = 0;
		for (long d : data)
			sum += d;
		return sum;
	}

	public static float sumFloat(Collection<Float> data) {
		float sum = 0;
		for (Float d : data)
			if (d != null)
				sum += d.floatValue();
		return sum;
	}

	public static float sumFloat(float... data) {
		float sum = 0;
		for (float d : data)
			sum += d;
		return sum;
	}

	public static double sumDouble(Collection<Double> data) {
		double sum = 0;
		for (Double d : data)
			if (d != null)
				sum += d.doubleValue();
		return sum;
	}

	public static double sumDouble(double... data) {
		double sum = 0;
		for (double d : data)
			sum += d;
		return sum;
	}

	public static double average(Collection<? extends Number> data) {
		return sum(data) / data.size();
	}

	public static double variance(Collection<? extends Number> data, boolean isBias) {
		double avg = average(data);
		double v = 0;
		for (Number d : data)
			if (d != null)
				v += sqr(d.doubleValue() - avg);
		return v / (isBias ? data.size() - 1 : data.size());
	}

	public static double stDev(Collection<? extends Number> data, boolean isBias) {
		return Math.sqrt(variance(data, isBias));
	}

	public static double dotProduct(double x1, double y1, double x2, double y2) {
		return x1 * x2 + y1 * y2;
	}

	public static double crossProduct(double x1, double y1, double x2, double y2) {
		return x1 * y2 - x2 * y1;
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	public static double angle(double x1, double y1, double x2, double y2) {
		double cos = (x1 * x2 + y1 * y2) / distance(x1, y1, 0, 0) / distance(x2, y2, 0, 0);
		if (cos < -1) cos = -1;
		if (cos > 1) cos = 1;
		return Math.acos(cos);
	}
}
