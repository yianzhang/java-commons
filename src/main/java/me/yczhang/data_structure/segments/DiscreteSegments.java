package me.yczhang.data_structure.segments;

import me.yczhang.data_structure.tuple.Tuple3;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YCZhang on 11/30/15.
 */
@NotThreadSafe
public abstract class DiscreteSegments<E> implements Serializable, Iterable<DiscreteSegments<E>.Segment> {

	private static final long serialVersionUID = -843337636987074021L;

	public class Segment {
		protected long left;
		protected long right;
		protected E element;

		public long left() {
			return left;
		}

		public long right() {
			return right;
		}

		public E element() {
			return element;
		}

		public Segment(long left, long right) {
			this.left = left;
			this.right = right;
		}

		public Segment(long left, long right, E element) {
			this.left = left;
			this.right = right;
			this.element = element;
		}

		public void copyFrom(Segment segment) {
			this.left = segment.left;
			this.right = segment.right;
			this.element = segment.element;
		}

		@Override
		public String toString() {
			return left + "," + right + "," + element.toString();
		}
	}

	protected List<Segment> segments = new LinkedList<>();

	protected long leftLimit = Long.MIN_VALUE;
	protected long rightLimit = Long.MAX_VALUE;
	protected boolean isAutoJoin = false;

	public DiscreteSegments() {

	}

	public DiscreteSegments(long leftLimit, long rightLimit) {
		if (leftLimit > rightLimit)
			throw new IllegalArgumentException("left limit > right limit");

		this.leftLimit = leftLimit;
		this.rightLimit = rightLimit;
	}

	public long leftLimit() {
		return leftLimit;
	}

	public long rightLimit() {
		return rightLimit;
	}

	public DiscreteSegments<E> clear() {
		segments.clear();
		return this;
	}

	public int size() {
		return segments.size();
	}

	public DiscreteSegments<E> addSegment(long left, long right, E element) {
		if (left > right)
			return this;

		Segment s0 = new Segment(left, right, element);
		Tuple3<Segment, Segment, Segment> t = divideSegment(s0, leftLimit, rightLimit);
		s0 = t.e2;

		if (s0 == null)
			return this;

		_addSegment(s0);

		if (isAutoJoin) {
			joinSegments();
		}

		return this;
	}

	protected void _addSegment(Segment s0) {
		for (int i = 0; true; ) {
			if (i >= segments.size()) {
				segments.add(s0);
				break;
			}

			Segment s1 = segments.get(i);
			Tuple3<Segment, Segment, Segment> t0 = divideSegment(s0, s1.left, s1.right);
			Tuple3<Segment, Segment, Segment> t1 = divideSegment(s1, s0.left, s0.right);

			Segment ss1 = t0.e1 != null ? t0.e1 : t1.e1;
			Segment ss2 = t0.e2 == null ? t1.e2 : t1.e2 == null ? t0.e2 : mergeSegments(t0.e2, t1.e2);
			Segment ss3 = t0.e3 != null ? t0.e3 : t1.e3;

			if (ss1 != null) {
				segments.set(i, ss1);
				i += 1;
			}

			if (ss2 != null) {
				if (ss1 != null)
					segments.add(i, ss2);
				else
					segments.set(i, ss2);
				i += 1;
			}

			if (ss3 != null)
				s0 = ss3;
			else
				break;
		}
	}

	public DiscreteSegments<E> setAutoJoin(boolean autoJoin) {
		this.isAutoJoin = autoJoin;
		if (autoJoin)
			joinSegments();
		return this;
	}

	public DiscreteSegments<E> joinSegments() {
		Segment s0 = null;
		for (int i = 0; true;) {
			if (i >= segments.size())
				break;

			Segment s1 = segments.get(i);
			if (s0 == null) {
				s0 = s1;
				i += 1;
			}
			else {
				if (canJoinSegments(s0, s1)) {
					s0.copyFrom(joinSegments(s0, s1));
					segments.remove(i);
				}
				else {
					s0 = s1;
					i += 1;
				}
			}
		}

		return this;
	}

	@Override
	public Iterator<Segment> iterator() {
		return segments.iterator();
	}

	protected abstract Tuple3<Segment, Segment, Segment> divideSegment(Segment segment, long dividedLine1, long dividedLine2);
	protected abstract Segment mergeSegments(Segment segment1, Segment segment2);
	protected abstract boolean canJoinSegments(Segment segment1, Segment segment2);
	protected abstract Segment joinSegments(Segment segment1, Segment segment2);

}
