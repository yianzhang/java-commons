package me.yczhang.data_structure.tuple;

import java.io.Serializable;

/**
 * Created by YCZhang on 9/14/15.
 */
public abstract class Tuple implements Serializable, Comparable {
	@Override
	public int compareTo(Object o) {
		return this.toString().compareTo(o.toString());
	}

	public static <E1> Tuple1<E1> of(E1 e1) {
		return new Tuple1<>(e1);
	}

	public static <E1, E2> Tuple2<E1, E2> of(E1 e1, E2 e2) {
		return new Tuple2<>(e1, e2);
	}

	public static <E1, E2, E3> Tuple3<E1, E2, E3> of(E1 e1, E2 e2, E3 e3) {
		return new Tuple3<>(e1, e2, e3);
	}

	public static <E1, E2, E3, E4> Tuple4<E1, E2, E3, E4> of(E1 e1, E2 e2, E3 e3, E4 e4) {
		return new Tuple4<>(e1, e2, e3, e4);
	}

	public static <E1, E2, E3, E4, E5> Tuple5<E1, E2, E3, E4, E5> of (E1 e1, E2 e2, E3 e3, E4 e4, E5 e5) {
		return new Tuple5<>(e1, e2, e3, e4, e5);
	}

	public static <E1, E2, E3, E4, E5, E6> Tuple6<E1, E2, E3, E4, E5, E6> of (E1 e1, E2 e2, E3 e3, E4 e4, E5 e5, E6 e6) {
		return new Tuple6<>(e1, e2, e3, e4,e5, e6);
	}

	public static <E1, E2, E3, E4, E5, E6, E7> Tuple7<E1, E2, E3, E4, E5, E6, E7> of (E1 e1, E2 e2, E3 e3, E4 e4, E5 e5, E6 e6, E7 e7) {
		return new Tuple7<>(e1, e2, e3, e4,e5, e6, e7);
	}

	public static <E1, E2, E3, E4, E5, E6, E7, E8> Tuple8<E1, E2, E3, E4, E5, E6, E7, E8> of (E1 e1, E2 e2, E3 e3, E4 e4, E5 e5, E6 e6, E7 e7, E8 e8) {
		return new Tuple8<>(e1, e2, e3, e4,e5, e6, e7, e8);
	}

	public static <E1, E2, E3, E4, E5, E6, E7, E8, E9> Tuple9<E1, E2, E3, E4, E5, E6, E7, E8, E9> of (E1 e1, E2 e2, E3 e3, E4 e4, E5 e5, E6 e6, E7 e7, E8 e8, E9 e9) {
		return new Tuple9<>(e1, e2, e3, e4,e5, e6, e7, e8, e9);
	}
}
