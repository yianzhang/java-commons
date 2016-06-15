package me.yczhang.util;

/**
 * Created by YCZhang on 1/27/16.
 */
public class CompareUtil {

	/**
	 * 比较两个对象
	 * @param t1
	 * @param t2
	 * @param <T>
	 * @return -1: t1 < t2; 0: t1 = t2; 1: t1 > t2
	 */
	public static <T> int compare(T t1, T t2) {
		return compare(t1, t2, false);
	}

	/**
	 * 比较两个对象
	 * @param t1
	 * @param t2
	 * @param nullGreater 是否null是较大值
	 * @param <T>
	 * @return -1: t1 < t2; 0: t1 = t2; 1: t1 > t2
	 */
	public static <T> int compare(T t1, T t2, boolean nullGreater) {
		if (t1 == t2)
			return 0;
		else if (t1 == null)
			return nullGreater ? 1 : -1;
		else if (t2 == null)
			return nullGreater ? -1 : 1;
		else if (t1 instanceof Comparable) {
			Comparable<? super T> ct1 = (Comparable<? super T>) t1;
			return ct1.compareTo(t2);
		}
		else {
			return t1.toString().compareTo(t2.toString());
		}
	}

	/**
	 * 比较链
	 */
	public static class CompareChain {
		int result = 0;

		/**
		 * 同于CompareUtil.compare(t1, t2)
		 * @param t1
		 * @param t2
		 * @param <T>
		 * @return self
		 */
		public <T> CompareChain compare(T t1, T t2) {
			this.compare(t1, t2, false);
			return this;
		}

		/**
		 * 同于CompareUtil.compare(t1, t2, nullGreater)
		 * @param t1
		 * @param t2
		 * @param nullGreater
		 * @param <T>
		 * @return self
		 */
		public <T> CompareChain compare(T t1, T t2, boolean nullGreater) {
			if (this.result == 0) {
				this.result = CompareUtil.compare(t1, t2, nullGreater);
			}

			return this;
		}

		/**
		 * 返回结果
		 * @return -1: t1 < t2; 0: t1 = t2; 1: t1 > t2
		 */
		public int result() {
			return this.result;
		}

		/**
		 * 重置
		 * @return self
		 */
		public CompareChain reset() {
			this.result = 0;
			return this;
		}
	}

	/**
	 * 返回一个比较链对象
	 * @return
	 */
	public static CompareChain chain() {
		return new CompareChain();
	}
}
