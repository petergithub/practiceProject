package org.pu.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DateUtil contains common helper methods for working with dates.
 * 
 * <pre>
 * format: 
 * yyyy-MM-dd
 * yyyy-MM-dd hh:mm:ss - 12 hour
 * yyyy-MM-dd kk:mm:ss - 24 hour Hour in day (1-24)
 * yyyy-MM-dd HH:mm:ss - 24 hour Hour in day (0-23)
 * MM/dd/yyyy HH:mm:ss a - AM/PM
 * yyyy年MM月dd日HH点mm分ss秒
 * refer to org.apache.commons.lang.time.DateFormatUtils
 * </pre>
 * 
 * @author Shang Pu
 */
public class DateUtils {
	private static final Logger log = LoggerFactory.getLogger(DateUtils.class);

	@Test
	public void testDateFormat() {
		Date date = new Date();
		// 用来显示某一个时间的中文格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
		String dateChinese = df.format(date);
		log.info("dateChinese = {}", dateChinese);
		// 用来显示某一个时间的其它国家格式
		DateFormat fmt = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("en", "US"));
		String dateLocale = fmt.format(date);
		log.info("dateLocale = {}", dateLocale);

		log.info("getLocaleDate() = {}", getLocaleDate());

		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
		String beginDatetime = df2.format(date);
		log.info("Date beginDatetime = {}", beginDatetime);

