package me.yczhang.kit.timer;

/**
 * Created by YCZhang on 9/29/15.
 */
public class OnceTimer {

	protected long startTime = 0;
	protected long stopTime = 0;

	public OnceTimer start() {
		if (startTime <= 0)
			startTime = System.currentTimeMillis();
		return this;
	}

	public OnceTimer stop() {
		if (startTime > 0 && stopTime <= 0)
			stopTime = System.currentTimeMillis();
		return this;
	}

	public long costWithMillis() {
		if (startTime > 0) {
			return stopTime > 0 ? stopTime - startTime : System.currentTimeMillis() - startTime;
		}
		else {
			return 0;
		}
	}

	public long startTimeWithMillis() {
		return startTime;
	}

	public long stopTimeWithMillis() {
		return stopTime;
	}

	public boolean isStarted() {
		return startTime > 0;
	}

	public boolean isStopped() {
		return stopTime > 0;
	}
}
