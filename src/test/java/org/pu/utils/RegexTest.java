package org.pu.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @version Date: Feb 22, 2013 1:56:39 PM
 * @author Shang Pu
 */
public class RegexTest extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(RegexTest.class);

	@Test
	public void testRepleaceFirst() {
		String str = "aaaa\\Abb";
		Scanner in = new Scanner(System.in);
		str = in.nextLine();
		in.close();

		log.info("str = {}", str);
		String result = str.replaceFirst("b", "c");
		log.info("result = {}", result);
	}

	// TODO: Phone and character
	public void testMatch() {
		String str = "1adD";
		log.info("str.matches(regex) = {}", str.matches("[0-9a-zA-Z]{4}"));
		log.info("str.matches(regex) = {}", str.matches("[\\w]{4}"));

		String phone = "123-(456)-9999";
		String phoneNumberPattern = "(\\d-)?(\\d{3}-)?\\d{3}-\\d{4}";
		assertEquals(false, phone.matches(phoneNumberPattern));

		String name = "First Name";
		String nameToken = "\\p{Upper}(\\p{Lower}+\\s?)";
		String namePattern = "(" + nameToken + "){2,3}";
		assertEquals(true, name.matches(namePattern));

		String character = "汉字";
		String characterPattern = "[u4e00-u9fa5]";
		assertEquals(false, character.matches(characterPattern));

		String fileNameSuffix = "image.gif";
		String suffixPattern = "(?<=\\.)(\\w+)$";
		assertEquals("image.tif", fileNameSuffix.replaceFirst(suffixPattern, "tif"));
	}

	// @Test(expected = StringIndexOutOfBoundsException.class)
	public void testReplaceEndBackSlash() {
		String str = "!abcd\\";
		String replacement = "backSlash\\";
		String regex = "!";
		log.info("str = {}, replacement = {}", str, replacement);
		String result = str.replaceAll(regex, Matcher.quoteReplacement(replacement));
		log.info("result = {}", result);
		// throw StringIndexOutOfBoundsException
		str.replaceFirst(regex, replacement);
	}

	/**
	 * ^为限制开头 ^java 条件限制为以Java为开头字符 $为限制结尾 java$ 条件限制为以java为结尾字符
	 */
	public void testBegin() {
		// 查找以Java开头,任意结尾的字符串
		String regex = "^Java.*";
		String str = "Java不是人";

		// first way
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		boolean b = matcher.matches();
		// 当条件满足时，将返回true，否则返回false
		assertEquals(true, b);

		// 2nd way
		assertEquals(true, str.matches(regex));
	}

	/**
	 * 以多条件分割字符串时
	 */
	public void testSplit() {
		String regex = "[, |]+";
		String str = "Java Hello World  Java,Hello,,World|Sun";

		// first way
		Pattern pattern = Pattern.compile(regex);
		String[] strs = pattern.split(str);
		printArray(strs);
		// 2nd way
		String[] array = str.split(regex);
		printArray(array);
	}

	/**
	 * 文字替换（字符）
	 */
	public void testReplace() {
		String regex = "正则表达式";
		String str = "正则表达式 Hello World,正则表达式 Hello World";
		String expected = "Java Hello World,正则表达式 Hello World";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		// 替换第一个符合正则的数据
		assertEquals(expected, matcher.replaceFirst("Java"));
		assertEquals(expected, str.replaceFirst(regex, "Java"));
	}

	/**
	 * 验证是否为邮箱地址
	 */
	public void testEmail() {
		String str = "cepon.line@yahoo.com.cn";
		Pattern pattern = Pattern.compile(Regex.EMAIL, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		assertEquals(true, matcher.matches());
		// not match . before @
		String regexNotMatchDot = "^[\\w\\d]+@[\\w\\d]+(\\.[\\w\\d]+)+$";
		assertEquals(false, str.matches(regexNotMatchDot));
		assertEquals(true, "ceponLIne@yahoo.com.cn".matches(regexNotMatchDot));
	}

	/**
	 * 去除html标记
	 */
	public void testRemoveHtmlTag() {
		String regex = "<.+?>";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern
				.matcher("<a href=\"index.html\">主页</a><tr><td> testTd</td></tr><tr/>");
		String string = matcher.replaceAll("");
		System.out.println(string);

		try {
			String content = IoUtils.getContent("C:\\cache\\cancel.html");
			String jsStr = content.replaceAll(regex, "").trim();
			log.info("jsStr = {}", jsStr);
			String notag = HtmlUtils.removeHtmlTag(content);
			log.info("notag = {}", notag);
		} catch (IOException e) {
			log.error("Exception in RegexTest.testRemoveHtmlTag()", e);
		}
	}

	/**
	 * 截取http://地址 截取url
	 */
	public void testUrl() {
		String regex = "(http://|https://){1}[\\w\\.\\-/:]+";
		Pattern pattern = Pattern.compile(regex);
		String str = "dsdsds<http://dsds//gfgffdfd>fdf";
		Matcher matcher = pattern.matcher(str);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			buffer.append(matcher.group());
			buffer.append(Constants.EOL);
			System.out.println(buffer.toString());
		}
	}

	/**
	 * 替换指定{}中文字
	 */
	public void testReplaceCharacter() {
		String str = "Java目前的发展史是由{0}年-{1}年";
		String[][] object = { new String[] { "\\{0\\}", "1995" }, new String[] { "\\{1\\}", "2007" } };
		System.out.println(replace(str, object));
	}

	public static String replace(final String sourceString, Object[] object) {
		String temp = sourceString;
		for (int i = 0; i < object.length; i++) {
			String[] result = (String[]) object[i];
			Pattern pattern = Pattern.compile(result[0]);
			Matcher matcher = pattern.matcher(temp);
			temp = matcher.replaceAll(result[1]);
		}
		return temp;
	}
}
