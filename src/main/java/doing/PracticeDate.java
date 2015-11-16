package doing;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.Assert;

import org.apache.log4j.helpers.ISO8601DateFormat;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Sep 22, 2015 3:35:16 PM
 * 
 *          <a href=
 *          "http://stackoverflow.com/questions/11294307/convert-java-date-to-utc-string"
 *          >Convert Java Date to UTC String</a>
 */

public class PracticeDate extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeDate.class);
	String dateFormat = "yyyy-MM-dd hh:mm:ss";
	String dateFormatUs = "MM/dd/yyyy HH:mm:ss";
	String dateFormat_ISO_8601Z = "yyyy-MM-dd'T'HH:mm:ssZ";
	String dateFormat_ISO_8601z = "yyyy-MM-dd'T'HH:mm:ssz";

	public void testDate() throws ParseException {
		// 20060102150405
		DateTime dt = new DateTime(2006, 1, 2, 15, 4, 5);
		Date date = dt.toDate();

		// Mon Jan 02 15:04:05 CST 2006
		log.info("system default format: date = {}", date);
		date = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA).parse(
				"2006-01-02 15:04:05");
		log.info("getDateInstance with locale: date = {}", date);
		Assert.assertEquals(date.toString(), "Mon Jan 02 00:00:00 CST 2006");
		
		date = new SimpleDateFormat(dateFormat).parse("2006-01-02 15:04:05");
		log.info("SimpleDateFormat: date = {}", date);
		Assert.assertEquals(date.toString(), "Mon Jan 02 15:04:05 CST 2006");

		date = stringToDate("2006-01-02 15:04:05");
		log.info("SimpleDateFormat: date = {}", date);
		Assert.assertEquals(date.toString(), "Mon Jan 02 15:04:05 CST 2006");

		long localTimeStamp = System.currentTimeMillis();
		log.info("localDate = {}", new Date(localTimeStamp));
		log.info("utcDate = {}", new Date(converLocalTimeToUtcTime(localTimeStamp)));

		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat(dateFormat_ISO_8601Z);
		df.setTimeZone(tz);
		String utcAsISO = df.format(dt.toDate());
		log.info("nowAsISO = {}", utcAsISO);
		Assert.assertEquals(utcAsISO, "2006-01-02T07:04:05+0000");

		DateFormat dfz = new SimpleDateFormat(dateFormat_ISO_8601z);
		dfz.setTimeZone(tz);
		String utcAsISOz = dfz.format(dt.toDate());
		log.info("nowAsISO = {}", utcAsISOz);
		Assert.assertEquals(utcAsISOz, "2006-01-02T07:04:05UTC");

		DateFormat dfISO_8601z = new SimpleDateFormat(dateFormat_ISO_8601z);
		Assert.assertEquals(dfISO_8601z.format(dt.toDate()), "2006-01-02T15:04:05CST");
		
		DateFormat dfISO_8601Z = new SimpleDateFormat(dateFormat_ISO_8601Z);
		Assert.assertEquals(dfISO_8601Z.format(dt.toDate()), "2006-01-02T15:04:05+0800");

		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		log.info("ISODateTimeFormat = {}", fmt.print(dt));
		Assert.assertEquals(fmt.print(dt), "2006-01-02T15:04:05.000+08:00");

		ISO8601DateFormat dfISO8601 = new ISO8601DateFormat();
		// Date d = dfISO8601.parse("2010-07-28T22:25:51Z");
		Date d = Date.from(Instant.parse("2014-12-12T10:39:40Z"));
		log.info("d[{}]", d);
		String ISO8601 = dfISO8601.format(dt.toDate());
		log.info("dfISO8601.format(dt.toDate()) = {}", ISO8601);
		Assert.assertEquals(ISO8601, "2006-01-02 15:04:05,000");

		// Java 8: '2015-12-03T10:15:30.120Z'
		String dateJava8 = Instant.now().toString();
		log.info("dateJava8[{}]", dateJava8);
	}

	public static long getLocalToUtcDelta() {
		Calendar local = Calendar.getInstance();
		local.clear();
		local.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
		return local.getTimeInMillis();
	}

	public static long converLocalTimeToUtcTime(long timeSinceLocalEpoch) {
		log.info("Enter converLocalTimeToUtcTime(timeSinceLocalEpoch[{}])", timeSinceLocalEpoch);
		return timeSinceLocalEpoch + getLocalToUtcDelta();
	}

	public static Date stringToDate(String value) {
		try {
			log.info("stringToDate value:{}, value.lenth:{}", value, value.length());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(value.trim());
			return date;
		} catch (ParseException e) {
			log.error("parseDate error, value=" + value, e);
			return null;
		}
	}

	public void testDateBetween() {
		final String dateStart = "01/14/2012 09:29:58";
		final String dateStop = "01/15/2012 10:31:48";
		final DateTimeFormatter format = DateTimeFormat.forPattern(dateFormatUs);
		final DateTime dt1 = format.parseDateTime(dateStart);
		final DateTime dt2 = format.parseDateTime(dateStop);

		System.out.print(Days.daysBetween(dt1, dt2).getDays() + " days, ");
		System.out.print(Hours.hoursBetween(dt1, dt2).getHours() % 24 + " hours, ");
		System.out.print(Minutes.minutesBetween(dt1, dt2).getMinutes() % 60 + " minutes, ");
		System.out.print(Seconds.secondsBetween(dt1, dt2).getSeconds() % 60 + " seconds.");
		System.out.println();

		long diffInMillis = dt2.getMillis() - dt1.getMillis();
		log.info("diffInMillis = {}", diffInMillis);
		long diffInMillisRevers = dt1.getMillis() - dt2.getMillis();
		log.info("diffInMillisRevers = {}", diffInMillisRevers);
	}

	@Test
	public void testJodaDate() {
		DateTime dt = new DateTime(2006, 1, 2, 15, 4, 5);
		Date date = dt.toDate();
		log.info("dt = {}, date = {}, dt.toDateTimeISO()[{}]", dt, date, dt.toDateTimeISO());
		log.info("dt.toDateTime(DateTimeZone.UTC)[{}]", dt.toDateTime(DateTimeZone.UTC));

		Assert.assertEquals(dt.toString(), "2006-01-02T15:04:05.000+08:00");
		Assert.assertEquals(date.toString(), "Mon Jan 02 15:04:05 CST 2006");
		Assert.assertEquals(dt.toDateTimeISO().toString(), "2006-01-02T15:04:05.000+08:00");
		Assert.assertEquals(dt.toDateTime(DateTimeZone.UTC).toString(), "2006-01-02T07:04:05.000Z");

		// 2015-11-16T08:45:45.995Z
		DateTime now = new DateTime(DateTimeZone.UTC);
		log.info("now[{}]", now);

		DateTime plusPeriod = dt.plus(Period.days(1));
		DateTime plusDuration = dt.plus(new Duration(24L * 60L * 60L * 1000L));
		DateTime onedayLater = dt.plusDays(1);
		Assert.assertEquals(onedayLater, plusDuration);
		Assert.assertEquals(onedayLater, plusPeriod);
		Assert.assertEquals(onedayLater.toString(), "2006-01-03T15:04:05.000+08:00");
		String onedayLaterStr = onedayLater.toString(dateFormat);
		Assert.assertEquals(onedayLaterStr, "2006-01-03 03:04:05");
		DateTime twoHoursLater = dt.plusHours(2);
		Assert.assertEquals(twoHoursLater.toString(), "2006-01-02T17:04:05.000+08:00");

		java.util.Date juDate = new Date();
		dt = new DateTime(juDate);

		String monthName = dt.monthOfYear().getAsText();
		String frenchShortName = dt.monthOfYear().getAsShortText(Locale.CHINA);
		boolean isLeapYear = dt.year().isLeap();
		DateTime rounded = dt.dayOfMonth().roundFloorCopy();
		DateTime year2000 = dt.withYear(2000);
		log.debug("monthName[{}],frenchShortName[{}],isLeapYear[{}],rounded[{}],year2000[{}]",
				new Object[] { monthName, frenchShortName, isLeapYear, rounded, year2000 });

		DateTime today = new DateTime().withTimeAtStartOfDay();
		log.info("today = {}", today);

		dt = new DateTime();
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		log.info("ISODateTimeFormat = {}", fmt.print(dt));
	}

	public void testDateFormat() throws ParseException {
		Date d = new Date();
		String s;

		/** Date类的格式: Sat Apr 16 13:17:29 CST 2006 */
		System.out.println(d);

		System.out.println("******************************************");

		/** getDateInstance() */
		/** 输出格式: 2006-4-16 */
		s = DateFormat.getDateInstance().format(d);
		System.out.println(s);

		/** 输出格式: 2006-4-16 */
		s = DateFormat.getDateInstance(DateFormat.DEFAULT).format(d);
		System.out.println(s);

		/** 输出格式: 2006年4月16日 星期六 */
		s = DateFormat.getDateInstance(DateFormat.FULL).format(d);
		System.out.println(s);

		/** 输出格式: 2006-4-16 */
		s = DateFormat.getDateInstance(DateFormat.MEDIUM).format(d);
		System.out.println(s);

		/** 输出格式: 06-4-16 */
		s = DateFormat.getDateInstance(DateFormat.SHORT).format(d);
		System.out.println(s);

		/** 输出格式: 2006-01-01 00:00:00 */
		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		s = format1.format(new Date());
		System.out.println(s);

		/** 输出格式: 2006-01-01 01:00:00 */
		System.out.println((new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))
				.format(new Date()));

		/** 输出格式: 2006-01-01 13:00:00 */
		System.out.println((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
				.format(new Date()));

		/** 输出格式: 20060101000000 ***/
		java.text.DateFormat format2 = new java.text.SimpleDateFormat("yyyyMMddhhmmss");
		s = format2.format(new Date());
		System.out.println(s);
	}
}
