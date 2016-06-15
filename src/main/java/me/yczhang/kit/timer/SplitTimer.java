package me.yczhang.kit.timer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by YCZhang on 9/17/15.
 */
public class SplitTimer {
	public class SubTimer {
		private long beginTime = System.currentTimeMillis();

		private SubTimer(){}

		public void stop() {
			if (beginTime >= 0) {
				long cost = System.currentTimeMillis() - beginTime;
				if (withCount) {
					lock.writeLock().lock();
					try {
						totCost.addAndGet(cost);
						totCount.incrementAndGet();
					}
					finally {
						lock.writeLock().unlock();
					}
				}
				else {
					totCost.addAndGet(cost);
				}
				beginTime = -1;
			}
		}
	}

	private AtomicLong totCost;
	private AtomicInteger totCount;

	private final boolean withCount;
	private ReentrantReadWriteLock lock;

	public SplitTimer() {
		this(false);
	}

	public SplitTimer(boolean withCount) {
		this.withCount = withCount;
		this.totCost = new AtomicLong(0);
		if (this.withCount) {
			this.totCount = new AtomicInteger(0);
			this.lock = new ReentrantReadWriteLock();
		}
	}

	public SubTimer startNew() {
		return new SubTimer();
	}

	public long costWithMillis() {
		return totCost.get();
	}

	public int count() {
		if (withCount) {
			return totCount.get();
		}
		else {
			throw new UnsupportedOperationException("count function not open");
		}
	}

	public double avgCostWithMillis() {
		if (withCount) {
			lock.readLock().lock();
			try {
				return (totCost.get() + 0.0) / totCount.get();
			}
			finally {
				lock.readLock().unlock();
			}
		}
		else {
			throw new UnsupportedOperationException("count function not open");
		}
	}

}
