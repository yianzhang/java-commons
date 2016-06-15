package me.yczhang.data_structure.topology;

import org.apache.commons.collections.map.HashedMap;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

/**
 * Created by YCZhang on 1/17/16.
 */
public class KeyedTopology<E> extends Topology<String> {

	protected Map<String, E> elements;

	public KeyedTopology() {
		super();
		this.elements = new HashedMap();
	}

	public KeyedTopology<E> addElement(@Nonnull String key, E element) {
		Objects.requireNonNull(key);

		elements.put(key, element);
		super.addElement(key);

		return this;
	}

	@Override
	public KeyedTopology<E> removeElement(@Nonnull String key) {
		Objects.requireNonNull(key);

		elements.remove(key);
		super.removeElement(key);

		return this;
	}

	public E getElement(@Nonnull String key) {
		Objects.requireNonNull(key);

		return elements.get(key);
	}

	@Override
	public Topology<String> addElement(@Nonnull String element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Topology<String> addDirectFollowship(@Nonnull String e1, @Nonnull String e2) {
		Objects.requireNonNull(e1);
		Objects.requireNonNull(e2);

		if (!elements.containsKey(e1)) {
			throw new TopologyException("no element of key " + e1);
		}
		else if (!elements.containsKey(e2)) {
			throw new TopologyException("no element of key " + e2);
		}
		else {
			return super.addDirectFollowship(e1, e2);
		}
	}
}
