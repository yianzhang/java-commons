package me.yczhang.data_structure.tuple;

import me.yczhang.util.ObjUtil;

/**
 * Created by YCZhang on 5/14/15.
 */
public class Tuple3<E1, E2, E3> extends Tuple {

	private static final long serialVersionUID = 3868006157715821052L;

	public E1 e1;
	public E2 e2;
	public E3 e3;

	public Tuple3(E1 e1, E2 e2, E3 e3) {
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Tuple3)) return false;

		Tuple3 tuple3 = (Tuple3) o;

		if (e1 != null ? !e1.equals(tuple3.e1) : tuple3.e1 != null) return false;
		if (e2 != null ? !e2.equals(tuple3.e2) : tuple3.e2 != null) return false;
		if (e3 != null ? !e3.equals(tuple3.e3) : tuple3.e3 != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = e1 != null ? e1.hashCode() : 0;
		result = 31 * result + (e2 != null ? e2.hashCode() : 0);
		result = 31 * result + (e3 != null ? e3.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "(" + e1 + ", " + e2 + ", " + e3 + ")";
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Tuple3) {
			try {
				Tuple3<? super E1, ? super E2, ? super E3> that = (Tuple3<? super E1, ? super E2, ? super E3>) o;
				int temp;
				return  (temp = ObjUtil.compare(this.e1, that.e1)) != 0 ? temp :
						(temp = ObjUtil.compare(this.e2, that.e2)) != 0 ? temp :
						ObjUtil.compare(this.e3, that.e3);
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
