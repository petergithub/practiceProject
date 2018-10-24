package doing;

import org.apache.log4j.helpers.ISO8601DateFormat;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import demo.security.MD5Util;

/**
 * Refer to java.text.SimpleDateFormat
 * <p>
 * "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
 * <p>
 * "yyyyy.MMMMM.dd GGG hh:mm aaa" 02001.July.04 AD 12:08 PM
 * <p>
 * "EEE, d MMM yyyy HH:mm:ss Z" Wed, 4 Jul 2001 12:08:56 -0700
 * <p>
 * "yyyy-MM-dd'T'HH:mm:ss.SSSZ" 2001-07-04T12:08:56.235-0700
 * <p>
 * "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" 2001-07-04T12:08:56.235-07:00
 * <p>
 * "YYYY-'W'ww-u" 2001-W27-3
 * <p>
 * 
 * @author Shang Pu
 * @version Date: Sep 22, 2015 3:35:16 PM
 * 
 *          <a href=
 *          "http://stackoverflow.com/questions/11294307/convert-java-date-to-utc-string"
 *          >Convert Java Date to UTC String</a>
 */

public class PracticeDate extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeDate.class);
	String dateFormat = "yyyy-MM-dd hh:mm:ss"; // 2006-01-02 15:04:05
	String dateFormatUs = "MM/dd/yyyy HH:mm:ss";
	String dateFormat_ISO_8601Z = "yyyy-MM-dd'T'HH:mm:ssZ"; // 2006-01-02T07:04:05+0000
	String dateFormat_ISO_8601z = "yyyy-MM-dd'T'HH:mm:ssz"; // 2006-01-02T07:04:05UTC
	String datePattern = "EEE MMM d HH:mm:ss zzz yyyy"; // Thu Jun 18 20:56:02 EDT 2009
	//YYYY-MM-DDThh:mm:ss.sTZD (eg 1997-07-16T19:20:30.45+01:00)
	
	public static void main(String[] args){
		TimeZone t = TimeZone.getDefault();
		log.info("t[{}]", t);
		System.out.println(new Date());
		TimeZone timeZone = Calendar.getInstance().getTimeZone();
		System.out.println(timeZone.getDisplayName(false, TimeZone.SHORT)); 
	}
    
    /**
     * Date对象表示特定的日期和时间，而LocalDate(Java8)对象只包含没有任何时间信息的日期
     */
    @Test
    public void jdk8LocalDate() {
        LocalDate now = LocalDate.now();
        LocalDate sevenDaysBefore = now.minusDays(7L);
        
        boolean isAfter = now.isAfter(sevenDaysBefore);
        Assert.assertTrue(isAfter);
        
        // 日期差 返回1年2个月3天  P-1Y-2M-3D 里的 3
        java.time.Period period = java.time.Period.between(LocalDate.of(2018, 8, 15), LocalDate.of(2017, 6, 12));
        long duration = period.get(ChronoUnit.DAYS);
        Assert.assertEquals(-3, duration);
        log.info("period: {} duration: {}", period, duration);
        
        // 日期差 计算总天数 465
        long durationDays = ChronoUnit.DAYS.between(LocalDate.of(2018, 8, 15), LocalDate.of(2017, 6, 12));
        Assert.assertEquals(-429, durationDays);
        log.info("now: {} sevenDaysBefore: {} duration: {} durationDays: {}", now, sevenDaysBefore, duration, durationDays);

//      Date 转 LocalDate
//        1）将java.util.Date转换为ZonedDateTime。
//        2）使用它的toLocalDate（）方法从ZonedDateTime获取LocalDate。
        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        // LocalDate转Date
//      1）使用ZonedDateTime将LocalDate转换为Instant。
//      2）使用from（）方法从Instant对象获取Date的实例
        Date date = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        
        // 计算一年前或一年后的日期
        LocalDate previousYear = LocalDate.now().minus(1, ChronoUnit.YEARS);
        LocalDate nextYear = LocalDate.now().plus(1, ChronoUnit.YEARS);
    }
    
    @Test
    public void jdk8LocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysBefore = now.minusDays(7L);
        long duration = java.time.Duration.between(LocalDateTime.now(), LocalDateTime.of(2018, 9, 13, 18, 20, 30)).toDays();
        log.info("now: {} sevenDaysBefore: {} duration: {}", now, sevenDaysBefore, duration);
        

        //Date转换为LocalDateTime
        LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        
        //LocalDateTime转Date
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }
	
    /**
     * 
     * Instant：瞬时实例。 
     * LocalDate：本地日期，不包含具体时间 例如：2014-01-14 可以用来记录生日、纪念日、加盟日等。
     * LocalTime：本地时间，不包含日期。 
     * LocalDateTime：组合了日期和时间，但不包含时差和时区信息。
     * ZonedDateTime：最完整的日期时间，包含时区和相对UTC或格林威治的时差。
     * http://www.importnew.com/15637.html
     * http://www.importnew.com/26129.html
     * 
     * https://nkcoder.github.io/2016/01/31/java-8-date-time-api/
     */
    @Test
    public void jdk8Date() {
        // Instant表示某一个时间点的时间戳，可以类比于java.uti.Date
        Instant begin = Instant.now();
        begin.plus(5, ChronoUnit.SECONDS);
        begin.minusMillis(50);
        begin.isBefore(Instant.now());
        begin.toEpochMilli();
        log.info("begin: {}", begin);
        
        // Duration表示Instant之间的时间差，可以用来统计任务的执行时间，也支持各种运算操作
        // do some work
        Instant end = Instant.now();
        Duration elapsed = Duration.between(begin, end);
        elapsed.toMillis();
        elapsed.dividedBy(10).minus(Duration.ofMillis(10)).isNegative();
        elapsed.isZero();
        elapsed.plusHours(3);
        
        LocalDate now = LocalDate.now();
        LocalDate today = LocalDate.of(2016, 1, 31);
        LocalDate today2 = LocalDate.of(2016, Month.JANUARY, 31);   // JANUARY = 1, ..., DECEMBER = 12
        
        today2.getDayOfWeek().getValue();   // Monday = 1, ..., Sunday = 7
        LocalDate dayOfYear = Year.now().atDay(220);
        YearMonth april = Year.of(2016).atMonth(Month.APRIL);
        
//        注意，有些操作得到的日期可能是不存在的，比如2016-01-31增加1个月后为2016-02-31，该日期是不存在的，返回值为该月的最后一天，即2016-02-29:
        LocalDate nextMonth = LocalDate.of(2016, 1, 31).plusMonths(1);  // 2016-02-29
        
//        Period用来表示两个LocalDate之间的时间差，支持各种运算操作：
        LocalDate fiveDaysLater = LocalDate.now().plusDays(5);
        java.time.Period period = LocalDate.now().until(fiveDaysLater).plusMonths(2);
        period.isNegative();
        
//        TemporalAdjusters用于表示某个月第一天、下个周一等日期：
        LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate.now().with(TemporalAdjusters.lastInMonth(DayOfWeek.SUNDAY));
        LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        
        //LocalTime表示时间，没有日期，与时区(TimeZone)无关：
        LocalTime.now().isBefore(LocalTime.of(16, 2, 1));
        LocalTime.now().plusHours(2).getHour();
        
        //LocalDateTime表示日期和时间，适用于时区固定不变的场合(LocalDateTime使用系统默认的时区)，
        //如果需要根据时区调整日期和时间，应该使用ZonedDateTime:
        LocalDateTime.now().plusDays(3).minusHours(5).isAfter(LocalDateTime.of(2016, 1, 30, 10, 20, 30));
        
        //ZonedDateTime表示带时区的日期和时间，支持的操作与LocalDateTime非常类似：
        Set<String> zones = ZoneId.getAvailableZoneIds();
        ZonedDateTime.now(ZoneId.of("Asia/Shanghai")).plusMonths(1).minusHours(3)
               .isBefore(ZonedDateTime.now());
        
        // ZonedDateTime与LocalDateTime、Instant之间可以相互转换：
        ZonedDateTime nowOfShanghai = LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime.now(ZoneId.of("UTC")).toLocalDate();
        ZonedDateTime nowOfShanghai2 = Instant.now().atZone(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime.of(LocalDate.now(), LocalTime.now(), ZoneId.of("UTC")).toInstant();
        
        // Formatting 与 Parsing
        // 要格式化或者解析日期时，需要使用到DateTimeFormatter，用来定义日期或时间的格式：
     // 2016-01-31T15:39:31.481
        DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
        // Jan 31, 2016 3:50:04 PM
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(LocalDateTime.now());
        // Sun 2016-01-31 15:50:04
        DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        LocalDateTime.parse("2016-01-31 15:51:00-0400", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssxx"));
        LocalDate.parse("2016-01-31");

        //Date和Instant互相转换
        Date date = Date.from(Instant.now());
        Instant instant = date.toInstant();
        
        // 日期和时间格式化的常见格式：
//           年       yy: 16      yyyy: 2016
//           月       M: 1        MM: 01
//           日       d: 3        dd: 03
//           周       e: 3        E:  Web
//           时       H: 9        HH: 09
//           钟       mm: 02
//           秒       ss: 00
//           纳秒      nnnnnn:000000
//           时区偏移    x: -04     xx:-0400
        
    }

	@Test
	public void date() throws ParseException {
		log.info("System.currentTimeMillis(): {}", System.currentTimeMillis());
		int hour = (int) (System.currentTimeMillis() / (1000*60*60)) ;
		log.info("hour:{}", hour);
		
		String md5 = MD5Util.md5("account" + hour);
		log.info("md5:{}", md5);
		
	}
	public void parseStringToDate() throws ParseException {
		String strDate = "Thu Jun 18 20:56:02 EDT 2009";
		SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
		Date dateStr = formatter.parse(strDate);
		String formattedDate = formatter.format(dateStr);
		log.info("formattedDate: {}", formattedDate);
	}
	
	@Test
	public void parseLongToDate() throws ParseException {
		Date date = new Date(1464593775057l);
		log.info("date[{}]", date);
	}
	
	
	@Test
	public void testDate() throws ParseException {
		// 20060102150405
		DateTime dt = new DateTime(2006, 1, 2, 15, 4, 5);
		Date date = dt.toDate();

		// Mon Jan 02 15:04:05 CST 2006
		log.info("system default format: date = {}", date);
		date = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA).parse(
				"2006-01-02 15:04:05");
		log.info("getDateInstance with locale CHINA: date = {}", date);
		Assert.assertEquals(date.toString(), "Mon Jan 02 00:00:00 CST 2006");
        date = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US).parse(
                "Mon Jan 02 15:04:05 CST 2006");
        log.info("getDateInstance with locale US : date = {}", date);
		
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

		org.joda.time.format.DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
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
		final org.joda.time.format.DateTimeFormatter format = DateTimeFormat.forPattern(dateFormatUs);
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
		DateTime plusDuration = dt.plus(new org.joda.time.Duration(24L * 60L * 60L * 1000L));
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
		org.joda.time.format.DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		log.info("ISODateTimeFormat = {}", fmt.print(dt));
		
		DateTime daysAgo = today.minusDays(35);
		log.info("daysAgo: {}", daysAgo);
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
