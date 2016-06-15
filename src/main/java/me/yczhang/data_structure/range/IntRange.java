package me.yczhang.data_structure.range;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by YCZhang on 12/4/15.
 */
public class IntRange extends Range implements Iterator<Integer>, Iterable<Integer> {

	protected int from;
	protected int util;
	protected int skip;

	protected int next;

	protected IntRange(int from, int util) {
		this(from, util, from > util ? -1 : 1);
	}

	protected IntRange(int from, int util, int skip) {
		if (skip == 0)
			throw new IllegalArgumentException("skip == 0");
		if (from < util && skip < 0)
			throw new IllegalArgumentException("from < util but skip < 0");
		if (from > util && skip > 0)
			throw new IllegalArgumentException("from > util but skip > 0");

		this.from = this.next = from;
		this.util = util;
		this.skip = skip;
	}

	@Override
	public boolean hasNext() {
		return from > util ? next > util : next < util;
	}

	@Override
	public Integer next() {
		if (hasNext()) {
			int ret = next;
			next += skip;
			return ret;
		}
		else {
			throw new OutOfRangeException(from, util, next);
		}
	}

	@Override
	public Iterator<Integer> iterator() {
		return this;
	}

	public Stream<Integer> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
}
