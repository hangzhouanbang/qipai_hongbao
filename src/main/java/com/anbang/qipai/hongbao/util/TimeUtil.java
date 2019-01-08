package com.anbang.qipai.hongbao.util;

import java.util.Calendar;

/**
 * 时间类工具
 * 
 * @author lsc
 *
 */
public class TimeUtil {

	/**
	 * 获取某天的开始时间
	 */
	public static long getDayStartTime(long day) {
		long daySecond = 24L * 60 * 60 * 1000;
		return day - (day + 8L * 60 * 60 * 1000) % daySecond;
	}

	/**
	 * 获取某天的结束时间
	 */
	public static long getDayEndTime(long day) {
		long daySecond = 24L * 60 * 60 * 1000;
		return day - (day + 8L * 60 * 60 * 1000) % daySecond + daySecond - 1;
	}

	/**
	 * 获取某天所在周的开始时间
	 */
	public static long getWeekStartTime(long day) {
		Calendar calendar = Calendar.getInstance();
		int d = 0;
		if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
			d = -6;
		} else {
			d = 2 - calendar.get(Calendar.DAY_OF_WEEK);
		}
		calendar.add(Calendar.DAY_OF_WEEK, d);
		long daySecond = 24L * 60 * 60 * 1000;
		long date = calendar.getTimeInMillis();
		return date - (date + 8L * 60 * 60 * 1000) % daySecond;
	}

	/**
	 * 获取某天所在周的结束时间
	 */
	public static long getWeekEndTime(long day) {
		Calendar calendar = Calendar.getInstance();
		int d = 0;
		if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
			d = -6;
		} else {
			d = 2 - calendar.get(Calendar.DAY_OF_WEEK);
		}
		calendar.add(Calendar.DAY_OF_WEEK, d);
		long daySecond = 24L * 60 * 60 * 1000;
		long date = calendar.getTimeInMillis();
		return date - (date + 8L * 60 * 60 * 1000) % daySecond + daySecond - 1;
	}

	/**
	 * 获取某天所在月的开始时间
	 */
	public static long getMonthStartTime(long day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Calendar.MONTH, 1, 0, 0, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取某天所在月的结束时间
	 */
	public static long getMonthEndTime(long day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Calendar.MONTH, 1, 0, 0, 0);
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTimeInMillis() - 1;
	}
}
