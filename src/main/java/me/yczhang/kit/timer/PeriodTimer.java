package me.yczhang.kit.timer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by YCZhang on 10/12/15.
 */
public class PeriodTimer {
	public class SubTimer {
		private long beginTime = System.currentTimeMillis();

		private SubTimer(){}

		public void stop() {
			if (beginTime >= 0) {
				long cost = System.currentTimeMillis() - beginTime;

				if (withCount) {
					totalLock.writeLock().lock();
					try {
						totalCost.addAndGet(cost);
						totalCount.incrementAndGet();
					}
					finally {
						totalLock.writeLock().unlock();
					}
					periodLock.writeLock().lock();
					try {
						periodCost.addAndGet(cost);
						periodCount.incrementAndGet();
					} finally {
						periodLock.writeLock().unlock();
					}
				}
				else {
					totalCost.addAndGet(cost);
					periodCost.addAndGet(cost);
				}

				beginTime = -1;
			}
		}
	}

	private AtomicLong totalCost;
	private AtomicInteger totalCount;
	private AtomicLong periodCost;
	private AtomicInteger periodCount;

	private final boolean withCount;
	private ReentrantReadWriteLock totalLock;
	private ReentrantReadWriteLock periodLock;

	public PeriodTimer() {
		this(true);
	}

	public PeriodTimer(boolean withCount) {
		this.withCount = withCount;
		this.totalCost = new AtomicLong(0);
		this.periodCost = new AtomicLong(0);
		if (this.withCount) {
			this.totalCount = new AtomicInteger(0);
			this.periodCount = new AtomicInteger(0);
			this.totalLock = new ReentrantReadWriteLock();
			this.periodLock = new ReentrantReadWriteLock();
		}
	}

	public SubTimer startNew() {
		return new SubTimer();
	}

	public PeriodTimer newPeriod() {
		if (withCount) {
			periodLock.writeLock().lock();
			try {
				periodCost.set(0);
				periodCount.set(0);
			} finally {
				periodLock.writeLock().unlock();
			}
		}
		else {
			periodCost.set(0);
		}

		return this;
	}

	public long totalCostWithMillis() {
		return totalCost.get();
	}

	public int totalCount() {
		if (withCount) {
			return totalCount.get();
		}
		else {
			throw new UnsupportedOperationException("count function not open");
		}
	}

	public double totalAvgCostWithMillis() {
		if (withCount) {
			totalLock.readLock().lock();
			try {
				return totalCost.get() / (totalCount.get() + 0.0);
			}
			finally {
				totalLock.readLock().unlock();
			}
		}
		else {
			throw new UnsupportedOperationException("count function not open");
		}
	}

	public long periodCostWithMillis() {
		return periodCost.get();
	}

	public int periodCount() {
		if (withCount) {
			return periodCount.get();
		}
		else {
			throw new UnsupportedOperationException("count function not open");
		}
	}

	public double periodAvgCostWithMillis() {
		if (withCount) {
			periodLock.readLock().lock();
			try {
				return periodCost.get() / (periodCount.get() + 0.0);
			}
			finally {
				periodLock.readLock().unlock();
			}
		}
		else {
			throw new UnsupportedOperationException("count function not open");
		}
	}

}
