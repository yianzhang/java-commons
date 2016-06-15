package me.yczhang.data_structure.tuple;

import me.yczhang.util.ObjUtil;

/**
 * Created by YCZhang on 8/31/15.
 */
public class Tuple4<E1, E2, E3, E4> extends Tuple {

	private static final long serialVersionUID = -7608038336538976872L;

	public E1 e1;
	public E2 e2;
	public E3 e3;
	public E4 e4;

	public Tuple4(E1 e1, E2 e2, E3 e3, E4 e4) {
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Tuple4)) return false;

		Tuple4 tuple4 = (Tuple4) o;

		if (e1 != null ? !e1.equals(tuple4.e1) : tuple4.e1 != null) return false;
		if (e2 != null ? !e2.equals(tuple4.e2) : tuple4.e2 != null) return false;
		if (e3 != null ? !e3.equals(tuple4.e3) : tuple4.e3 != null) return false;
		if (e4 != null ? !e4.equals(tuple4.e4) : tuple4.e4 != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = e1 != null ? e1.hashCode() : 0;
		result = 31 * result + (e2 != null ? e2.hashCode() : 0);
		result = 31 * result + (e3 != null ? e3.hashCode() : 0);
		result = 31 * result + (e4 != null ? e4.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "(" + e1 + ", " + e2 + ", " + e3 + ", " + e4 + ")";
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Tuple4) {
			try {
				Tuple4<? super E1, ? super E2, ? super E3, ? super E4> that = (Tuple4<? super E1, ? super E2, ? super E3, ? super E4>) o;
				int temp;
				return  (temp = ObjUtil.compare(this.e1, that.e1)) != 0 ? temp :
						(temp = ObjUtil.compare(this.e2, that.e2)) != 0 ? temp :
								(temp = ObjUtil.compare(this.e3, that.e3)) != 0 ? temp :
								ObjUtil.compare(this.e4, that.e4);
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
