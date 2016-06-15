package me.yczhang.data_structure.heap;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by YCZhang on 4/24/15.
 */
public class Heap<E> {

	private ArrayList<E> heap;
	private Comparator<E> comparator;

	public Heap() {
		this.heap = new ArrayList<E>();
	}

	public Heap(Comparator<E> comparator) {
		this();
		this.comparator = comparator;
	}

	public Heap(int initialCapacity) {
		this.heap = new ArrayList<E>(initialCapacity);
	}

	public Heap(int initialCapacity, Comparator<E> comparator) {
		this(initialCapacity);
		this.comparator = comparator;
	}

	public int size() {
		return heap.size();
	}

	public void clear() {
		this.heap.clear();
	}

	public boolean add(E e) {
		heap.add(e);
		raise(size()-1);

		return true;
	}

	public E first() {
		if (size()==0)
			return null;

		return heap.get(0);
	}

	public E pop() {
		if (size()==0)
			return null;

		E ret = heap.get(0);
		if (size()==1) {
			heap.remove(0);
		} else {
			E e = heap.get(size()-1);
			heap.remove(size()-1);
			heap.set(0, e);

			sink(0);
		}

		return ret;
	}

	private void raise(int k) {
		if (k==0)
			return;

		int k1 = (k-1)>>1;
		if (compare(heap.get(k), heap.get(k1))<0) {
			swap(k, k1);
			raise(k1);
		}
	}

	private void sink(int k) {
		int k1 = (k<<1)+1;
		int k2 = k1+1;
		if (k1>=size())
			return;

		int k0 = k2>=size()?k1:(compare(heap.get(k1), heap.get(k2))<0?k1:k2);
		if (compare(heap.get(k), heap.get(k0))>0) {
			swap(k, k0);
			sink(k0);
		}
	}

	private int compare(E e1, E e2) {
		if (comparator != null)
			return comparator.compare(e1, e2);
		else {
			Comparable<? super E> ce1 = (Comparable<? super E>) e1;
			return ce1.compareTo(e2);
		}
	}

	private void swap(int k1, int k2) {
		E e1 = heap.get(k1);
		E e2 = heap.get(k2);

		heap.set(k2, e1);
		heap.set(k1, e2);
	}

}
