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
public abstract class ContinuousSegments<E> implements Serializable, Iterable<ContinuousSegments<E>.Segment> {

	private static final long serialVersionUID = -3006497642478942864L;

	public class Segment {
		protected double left;
		protected double right;
		protected E element;

		public double left() {
			return left;
		}

		public double right() {
			return right;
		}

		public E element() {
			return element;
		}

		public Segment(double left, double right) {
			this.left = left;
			this.right = right;
		}

		public Segment(double left, double right, E element) {
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

	protected double leftLimit = Double.MIN_VALUE;
	protected double rightLimit = Double.MAX_VALUE;
	protected boolean isAutoJoin = false;

	public ContinuousSegments() {

	}

	public ContinuousSegments(double leftLimit, double rightLimit) {
		if (leftLimit > rightLimit)
			throw new IllegalArgumentException("left limit > right limit");

		this.leftLimit = leftLimit;
		this.rightLimit = rightLimit;
	}

	public double leftLimit() {
		return this.leftLimit;
	}

	public double rightLimit() {
		return this.rightLimit;
	}

	public ContinuousSegments<E> clear() {
		segments.clear();
		return this;
	}

	public int size() {
		return segments.size();
	}

	public ContinuousSegments<E> addSegment(double left, double right, E element) {
		if (left >= right)
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
		List<Segment> newSegments = new LinkedList<>();
		for (Segment s1 : segments) {
			if (s0 == null) {
				newSegments.add(s1);
				continue;
			}

			Tuple3<Segment, Segment, Segment> t0 = divideSegment(s0, s1.left, s1.right);
			Tuple3<Segment, Segment, Segment> t1 = divideSegment(s1, s0.left, s0.right);

			Segment ss1 = t0.e1 != null ? t0.e1 : t1.e1;
			Segment ss2 = t0.e2 == null ? t1.e2 : t1.e2 == null ? t0.e2 : mergeSegments(t0.e2, t1.e2);
			Segment ss3 = t0.e3 != null ? t0.e3 : t1.e3;

			if (ss1 != null) {
				newSegments.add(ss1);
			}

			if (ss2 != null) {
				newSegments.add(ss2);
			}

			s0 = ss3;
		}
		if (s0 != null)
			newSegments.add(s0);

		segments = newSegments;

/*		for (int i = 0; true; ) {
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
		}*/
	}

	public ContinuousSegments<E> setAutoJoin(boolean autoJoin) {
		this.isAutoJoin = autoJoin;
		if (autoJoin)
			joinSegments();
		return this;
	}

	public ContinuousSegments<E> joinSegments() {
		List<Segment> newSegments = new LinkedList<>();
		Segment s0 = null;
		for (Segment s1 : segments) {
			if (s0 != null) {
				if (canJoinSegments(s0, s1)) {
					s1 = joinSegments(s0, s1);
				}
				else {
					newSegments.add(s0);
				}
			}

			s0 = s1;
		}
		if (s0 != null)
			newSegments.add(s0);
		segments = newSegments;

/*		Segment s0 = null;
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
		}*/

		return this;
	}

	@Override
	public Iterator<Segment> iterator() {
		return segments.iterator();
	}

	/**
	 * 分割线段
	 * @param segment
	 * @param dividedLine1 不一定在segment.left()和segment.right()之间
	 * @param dividedLine2 不一定在segment.left()和segment.right()之间，保证dividedLine1 <= dividedLine2
	 * @return
	 */
	protected abstract Tuple3<Segment, Segment, Segment> divideSegment(Segment segment, double dividedLine1, double dividedLine2);

	/**
	 * 合并线段
	 * @param segment1
	 * @param segment2 保证segment1.left()==segment2.left() && segment1.right()==segments2.right()
	 * @return
	 */
	protected abstract Segment mergeSegments(Segment segment1, Segment segment2);

	/**
	 * 判断是否可连接
	 * @param segment1
	 * @param segment2 保证segment1.right() <= segments2.left(), 但不保证segment1.right()==segment2.left()
	 * @return
	 */
	protected abstract boolean canJoinSegments(Segment segment1, Segment segment2);

	/**
	 * 连接线段
	 * @param segment1
	 * @param segment2 保证segment1.right() <= segments2.left(), 但不保证segment1.right()==segment2.left()
	 * @return
	 */
	protected abstract Segment joinSegments(Segment segment1, Segment segment2);

}
