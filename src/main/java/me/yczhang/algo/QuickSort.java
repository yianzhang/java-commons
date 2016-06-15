package me.yczhang.algo;

import java.util.Comparator;

/**
 * Created by YCZhang on 6/23/15.
 */
public class QuickSort {

	public static <T> void sort(T[] source, Comparator<T> comparator) {
		_sort(source, comparator, 0, source.length - 1);
	}

	public static <T extends Comparable<T>> void sort(T[] source) {
		_sort(source, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return o1.compareTo(o2);
			}
		}, 0, source.length - 1);
	}

	private static <T> void _sort(T[] source, Comparator<T> comparator, int left, int right) {
		if (left >= right)
			return;

		int l = left;
		int r = right;
		T datum = source[(l + r) >> 1];
		source[(l + r) >> 1] = source[l];

		while (l < r) {
			while (l < r && comparator.compare(datum, source[r]) <= 0)
				--r;
			if (l < r)
				source[l++] = source[r];
			while (l < r && comparator.compare(datum, source[l]) >= 0)
				++l;
			if (l < r)
				source[r--] = source[l];
		}

		source[l] = datum;
		if (left < l - 1) _sort(source, comparator, left, l - 1);
		if (right > r + 1) _sort(source, comparator, r + 1, right);
	}

//	public static void main(String... args) {
//		final int max = 10;
//		Integer[] source = new Integer[max];
//		for (int i = 0; i < max; ++i) {
//			source[i] = (int) (Math.random() * max);
//		}
//
//		sort(source, new Comparator<Integer>() {
//			@Override
//			public int compare(Integer o1, Integer o2) {
//				return o1 - o2;
//			}
//		});
//
//		for (int i = 0; i < max; ++i)
//			System.out.println(source[i]);
//	}
}
