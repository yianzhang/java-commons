package me.yczhang.data_structure.range;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by YCZhang on 12/4/15.
 */
public class LongRange extends Range implements Iterator<Long>, Iterable<Long> {

	protected long from;
	protected long util;
	protected long skip;

	protected long next;

	protected LongRange(long from, long util) {
		this(from, util, from > util ? -1 : 1);
	}

	protected LongRange(long from, long util, long skip) {
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
	public Long next() {
		if (hasNext()) {
			long ret = next;
			next += skip;
			return ret;
		}
		else {
			throw new OutOfRangeException(from, util, next);
		}
	}

	@Override
	public Iterator<Long> iterator() {
		return this;
	}

	public Stream<Long> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
}
