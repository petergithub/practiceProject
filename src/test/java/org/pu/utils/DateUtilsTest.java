package org.pu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.pu.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Jul 17, 2012 9:56:26 PM
 */
public class DateUtilsTest {
	private final Logger log = LoggerFactory.getLogger(DateUtilsTest.class);

	public void testDate() throws ParseException {
		Calendar cal = Calendar.getInstance();
		log.info("cal = {}", cal.getTime());

		String str = DateUtils.calculateTime(cal.getTimeInMillis());
		log.info("str = {}", str);

		Date date = DateUtils.stringToDate("02-12-1986", "MM-dd-yyyy");
		log.info("date = {}", date);

		Date date1 = DateUtils.stringToDate("2012-03-23", "yyyy-MM-dd");
		log.info("date = {}", date1);

		String format = "yyyy-MM-dd kk:mm:ss";
		Date date2 = DateUtils.stringToDate("2012-03-23 01:23:45", format);
		log.info("date = {}", date2);
	}

	@Test
	public void formatDate() {
//		9-4-13 5_23 PM
		String format = "M-d-yy h_m a";
		final SimpleDateFormat df = new SimpleDateFormat(format );
		String dateStr = df.format(new Date());
		log.info("dateStr = {}", dateStr);
	}
}
