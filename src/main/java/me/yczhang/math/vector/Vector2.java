package me.yczhang.math.vector;

import java.io.Serializable;

/**
 * Created by YCZhang on 10/28/15.
 */
public class Vector2 implements Serializable {

	private static final long serialVersionUID = 2834615069198809592L;

	protected double x = 0;
	protected double y = 0;

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public Vector2 x(double x) {
		this.x = x;
		return this;
	}

	public Vector2 y(double y) {
		this.y = y;
		return this;
	}

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2() {}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	public static Vector2 of(double x, double y) {
		return new Vector2(x, y);
	}

	public static double dotProduct(Vector2 v1, Vector2 v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}

	public static double crossProduct(Vector2 v1, Vector2 v2) {
		return v1.x * v2.y - v1.y * v2.x;
	}

	public static double cosAngle(Vector2 v1, Vector2 v2) {
		return dotProduct(v1, v2) / v1.length() / v2.length();
	}

	public static double sinAngle(Vector2 v1, Vector2 v2) {
		return crossProduct(v1, v2) / v1.length() / v2.length();
	}
}
