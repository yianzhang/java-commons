package me.yczhang.data_structure.topology;

import me.yczhang.data_structure.tuple.Tuple;
import me.yczhang.data_structure.tuple.Tuple2;
import me.yczhang.util.ObjUtil;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by YCZhang on 1/15/16.
 * 拓扑结构，thread unsafe
 */
public class Topology<E> {

	/**
	 * item和direct followers
	 */
	protected Map<E, Set<E>> itemAndFollowers;
	/**
	 * 跟随关系，直接和间接都算
	 */
	protected Map<Tuple2<E, E>, Boolean> followships;
	/**
	 * 拓扑序
	 */
	protected List<E> topoList;

	public Topology() {
		this.itemAndFollowers = new HashMap<>();
		this.followships = new HashMap<>();
	}

	/**
	 * 新增item
	 * @param element
	 * @return self
	 */
	public Topology<E> addElement(@Nonnull E element) {
		Objects.requireNonNull(element);

		if (!itemAndFollowers.containsKey(element)) {
			itemAndFollowers.put(element, new HashSet<E>());
			topoList = null;
		}

		return this;
	}

	/**
	 * 删除item
	 * @param element
	 * @return self
	 */
	public Topology<E> removeElement(@Nonnull E element) {
		if (itemAndFollowers.containsKey(element)) {
			itemAndFollowers.remove(element);
			calcFollowships();
		}

		return this;
	}

	/**
	 * 是否包含item
	 * @param element
	 * @return
	 */
	public boolean containsElement(@Nonnull E element) {
		Objects.requireNonNull(element);

		return itemAndFollowers.containsKey(element);
	}

	/**
	 * 获取direct followers
	 * @param element
	 * @return
	 */
	public Set<E> getDirectFollowers(@Nonnull E element) {
		Objects.requireNonNull(element);

		return itemAndFollowers.containsKey(element) ? Collections.unmodifiableSet(itemAndFollowers.get(element)) : null;
	}

	/**
	 * 新增一个direct followship, e2 follow e1 directly
	 * @param e1
	 * @param e2
	 * @return self
	 */
	public Topology<E> addDirectFollowship(@Nonnull E e1, @Nonnull E e2) {
		Objects.requireNonNull(e1);
		Objects.requireNonNull(e2);

		if (e1 == e2) {
			// do nothing
		}
		else if (ObjUtil.isNull(followships.get(Tuple.of(e2, e1)), false)) {
			throw new TopologyException("the followship (e1 -> e2) exits");
		}
		else {
			if (!itemAndFollowers.containsKey(e1))
				itemAndFollowers.put(e1, new HashSet<E>());
			itemAndFollowers.get(e1).add(e2);
			if (!itemAndFollowers.containsKey(e2))
				itemAndFollowers.put(e2, new HashSet<E>());
			calcFollowships();
		}
		return this;
	}

	/**
	 * 移除一个direct followship, e2 follow e1 directly
	 * @param e1
	 * @param e2
	 * @return self
	 */
	public Topology<E> removeDirectFollowship(@Nonnull E e1, @Nonnull E e2) {
		Objects.requireNonNull(e1);
		Objects.requireNonNull(e2);

		if (e1 == e2) {
			// do nothing
		}
		else if (!itemAndFollowers.containsKey(e1) || !itemAndFollowers.get(e1).contains(e2)) {
			// do nothing
		}
		else {
			itemAndFollowers.get(e1).remove(e2);
			calcFollowships();
		}
		return this;
	}

	/**
	 * 是否存在direct followship, e2 follow e1 directly
	 * @param e1
	 * @param e2
	 * @return
	 */
	public boolean containsDirectFollowship(@Nonnull E e1, @Nonnull E e2) {
		Objects.requireNonNull(e1);
		Objects.requireNonNull(e2);

		return itemAndFollowers.containsKey(e1) && itemAndFollowers.get(e2).contains(e2);
	}

	/**
	 * 是否存在followship, e2 follow e1
	 * @param e1
	 * @param e2
	 * @return
	 */
	public boolean containsFollowship(@Nonnull E e1, @Nonnull E e2) {
		Objects.requireNonNull(e1);
		Objects.requireNonNull(e2);

		return followships.containsKey(Tuple.of(e1, e2));
	}


	/**
	 * 计算followships，floyed算法，时效O(n^3)
	 */
	protected void calcFollowships() {
		topoList = null;
		followships.clear();
		Set<E> items = itemAndFollowers.keySet();

		for (Map.Entry<E, Set<E>> entry : itemAndFollowers.entrySet()) {
			E i = entry.getKey();
			for (E j : entry.getValue()) {
				followships.put(Tuple.of(i, j), true);
			}
		}

		for (E k : items) {
			for (E i : items) {
				if (k != i) {
					for (E j : items) {
						if (j != k && j != i) {
							boolean isFollower = ObjUtil.isNull(followships.get(Tuple.of(i, j)), false) ||
									ObjUtil.isNull(followships.get(Tuple2.of(i, k)), false) && ObjUtil.isNull(followships.get(Tuple2.of(k, j)), false);
							if (isFollower)
								followships.put(Tuple2.of(i, j), isFollower);
						}
					}
				}
			}
		}
	}

	/**
	 * 获取拓扑序
	 * @return
	 */
	public List<E> getTopologyList() {
		if (topoList == null) {
			Map<E, Integer> inDegrees = new HashMap();
			for (Map.Entry<E, Set<E>> entry : itemAndFollowers.entrySet()) {
				for (E follower : entry.getValue()) {
					if (!inDegrees.containsKey(follower))
						inDegrees.put(follower, 1);
					else
						inDegrees.put(follower, inDegrees.get(follower) + 1);
				}
			}
			Queue<E> inDegrees0 = new LinkedList<>(itemAndFollowers.keySet().stream().filter(x -> !inDegrees.containsKey(x)).collect(Collectors.toList()));

			List<E> list = new LinkedList<>();
			while (inDegrees0.size() > 0) {
				E item = inDegrees0.poll();
				list.add(item);
				for (E follower : itemAndFollowers.get(item)) {
					int inDegree = inDegrees.get(follower);
					if (inDegree > 1) {
						inDegrees.put(follower, inDegree - 1);
					}
					else {
						inDegrees.remove(follower);
						inDegrees0.offer(follower);
					}
				}
			}

			topoList = Collections.unmodifiableList(list);
		}

		return topoList;
	}

}
