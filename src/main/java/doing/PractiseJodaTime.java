package doing;

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

public class PractiseJodaTime extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PractiseJodaTime.class);
	String dateFormat = "yyyy-MM-dd hh:mm:ss";
	String dateFormatUs = "MM/dd/yyyy HH:mm:ss";

	@Test
	public void testDate() {
		Date date = new DateTime(2015, 9, 1, 0, 0, 0).toDate();
		log.info("date = {}", date);
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
	}
}
