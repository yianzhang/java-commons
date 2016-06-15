package me.yczhang.data_structure.tuple;

import me.yczhang.util.ObjUtil;

/**
 * Created by YCZhang on 12/4/15.
 */
public class Tuple9<E1, E2, E3, E4, E5, E6, E7, E8, E9> extends Tuple {

	public E1 e1;
	public E2 e2;
	public E3 e3;
	public E4 e4;
	public E5 e5;
	public E6 e6;
	public E7 e7;
	public E8 e8;
	public E9 e9;

	public Tuple9(E1 e1, E2 e2, E3 e3, E4 e4, E5 e5, E6 e6, E7 e7, E8 e8, E9 e9) {
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		this.e4 = e4;
		this.e5 = e5;
		this.e6 = e6;
		this.e7 = e7;
		this.e8 = e8;
		this.e9 = e9;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Tuple9)) return false;

		Tuple9 tuple9 = (Tuple9) o;

		if (e1 != null ? !e1.equals(tuple9.e1) : tuple9.e1 != null) return false;
		if (e2 != null ? !e2.equals(tuple9.e2) : tuple9.e2 != null) return false;
		if (e3 != null ? !e3.equals(tuple9.e3) : tuple9.e3 != null) return false;
		if (e4 != null ? !e4.equals(tuple9.e4) : tuple9.e4 != null) return false;
		if (e5 != null ? !e5.equals(tuple9.e5) : tuple9.e5 != null) return false;
		if (e6 != null ? !e6.equals(tuple9.e6) : tuple9.e6 != null) return false;
		if (e7 != null ? !e7.equals(tuple9.e7) : tuple9.e7 != null) return false;
		if (e8 != null ? !e8.equals(tuple9.e8) : tuple9.e8 != null) return false;
		if (e9 != null ? !e9.equals(tuple9.e9) : tuple9.e9 != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = e1 != null ? e1.hashCode() : 0;
		result = 31 * result + (e2 != null ? e2.hashCode() : 0);
		result = 31 * result + (e3 != null ? e3.hashCode() : 0);
		result = 31 * result + (e4 != null ? e4.hashCode() : 0);
		result = 31 * result + (e5 != null ? e5.hashCode() : 0);
		result = 31 * result + (e6 != null ? e6.hashCode() : 0);
		result = 31 * result + (e7 != null ? e7.hashCode() : 0);
		result = 31 * result + (e8 != null ? e8.hashCode() : 0);
		result = 31 * result + (e9 != null ? e9.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "(" + e1 + ", " + e2 + ", " + e3 + ", " + e4 + ", " + e5 + ", " + e6 + ", " + e7 + ", " + e8 + ", " + e9 + ")";
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Tuple9) {
			try {
				Tuple9<? super E1, ? super E2, ? super E3, ? super E4, ? super E5, ? super E6, ? super E7, ? super E8, ? super E9> that = (Tuple9<? super E1, ? super E2, ? super E3, ? super E4, ? super E5, ? super E6, ? super E7, ? super E8, ? super E9>) o;
				int temp;
				return  (temp = ObjUtil.compare(this.e1, that.e1)) != 0 ? temp :
						(temp = ObjUtil.compare(this.e2, that.e2)) != 0 ? temp :
								(temp = ObjUtil.compare(this.e3, that.e3)) != 0 ? temp :
										(temp = ObjUtil.compare(this.e4, that.e4)) !=0 ? temp :
												(temp = ObjUtil.compare(this.e5, that.e5)) != 0 ? temp :
														(temp = ObjUtil.compare(this.e6, that.e6)) != 0 ? temp :
																(temp = ObjUtil.compare(this.e7, that.e7)) != 0 ? temp :
																		(temp = ObjUtil.compare(this.e8, that.e8)) != 0 ? temp :
																		ObjUtil.compare(this.e9, that.e9);
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
