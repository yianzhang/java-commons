package me.yczhang.data_structure.tuple;

import me.yczhang.util.ObjUtil;

/**
 * Created by YCZhang on 5/14/15.
 */
public class Tuple2<E1, E2> extends Tuple {

	private static final long serialVersionUID = -6223636844178491478L;

	public E1 e1;
	public E2 e2;

	public Tuple2(E1 e1, E2 e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Tuple2)) return false;

		Tuple2 tuple2 = (Tuple2) o;

		if (e1 != null ? !e1.equals(tuple2.e1) : tuple2.e1 != null) return false;
		if (e2 != null ? !e2.equals(tuple2.e2) : tuple2.e2 != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = e1 != null ? e1.hashCode() : 0;
		result = 31 * result + (e2 != null ? e2.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "(" + e1 + ", " + e2 + ")";
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Tuple2) {
			try {
				Tuple2<? super E1, ? super E2> that = (Tuple2<? super E1, ? super E2>) o;
				int temp;
				return  (temp = ObjUtil.compare(this.e1, that.e1)) != 0 ? temp :
						ObjUtil.compare(this.e2, that.e2);
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
