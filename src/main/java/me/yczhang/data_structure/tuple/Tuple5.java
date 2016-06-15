package me.yczhang.data_structure.tuple;

import me.yczhang.util.ObjUtil;

/**
 * Created by YCZhang on 12/4/15.
 */
public class Tuple5<E1, E2, E3, E4, E5> extends Tuple {

	public E1 e1;
	public E2 e2;
	public E3 e3;
	public E4 e4;
	public E5 e5;

	public Tuple5(E1 e1, E2 e2, E3 e3, E4 e4, E5 e5) {
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
		this.e5 = e5;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Tuple5)) return false;

		Tuple5 tuple5 = (Tuple5) o;

		if (e1 != null ? !e1.equals(tuple5.e1) : tuple5.e1 != null) return false;
		if (e2 != null ? !e2.equals(tuple5.e2) : tuple5.e2 != null) return false;
		if (e3 != null ? !e3.equals(tuple5.e3) : tuple5.e3 != null) return false;
		if (e4 != null ? !e4.equals(tuple5.e4) : tuple5.e4 != null) return false;
		if (e5 != null ? !e5.equals(tuple5.e5) : tuple5.e5 != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = e1 != null ? e1.hashCode() : 0;
		result = 31 * result + (e2 != null ? e2.hashCode() : 0);
		result = 31 * result + (e3 != null ? e3.hashCode() : 0);
		result = 31 * result + (e4 != null ? e4.hashCode() : 0);
		result = 31 * result + (e5 != null ? e5.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "(" + e1 + ", " + e2 + ", " + e3 + ", " + e4 + ", " + e5 + ")";
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Tuple5) {
			try {
				Tuple5<? super E1, ? super E2, ? super E3, ? super E4, ? super E5> that = (Tuple5<? super E1, ? super E2, ? super E3, ? super E4, ? super E5>) o;
				int temp;
				return  (temp = ObjUtil.compare(this.e1, that.e1)) != 0 ? temp :
						(temp = ObjUtil.compare(this.e2, that.e2)) != 0 ? temp :
								(temp = ObjUtil.compare(this.e3, that.e3)) != 0 ? temp :
										(temp = ObjUtil.compare(this.e4, that.e4)) !=0 ? temp :
										ObjUtil.compare(this.e5, that.e5);
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
