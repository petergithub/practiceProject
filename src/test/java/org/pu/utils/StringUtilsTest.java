package org.pu.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version Date: Jun 27, 2013 9:54:39 AM
 * @author Shang Pu
 */
public class StringUtilsTest extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(StringUtilsTest.class);

	@Test
	public void testReplaceCharByAnotherChar() {
		String name = "the data might contain / or \\ or ? or & or ! or % or ' or # etc";
		String fileName = StringUtils.replaceCharByAnotherChar(name, "/\\:*?\"<>|,", "--_#!`()!_");
		log.info("fileName = {}", fileName);
		assertFalse(fileName.contains("/"));
		assertFalse(fileName.contains("\\"));
		assertFalse(fileName.contains("?"));
		assertTrue(fileName.contains("-"));
	}

	public void testConcatArrays() {
		String[] a = { "a", "b", "c" };
		String[] b = { "1", "2", "3" };
		String[] c = StringUtils.concatArrays(a, b);
		printArray(c);
	}
}