		Date datetime = Calendar.getInstance().getTime();
		log.info("Calendar beginDatetime = {}", df2.format(datetime));
	}

	/**
	 * @return DateFormat.getDateTimeInstance().format(new Date())
	 */
	public static String getLocaleDate() {
		return DateFormat.getDateTimeInstance().format(new Date());
	}

	public static String getSystemDateTime(String pattern) {
		Calendar calc = Calendar.getInstance();
		calc.setTime(new Date());
		Date date = calc.getTime();
		// FastDateFormat is a fast and thread-safe version of SimpleDateFormat.
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * isDate accepts a string and parses it to check if it is a date in one of the accepted formats
	 * contained in datePatterns.
	 * 
	 * @param dateString
	 * @return boolean isDate
	 */
	public static boolean isDate(String dateString) {
		log.debug("Enter isDate({})", dateString);
		boolean isDate = false;
		SimpleDateFormat df = new SimpleDateFormat();
		for (int i = 0; i < datePatterns.length; i++) {
			try {
				df.setLenient(false);
				df.applyPattern(datePatterns[i]);
				df.parse(dateString);
				isDate = true;
				break;
			} catch (ParseException e) {
				log.debug("ParseException in isDate(), return isDate = false", e);
				isDate = false;
			}
		}
		log.debug("Exit isDate()");
		return isDate;
	}

	/**
	 * convert Calendar to String with format "yyyy-MM-dd"
	 * 
	 * @param calendar
	 * @return null if date is null
	 */
	public static String calendarToString(Calendar calendar) {
		return calendarToString(calendar, dateFormat);
	}

	/**
	 * convert Calendar to String with format
	 * 
	 * @param calendar
	 * @return null if date is null
	 */
	public static String calendarToString(Calendar calendar, String format) {
		return dateToString(calendar.getTime(), format);
	}

	/**
	 * change date to String with format "yyyy-MM-dd"
	 * 
	 * @param date
	 * @return null if date is null
	 */
	public static String dateToString(Date date) {
		return dateToString(date, dateFormat);
	}

	/**
	 * change date to String with format
	 * 
	 * @return empty string if date is null
	 */
	public static String dateToString(Date date, String format) {
		if (date == null) return "";
		final SimpleDateFormat df = new SimpleDateFormat(format);
		String dateStr = df.format(date);
		return dateStr;
	}

	/**
	 * convert string to Calendar
	 * 
	 * @param dateString date string value
	 * @param format date format
	 * @return Return null if dateString is empty or the format string value is valid format.
	 * @throws ParseException
	 */
	public static Calendar stringToCalendar(String dateString, String format) throws ParseException {
		log.debug("Enter stringToCalendar({}, {})", dateString, format);

		if (StringUtils.isEmpty(dateString))
			throw new ParseException("dateString is null or blank", 0);
		Date date = stringToDate(dateString, format);
		Calendar cal = dateToCalendar(date);

		log.debug("Exit stringToCalendar()");
		return cal;
	}

	/**
	 * convert Date to Calendar
	 * 
	 * @param date
	 * @return null if date is null
	 */
	public static Calendar dateToCalendar(Date date) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
		}
		return null;
	}

	/**
	 * convert dateStringto Date
	 * 
	 * @param dateString with format "yyyy-MM-dd"
	 * @return null if date is null
	 * @throws ParseException
	 */
	public static Date stringToDate(String dateString) throws ParseException {
		return stringToDate(dateString, dateFormat);
	}

	/**
	 * convert String to Date
	 * 
	 * @param dateString date string value with <b>format</b>
	 * @param format date format
	 * @return Return null if dateString is empty or the format string value is valid format.
	 * @throws ParseException
	 */
	public static Date stringToDate(String dateString, String format) throws ParseException {
		if (StringUtils.isEmpty(dateString))
			throw new ParseException("dateString is null or blank", 0);
		final SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = df.parse(dateString);
		return date;
	}

	/**
	 * @return current hour. the hour of 24-hour system. e.g. 14 o'clock is 2 p.m return 14
	 */
	public static String getHour() {
		Calendar now = Calendar.getInstance();
		return Integer.toString(now.get(Calendar.HOUR_OF_DAY));
	}

	/**
	 * @return "d". Such as: if 2010-1-18, return 18
	 */
	public static String getDay() {
		Calendar calendar = Calendar.getInstance();
		return Integer.toString(calendar.get(Calendar.DATE));
	}

	/**
	 * @return current day of week. {Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday}
	 *         if today is Monday return Monday.
	 */
	public static String getWeek() {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return getWeekMap().get(dayOfWeek);
	}

	/**
	 * @return current month {January ,February ,March, April, May, June, July, August, September,
	 *         October, November, December}
	 */
	public static String getMonth() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		return getMonthMap().get(month);
	}

	/**
	 * @return current year. e.g. 2010-1-18 return 2010
	 */
	public static String getYear() {
		Calendar calendar = Calendar.getInstance();
		return Integer.toString(calendar.get(Calendar.YEAR));
	}

	/**
	 * Returns the value of the given calendar field - month
	 */
	public static int getMonth(Date date) {
		if (date == null) return -1;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}

	/**
	 * Returns the value of the given calendar field - year Return -1 if date is null
	 */
	public static int getYear(Date date) {
		if (date == null) return -1;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static Date getNextDate(Date date) {
		if (date == null) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	public static int getNextYear(Date date) {
		if (date == null) return -1;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR) + 1;
	}

	public static String concatYearMonth(String year, String month) {
		month = StringUtils.leftPad(month, 2, "0");
		return year + month;
	}

	public static String concatYearMonthDay(String year, String month, String day) {
		if (StringUtils.isEmpty(year) && StringUtils.isEmpty(month) && StringUtils.isEmpty(day)) {
			return "";
		}
		month = StringUtils.leftPad(month, 2, "0");
		day = StringUtils.leftPad(day, 2, "0");
		return year + month + day;
	}

	public static String getCurrentGMT() {
		Calendar now = Calendar.getInstance();
		Date time = now.getTime();
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));

		return df.format(time);
	}

	public static String getCurrentGMTforDisplay() {
		Calendar now = Calendar.getInstance();
		Date time = now.getTime();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));

		return df.format(time);
	}

	/**
	 * get the current week's order number of this year
	 * 
	 * @return int
	 */
	public static int getWeekOrderNumberOfYear() {
		int weekOrderOfYear = 0;
		Calendar now = Calendar.getInstance();
		weekOrderOfYear = now.get(Calendar.WEEK_OF_YEAR);
		return weekOrderOfYear;
	}

	/**
	 * get the week's order number of this year
	 * 
	 * @param strDate with format yyyy-MM-dd
	 * @return
	 */
	public static int getWeekOrderNumberOfYear(String strDate) {
		int weekOrderOfYear = 0;
		if (strDate == null) return weekOrderOfYear;
		try {
			Calendar cal = stringToCalendar(strDate, dateFormat);
			weekOrderOfYear = cal.get(Calendar.WEEK_OF_YEAR);
		} catch (ParseException e) {
			log.info("strDate = " + strDate + " in method getWeekOrderNumberOfYear()");
			log.error("Exception in getWeekOrderNumberOfYear()", e);
		}
		return weekOrderOfYear;
	}

	/**
	 * get the date's start time 00:00:00
	 * 
	 * @param Calendar date
	 * @return
	 */
	public static Calendar getDayStartTime(Calendar date) {
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		return date;
	}

	/**
	 * get the date's end time 23:59:59
	 * 
	 * @param Calendar date
	 * @return
	 */
	public static Calendar getDayEndTime(Calendar date) {
		date.set(Calendar.HOUR_OF_DAY, 23);
		date.set(Calendar.MINUTE, 59);
		date.set(Calendar.SECOND, 59);
		return date;
	}

	/**
	 * @return current time with specify date format
	 */
	public static String getNowTime(String dateformat) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
		String str = dateFormat.format(now);
		return str;
	}

	/**
	 * get current day with format yyyy-MM-dd
	 */
	public static String getCurrentDay() {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		String currentDay = df.format(now.getTime());
		return currentDay;
	}

	/**
	 * Change millis time to second.
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String timeMillis2Second(long timeInMillis) {
		return "" + ((float) timeInMillis / 1000);
	}

	/**
	 * @param startTime with format yyyy-MM-dd hh:mm:ss
	 * @return the count of days between startTime and current.
	 * @throws ParseException
	 */
	public static String getTimeUntiltoday(String startTime) throws ParseException {
		Calendar startDate = stringToCalendar(startTime, timeFormat);
		Calendar today = Calendar.getInstance();
		long start = startDate.getTime().getTime();
		long current = today.getTime().getTime();
		return calculateTime(current - start);
	}

	/**
	 * calculate millis to time
	 */
	public static String calculateTime(long time) {
		log.debug("Enter calculateTime({})", time);
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = time / dd;
		long hour = (time - day * dd) / hh;
		long minute = (time - day * dd - hour * hh) / mi;
		long second = (time - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = time - day * dd - hour * hh - minute * mi - second * ss;

		String strDay = day < 10 ? "0" + day : "" + day;
		String strHour = hour < 10 ? "0" + hour : "" + hour;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second;
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
		String dateString = strDay + " day(s) " + strHour + " hour(s) " + strMinute + " minutes "
				+ strSecond + " second(s) " + strMilliSecond;

		log.debug("Exit calculateTime() dateString = {}", dateString);
		return dateString;
	}

	public static Map<Integer, String> getWeekMap() {
		weekMap.put(2, "Monday");
		weekMap.put(3, "Tuesday");
		weekMap.put(4, "Wednesday");
		weekMap.put(5, "Thursday");
		weekMap.put(6, "Friday");
		weekMap.put(7, "Saturday");
		weekMap.put(1, "Sunday");
		return weekMap;
	}

	public static Map<Integer, String> getMonthMap() {
		monthMap.put(0, "January");
		monthMap.put(1, "February");
		monthMap.put(2, "March");
		monthMap.put(3, "April");
		monthMap.put(4, "May");
		monthMap.put(5, "June");
		monthMap.put(6, "July");
		monthMap.put(7, "August");
		monthMap.put(8, "September");
		monthMap.put(9, "October");
		monthMap.put(10, "November");
		monthMap.put(11, "December");
		return monthMap;
	}

	/**
	 * datePatterns is a string array containing accepted date formats used when parsing dates.
	 */
	private static String datePatterns[] = { "MM-dd-yyyy", "MM.dd.yyyy", "MM/dd/yyyy", "MM-dd-yy",
			"MM.dd.yy", "MM/dd/yy", "MMM-dd-yyyy", "MMM.dd.yyyy", "MMM/dd/yyyy", "MMM-dd-yy",
			"MMM.dd.yy", "MMM/dd/yy", "yyyy-MM-dd kk:mm:ss", "yyyy-MM-dd hh:mm:ss" };

	/**
	 * format is yyyy-MM-dd
	 */
	public static final String dateFormat = "yyyy-MM-dd";
	public static final String timeFormat = "yyyy-MM-dd hh:mm:ss";
	private static final Map<Integer, String> weekMap = new HashMap<Integer, String>();
	private static final Map<Integer, String> monthMap = new HashMap<Integer, String>();
}
