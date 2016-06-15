package me.yczhang.algo;

import java.util.Comparator;

/**
 * Created by YCZhang on 6/23/15.
 */
public class SelectionSort {

	public static <T> void sort(T[] source, Comparator<T> comparator) {
		for (int i = 0; i < source.length; ++i) {
			int k = i;
			for (int j = i + 1; j < source.length; ++j) {
				if (comparator.compare(source[k], source[j]) > 0)
					k = j;
			}

			if (k != i) {
				T tmp = source[i];
				source[i] = source[k];
				source[k] = tmp;
			}
		}
	}

	public static <T extends Comparable<T>> void sort(T[] source) {
		sort(source, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				return o1.compareTo(o2);
			}
		});
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
//				return o2 - o1;
//			}
//		});
//
//		for (int i = 0; i < max; ++i)
//			System.out.println(source[i]);
//	}
}
