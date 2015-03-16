/**
 * @author Shang Pu
 */
package org.pu.test.base;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.collections.ListUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.pu.utils.DateUtils;
import org.pu.utils.IoUtils;


/**
 * <a href='http://junit.sourceforge.net/doc/faq/faq.htm'>JUnit FAQ</a>
 * 
 * <pre>
 *  @BeforeClass - oneTimeSetUp
 *  @Before - setUp
 *  @Test - testEmptyCollection
 *  @After - tearDown
 *  @Before - setUp
 *  @Test - testOneItemCollection
 *  @After - tearDown
 *  @AfterClass - oneTimeTearDown
 * 
 *  @Ignore("this regular expression isn't working yet")
 *  @Test(timeout=10000, expected=NullPointerException.class)
 *  method name
 *  @RunWith(Suite.class)
 *  @Suite.SuiteClasses({
 *  JunitTest1.class,
 *  JunitTest2.class
 *  })
 *  class name
 * 
 *  The following table gives an overview of the available annotations in JUnit 4.x.
 *  Table 1. Annotations
 *  Annotation Description
 *  @Test public void method() The annotation @Test identifies that a method is a test method.
 *  @Before public void method() Will execute the method before each test. This method can prepare the test environment (e.g. read input data, initialize the class).
 *  @After public void method() Will execute the method after each test. This  method can cleanup the test environment (e.g. delete temporary data, restore defaults).
 *  @BeforeClass public void method() Will execute the method once, before the start of all tests. This can be used to perform time intensive activities, for example to connect to a database.
 *  @AfterClass public void method() Will execute the method once, after all tests have finished. This can be used to perform clean-up activities, for example to disconnect from a database.
 *  @Ignore Will ignore the test method. This is useful when the underlying code has been changed and the test case has not yet been adapted. Or if the execution time of this test is too long to be included.
 *  @Test(expected = Exception.class) Fails, if the method does not throw the named exception.
 *  @Test(timeout=100) Fails, if the method takes longer than 100 milliseconds.
 * 
 *  The following table gives an overview of the available assert statements.
 *  Table 2. Test methods
 *  Statement Description
 *  fail(String) Let the method fail. Might be used to check that a certain part of the code is not reached. Or to have failing test before the test code is implemented.
 *  assertTrue(true) / assertTrue(false) Will always be true / false. Can be used to predefine a test result, if the test is not yet implemented.
 *  assertTrue([message], boolean condition) Checks that the boolean condition is true.
 *  assertsEquals([String message], expected, actual) Tests that two values are the same. Note: for arrays the reference is checked not the content of the arrays.
 *  assertsEquals([String message], expected, actual, tolerance) Test that float or double values match. The tolerance is the number of decimals which must be the same.
 *  assertNull([message], object) Checks that the object is null.
 *  assertNotNull([message], object) Checks that the object is not null.
 *  assertSame([String], expected, actual) Checks that both variables refer to the same object.
 *  assertNotSame([String], expected, actual) Checks that both variables refer to different objects.
 *  
 *  run JUnit from command window
 *  java org.junit.runner.JUnitCore <test class name>
 * </pre>
 */
public class TestBase {
	// private final org.apache.log4j.Logger log =
	// org.apache.log4j.Logger.getLogger(getClass());
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TestBase.class);
	/**
	 * path=/C:/sp/Dropbox/Base/DctmTools/bin/
	 */
	protected String path = getClass().getResource("/").getPath();
	protected static Properties prop = new Properties();
	protected static final String UTF8 = "UTF-8";

	@BeforeClass
	public static void loadProperties() {
		// load application.properties as properties
		String propertiesPath = "application.properties";
		prop = IoUtils.getProperties(propertiesPath);
		log.trace("props = {}", prop);
	}
	
	public void testUtils() {
		Collection<String> list1 = new ArrayList<String>();
		Collection<String> list2 = new ArrayList<String>();
		Assert.assertTrue(ListUtils.isEqualList(list1, list2));
	}

	protected void printCollection(Collection<?> c) {
		for (Object o : c) {
			log.info(o.toString());
			// System.out.println(o);
		}
	}

	protected <E> void printArray(E[] array) {
		int length = array.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == length - 1) {
				sb.append(array[i]);
			} else {
				sb.append(array[i]).append(", ");
			}
		}
		log.debug("array = {}", sb);
	}

	protected void printArray(int[] array) {
		int length = array.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			if (i == length - 1) {
				sb.append(array[i]);
			} else {
				sb.append(array[i]).append(", ");
			}
		}
		log.debug("array = {}", sb);
	}

	// @Test
	public void testSlf4j() {
		long startTime = Calendar.getInstance().getTimeInMillis();
		String msg = "{0} log {0} and message {1}";
		msg = MessageFormat.format(msg, new Object[] { "first", "2", "3" });

		log.trace("trace");
		log.debug("debug 1={} 2={} 3={}", new Object[] { "v1", "v2", "v3" });
		log.info("info msg = {}", msg);
		log.warn("warn");
		// log.error("error Exception {}.", "object instance", new
		// Exception("This is a test exception."));
		// log.fatal("fatal");//not fatal level in slf4j
		
		long endTime = Calendar.getInstance().getTimeInMillis();
		long time = endTime - startTime;
		log.debug("Operation Duration: {}; exact {} milliseconds", DateUtils.calculateTime(time), time);
	}
}
