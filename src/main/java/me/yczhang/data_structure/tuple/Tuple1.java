package me.yczhang.data_structure.tuple;

import me.yczhang.util.ObjUtil;

/**
 * Created by YCZhang on 5/17/15.
 */
public class Tuple1<E1> extends Tuple {

	private static final long serialVersionUID = -301087720676636233L;

	public E1 e1;

	public Tuple1(E1 e1) {
		this.e1 = e1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Tuple1)) return false;

		Tuple1 tuple1 = (Tuple1) o;

		if (e1 != null ? !e1.equals(tuple1.e1) : tuple1.e1 != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return e1 != null ? e1.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "(" + e1 + ")";
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Tuple1) {
			try {
				Tuple1<? super E1> that = (Tuple1<? super E1>) o;
				return ObjUtil.compare(this.e1, that.e1);
			}
			catch (ClassCastException e) {
				return super.compareTo(o);
			}
		}
		else {
			return super.compareTo(o);
		}
	}
}
