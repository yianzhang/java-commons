package me.yczhang.data_structure.range;

/**
 * Created by YCZhang on 12/4/15.
 */
public abstract class Range {

	public static IntRange to(int to) {
		return to(0, to);
	}

	public static IntRange to(int from, int to) {
		return new IntRange(from, from > to ? to - 1 : to + 1);
	}

	public static IntRange to(int from, int to, int skip) {
		return new IntRange(from, from > to ? to - 1 : to + 1, skip);
	}

	public static IntRange util(int util) {
		return util(0, util);
	}

	public static IntRange util(int from, int util) {
		return new IntRange(from, util);
	}

	public static IntRange util(int from, int util, int skip) {
		return new IntRange(from, util, skip);
	}

	public static LongRange to(long to) {
		return to(0, to);
	}

	public static LongRange to(long from, long to) {
		return new LongRange(from, from > to ? to - 1 : to + 1);
	}

	public static LongRange to(long from, long to, long skip) {
		return new LongRange(from, from > to ? to - 1 : to + 1, skip);
	}

	public static LongRange util(long util) {
		return util(0, util);
	}

	public static LongRange util(long from, long util) {
		return new LongRange(from, util);
	}

	public static LongRange util(long from, long util, long skip) {
		return new LongRange(from, util, skip);
	}

}
