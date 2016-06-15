package me.yczhang.util;

/**
 * Created by YCZhang on 8/8/15.
 */
public class TimeUtil {

	public static final int MS_PER_SEC = 1000;
	public static final int SEC_PER_MIN = 60;
	public static final int MIN_PER_HOUR = 60;
	public static final int HOUR_PER_DAY = 24;
	public static final int DAY_PER_WEEK = 7;
	public static final int MS_PER_MIN = MS_PER_SEC * SEC_PER_MIN;
	public static final int MS_PER_HOUR = MS_PER_MIN * MIN_PER_HOUR;
	public static final int MS_PER_DAY = MS_PER_HOUR * HOUR_PER_DAY;
	public static final int MS_PER_WEEK = MS_PER_DAY * DAY_PER_WEEK;
	public static final int SEC_PER_HOUR = SEC_PER_MIN * MIN_PER_HOUR;
	public static final int SEC_PER_DAY = SEC_PER_HOUR * HOUR_PER_DAY;
	public static final int SEC_PER_WEEK = SEC_PER_DAY * DAY_PER_WEEK;
	public static final int MIN_PER_DAY = MIN_PER_HOUR * HOUR_PER_DAY;
	public static final int MIN_PER_WEEK = MIN_PER_DAY * DAY_PER_WEEK;
	public static final int HOUR_PER_WEEK = HOUR_PER_DAY * DAY_PER_WEEK;


	public static long msInDay(long time) {
		return (time + 8 * MS_PER_HOUR) % MS_PER_DAY;
	}

}
