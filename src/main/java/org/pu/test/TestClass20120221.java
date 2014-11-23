/**
 * @author Shang Pu
 */
package org.pu.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.pu.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestClass20120221 extends TestBase {
	private final Logger log = LoggerFactory.getLogger(TestClass20120221.class);

	/**
	 * C:\sp\Dropbox\Base\DctmTools\bin
	 */
	String path = getClass().getResource("/").getPath();

	public void getFileIntheSamePackage() {
		InputStream in = getClass().getResourceAsStream("/test/base/TestBase.class");
		Assert.assertNotNull(in);
	}

	public void testDecimalFormat() {
		double d = 0.234567;
		DecimalFormat df = new DecimalFormat("#.##");// get 0.23
		// DecimalFormat df = new DecimalFormat("#.00"); // get .23
		// display at lease 2 decimals and at most 4.
		// DecimalFormat df = new DecimalFormat("0.00##");
		log.info("format = {}", df.format(d));
		log.info("format = {}", df.format(2.3));

		double a = 34.51236;
		NumberFormat nf = DecimalFormat.getInstance();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(4);
		df.setRoundingMode(RoundingMode.DOWN);
		System.out.println(nf.format(a));

		NumberFormat numberFormat = new DecimalFormat("#,###.00");
		for (double amount = 50; amount < 1100; amount += 350.5) {
			System.out.println("");
			System.out.println("amount without formatting: " + amount);
			System.out.println("amount with    formatting: " + numberFormat.format(amount));
		}
	}

	/**
	 * update log4j.properties dynamic
	 */
	public void testLog4jLoadingConfiguration() throws IOException {
		log.debug("Enter testLog4jLoadingConfiguration()");
		// load log4j.properties into memory
		String propertiesPath = "C:/sp/Dropbox/Base/DctmTools/config/log4j.properties";
		org.apache.log4j.PropertyConfigurator.configure(propertiesPath);

		// load log4j.properties as properties
		Properties props = new Properties();
		FileInputStream in = new FileInputStream(propertiesPath);
		props.load(in);
		in.close();

		// change value
		String root = props.getProperty("log4j.rootLogger");
		log.info(root);
		props.put("log4j.rootLogger", "ERROR, file, stdout"); // overwrite
		// "log4j.rootLogger"

		org.apache.log4j.PropertyConfigurator.configure(props);
		log.info(props.getProperty("log4j.rootLogger"));
		log.debug("Exit testLog4jLoadingConfiguration()");
	}

	public void testBoolean() {
		Assert.assertTrue(Boolean.valueOf("True"));
		Assert.assertFalse(Boolean.valueOf("1"));
		Assert.assertEquals(0, "".length());
	}

	public void testPrint() {
		String[] stringArray = { "1", "2", "3" };
		log.info("the first element of stringArray = {}", stringArray);
		printArray(stringArray);

		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "1v");
		map.put(2, "2v");
		map.put(3, "3v");
		List<String> list = new ArrayList<String>(Arrays.asList(stringArray));
		log.info("map = " + map);
		log.info("list={}", list);

		String[] arr = {};
		Assert.assertEquals(0, arr.length);
		for (String s : arr) {
			log.info("Not execute, if you see this, it must be wrong s = {}", s);
		}
	}

	@Test
	public void testListRemove() {
		List<String> list = new ArrayList<String>(Arrays.asList("a", "b", "c", "d", ""));
		log.info("list = {}", list);
		list.remove("");
		log.info("list = {}", list);
		Iterator<String> i = list.iterator();
		while (i.hasNext()) {
			if (i.next().equals("b")) {
				i.remove();
			}
		}
		log.info("list = {}", list);
	}

	public void testList() {
		List<String> list = Arrays.asList("a", "b", "c", "d");
		for (ListIterator<String> it = list.listIterator(list.size()); it.hasPrevious();) {
			String t = it.previous();
			log.info("t = {}", t);
		}
	}

	// @Test(expected=ConcurrentModificationException.class)
	public void testListRemoveExecption() {
		List<String> list = new ArrayList<String>(Arrays.asList("a", "b", "c", "d"));
		log.info("list = {}", list);
		for (String s : list) {
			if (s.equals("b")) {
				list.remove("b");
			}
		}
		log.info("list = {}", list);
	}

	public void testSetAddAll() {
		String[] array = { "a", "b", "c", "a", "b" };
		List<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(array));
		log.info("list");
		printCollection(list);
		list.remove("a");
		log.info("remove a");
		printCollection(list);
		Set<String> set = new HashSet<String>();
		set.addAll(list);
		log.info("set");
		printCollection(set);

		set.remove("c");
		log.info("remove a");
		printCollection(set);
	}

	@SuppressWarnings("null")
	public void testArrayNull() {
		List<String> ids = null;
		for (String id : ids) {
			log.info(id);
		}
	}

	public void testArraySplit() {
		// String str = "11820211;";
		String str = "11820211;11819142;11818044;";
		String[] array = str.split(";");
		for (String val : array) {
			val = val.trim();
			if (!val.equals(";")) {
				log.info("val = {}", val);
			}
		}
	}

	public void testFinally() throws IOException {
		log.info(finallyMethod());
	}

	public String finallyMethod() {
		String result = "1";
		int i = 1;
		try {
			i = 2;
			result = "2 in try";
			log.info(result + " in try");
			return result;
		} finally {
			result = "3 in finally";
			log.info(result + " in finally");
			if (i == 2) {
				return result;
			}
		}
	}

	public void testCharStream() throws IOException {
		String file = path + "testCharStream.txt";
		FileWriter fw = new FileWriter(file);
		int count = 3;
		fw.write(count);
		fw.write(4);
		fw.write(5);
		fw.close();

		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		int c = br.read();
		int d = br.read();
		int e = br.read();
		log.info(String.valueOf(c));
		log.info(String.valueOf(d));
		log.info(String.valueOf(e));
		fr.close();
	}

	public void testByteStream() throws IOException {
		String file = path + "testStream.txt";
		// ByteArrayInputStream
		// OutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		int count = 3;
		oos.writeInt(count);
		oos.close();
		// File inFile = new File("C:\\Downloads\\jigloo352.zip");
		// FileInputStream fis = new FileInputStream(inFile);
		// fis.close();
		// FileOutputStream fos = new FileOutputStream(file);
		// int c = 3;
		// fos.write(c);
		// fos.close();

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		int c = ois.readInt();
		log.info(String.valueOf(c));
		ois.close();
	}

	public void testSplit() {
		String countries = "|a|b||";
		String[] country_names = countries.split("\\|");
		for (String s : country_names) {
			log.info(s);
		}
		String string = "gtc_documentT*T*subtype";
		String DISPLAY_DELIMITER = "[T]\\*[T]\\*";
		String[] array = string.split(DISPLAY_DELIMITER);
		log.info(array[0]);
		log.info(array[1]);
	}

	public void testPath() throws IOException {
		// /D:/Shang/Dropbox/Base/DctmTools/bin/
		// get classpath
		log.info(getClass().getResource("/").getPath());
		// /D:/Shang/Dropbox/Base/DctmTools/bin/com/test/
		log.info(getClass().getResource("").getPath());

		// C:\\test.xml
		File file = new File("c:\\test.xml");
		log.info(file.getAbsolutePath());
		// test.xml
		log.info(file.getPath());
		// C:\Shang\Dropbox\Base\DctmTools\test.xml
		log.info(file.getCanonicalPath());
	}

	public void readFile() throws IOException {
		FileReader reader = new FileReader(getClass().getResource("/").getPath() + "log4j.properties");
		BufferedReader br = new BufferedReader(reader);
		String line;
		String file2String = "";
		try {
			while ((line = br.readLine()) != null) {
				log.info(line);
				file2String += System.getProperty("line.separator");
				file2String += line;
			}
		} finally {
			IoUtils.close(br);
		}
		log.info("=============================");
		log.info(file2String);
	}

	/**
	 * 文件转换为字符串
	 * 
	 * @param f 文件
	 * @param charset 文件的字符集
	 * @return 文件内容
	 */
	public static String file2String(File f, String charset) {
		String result = null;
		try {
			result = stream2String(new FileInputStream(f), charset);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 文件转换为字符串
	 * 
	 * @param in 字节流
	 * @param charset 文件的字符集
	 * @return 文件内容
	 */
	public static String stream2String(InputStream in, String charset) {
		StringBuffer sb = new StringBuffer();
		try {
			Reader r = new InputStreamReader(in, charset);
			int length = 0;
			for (char[] c = new char[1024]; (length = r.read(c)) != -1;) {
				sb.append(c, 0, length);
			}
			r.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
