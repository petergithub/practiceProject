package doing;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version Date: Jan 14, 2014 6:45:48 PM
 * @author Shang Pu
 */
public class StringDemo extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(StringDemo.class);

	@Test
	public void testNullString() {
		String nullStr = null;
		Assert.assertNull(nullStr);
		log.info("null = {}", nullStr);
		nullStr = "null";
		assertEquals("null", nullStr);
		log.info("nullStr = {}", nullStr);
	}
	
	public void testListString() {
		List<String> gpseqnum = Arrays.asList("a", "c", "b");
		log.info("gpseqnum = {}", gpseqnum);
		if (gpseqnum.size() == 0) return;
		String gpseqnumQuery = "'";
		for (String s : gpseqnum) {
			gpseqnumQuery = gpseqnumQuery + s.trim() + "','";
		}
		gpseqnumQuery = gpseqnumQuery.substring(0, gpseqnumQuery.lastIndexOf(",'"));
		log.info("gpseqnumQuery = {}", gpseqnumQuery);
	}

	public void testSplitLengthString() {
		String[] array = ", ".split(",");
		Assert.assertEquals(2, array.length);
		Assert.assertEquals("", array[0]);
		Assert.assertEquals(" ", array[1]);

		String[] array2 = ",".split(",");
		Assert.assertEquals(0, array2.length);
	}

	public void testSplitEscapeString() {
		String str = "a|cdef";
		String[] splitWithEscape = str.split("\\|");
		printArray(splitWithEscape);
		Assert.assertEquals("a", splitWithEscape[0]);
		Assert.assertEquals("cdef", splitWithEscape[1]);

		// split to char by char
		String[] splitWithoutEscape = str.split("|");
		printArray(splitWithoutEscape);
		Assert.assertEquals("", splitWithoutEscape[0]);
		Assert.assertEquals("a", splitWithoutEscape[1]);
		Assert.assertEquals("|", splitWithoutEscape[2]);
		Assert.assertEquals("c", splitWithoutEscape[3]);
	}
}
