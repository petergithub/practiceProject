package doing;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Sep 22, 2015 3:35:16 PM
 */

public class PracticeDate extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeDate.class);
	String dateFormat = "yyyy-MM-dd hh:mm:ss";
	String dateFormatUs = "MM/dd/yyyy HH:mm:ss";

	public void testDate() throws ParseException {
		Date date = new DateTime(2015, 9, 1, 0, 0, 0).toDate();
		log.info("system default format: date = {}", date);
		date = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA).parse("2015-09-02 02:00:00");
		log.info("getDateInstance with locale: date = {}", date);
		date = new SimpleDateFormat(dateFormat).parse("2015-09-02 02:00:00");
		log.info("SimpleDateFormat: date = {}", date);
		date = stringToDate("2015-09-02 02:00:00");
		log.info("SimpleDateFormat: date = {}", date);
		
		long localTimeStamp = System.currentTimeMillis();
		log.info("localDate = {}", new Date(localTimeStamp));
		log.info("utcDate = {}", new Date(converLocalTimeToUtcTime(localTimeStamp)));
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
		DateTime dt = new DateTime(2005, 3, 26, 12, 0, 0, 0);
		Date date = dt.toDate();
		log.info("dt = {}, date = {}", dt, date);
		DateTime plusPeriod = dt.plus(Period.days(1));
		DateTime plusDuration = dt.plus(new Duration(24L * 60L * 60L * 1000L));
		DateTime onedayLater = dt.plusDays(1);
		Assert.assertEquals(onedayLater, plusDuration);
		Assert.assertEquals(onedayLater, plusPeriod);
		log.info("onedayLater = {}", onedayLater);
		String onedayLaterStr = onedayLater.toString(dateFormat);
		log.info("onedayLaterStr = {}", onedayLaterStr);
		DateTime twoHoursLater = dt.plusHours(2);
		log.info("twoHoursLater = {}", twoHoursLater);

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
        System.out.println((new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date()));
        

        /** 输出格式: 2006-01-01 13:00:00 */
        System.out.println((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));

        /** 输出格式: 20060101000000***/
        java.text.DateFormat format2 = new java.text.SimpleDateFormat("yyyyMMddhhmmss");
        s = format2.format(new Date());
        System.out.println(s);
    }
}
