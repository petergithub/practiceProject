package doing;

import com.alibaba.fastjson.JSONObject;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bean.User;
import junit.framework.Assert;

/**
 * @author Shang Pu
 */
public class Practice extends TestBase {
	private final static Logger log = LoggerFactory.getLogger(Practice.class);

	@Test
	public void practiceSensitive() {
		List<String> list = new ArrayList<>();
		list.add("微信");
		list.add("我微信准备删除账号");
		list.add("我准备删除账号");
		Map<String, Object> sensitiveWordMap = initSensitiveWord(list);

		checkSensitiveWord("加我微信教你减肥", sensitiveWordMap);
	}

	public int checkSensitiveWord(String word, Map<String, Object> sensitiveWordMap) {
		log.debug("Enter checkSensitiveWord word: {} sensitiveWordMap: {}", word, sensitiveWordMap);
		int count = word.codePointCount(0, word.length());
		int matchFlag = 0;
		Map<String, Object> nowMap = sensitiveWordMap;
		for (int i = 0; i < count; i++) {
			String key = new String(Character.toChars(word.codePointAt(i))).toLowerCase();
			log.debug("敏感词key " + key);
			nowMap = (Map<String, Object>) nowMap.get(key);
			log.debug("敏感词key库 " + JSONObject.toJSONString(nowMap));
			if (nowMap != null) {
				matchFlag++;
				if ("y".equals((String) nowMap.get("isEnd"))) { //如果为最后一个匹配规则,结束循环，返回匹配标识数  
					log.debug("敏感词匹配成功 " + word + "匹配度 " + matchFlag);
					break;
				}
			} else {
				if (matchFlag >= 1) {
					// i = i - matchFlag;
					i--;
				}
				nowMap = sensitiveWordMap;
				matchFlag = 0;
			}
		}
		if (matchFlag < 2) {
			matchFlag = 0;
			System.out.println(word + " is good");
		} else {
			System.out.println(word + " is not allowed");
		}
		log.debug("Exit checkSensitiveWord matchFlag: {} nowMap: {}", matchFlag, nowMap);
		return matchFlag;
	}


	public Map<String, Object> initSensitiveWord(List<String> wList) {
		log.debug("Enter initSensitiveWord wList: {} wList: {}", wList, wList);
		Map<String, Object> sensitiveWordMap = new HashMap<String, Object>(wList.size());
		Map<String, Object> temMap = null;
		Map<String, Object> nowMap = null;
		for (String w : wList) {
			nowMap = sensitiveWordMap;
			int count = w.codePointCount(0, w.length());
			for (int i = 0; i < count; i++) {
				String key = new String(Character.toChars(w.codePointAt(i)));
				Object res = nowMap.get(key);
				if (res == null) {
					temMap = new HashMap<String, Object>();
					temMap.put("isEnd", "n");
					nowMap.put(key, temMap);
					nowMap = temMap;
				} else {
					nowMap = (Map<String, Object>) res;
				}
				if (i == count - 1) {
					temMap.put("isEnd", "y");
				}
			}
		}
		log.debug("Exit initSensitiveWord sensitiveWordMap: {} nowMap: {}", sensitiveWordMap, nowMap);
		return sensitiveWordMap;
	}

	@Test
	public void practiceBoolean() {
		boolean needLoopback = false;
		Assert.assertFalse(needLoopback);
		needLoopback |= true;
		Assert.assertTrue(needLoopback);
	}

	@Test
	public void practiceMap() {
		String word = "微信,分析";
		log.info("list: {}", splitString(word));
		word = "微信，分析";
		log.info("list: {}", splitString(word));
	}

	private List<String> splitString(String word) {
		int count = word.codePointCount(0, word.length());
		List<String> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			String key = new String(Character.toChars(word.codePointAt(i))).toLowerCase();
			list.add(key);
		}
		return list;
	}

	/**
	 * Set Default Locale 
	 * 1. jvm arguments
	 * java -Duser.country=US -Duser.language=en
	 * java -Duser.country=CN -Duser.language=zh
	 * 
	 * 2. setDefault(Locale aLocale)
	 */
	@Test
	public void locale() {
		log.info("user.country: {}", System.getProperty("user.country"));
		log.info("user.region: {}", System.getProperty("user.region"));
		log.info("defaultLocale: {}", Locale.getDefault());

		Locale.setDefault(Locale.CANADA);
		log.info("defaultLocale: {}", Locale.getDefault());

		Locale aLocale = Locale.JAPAN;
		System.out.println("Locale: " + aLocale);
		System.out.println("ISO 2 letter: " + aLocale.getLanguage());
		System.out.println("ISO 3 letter: " + aLocale.getISO3Language());
	}

	public void exeCurrency() throws IOException {
		Currency currency = Currency.getInstance("CNY");
		Assert.assertEquals("CNY", currency.getCurrencyCode());
		Assert.assertEquals("CNY", currency.getSymbol());
		Assert.assertEquals("￥", currency.getSymbol(Locale.CHINA));
		Assert.assertEquals("Chinese Yuan", currency.getDisplayName(Locale.US));
		Assert.assertEquals("Chinese Yuan", currency.getDisplayName(Locale.CHINESE));
		Assert.assertEquals("人民币", currency.getDisplayName(Locale.CHINA));

		currency = Currency.getInstance(Locale.US);
		Assert.assertEquals("USD", currency.getCurrencyCode());
		Assert.assertEquals("$", currency.getSymbol());
		Assert.assertEquals("$", currency.getSymbol(Locale.US));

		currency = Currency.getInstance(Locale.FRANCE);
		Assert.assertEquals("EUR", currency.getCurrencyCode());
		Assert.assertEquals("EUR", currency.getSymbol());
		Assert.assertEquals("€", currency.getSymbol(Locale.FRANCE));

		Locale locale = new Locale("Chinese");
		log.info("locale = {}", locale);
		log.info("locale.getCountry() = {}", locale.getCountry());
		log.info("locale.getDisplayCountry() = {}", locale.getDisplayCountry());
		log.info("locale.getDisplayLanguage() = {}", locale.getDisplayLanguage());
	}
	
	@Test
	public void stackTrace() {
		int num = getLineNumber();
		log.info("num: {}", num);
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		printArray(stackTrace);

		String fullClassName = stackTrace[2].getClassName();
		log.info("fullClassName: {}", fullClassName);
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		log.info("className: {}", className);
		String methodName = stackTrace[2].getMethodName();
		int lineNumber = stackTrace[2].getLineNumber();

		log.info("methodName: {} lineNumber: {}", methodName, lineNumber);
	}

	/**
	 * Get the current line number.
	 * 
	 * @return int - Current line number.
	 */
	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

	@Test
	public void testEquals() {
		@SuppressWarnings("unused")
		int sid = 0;
		Assert.assertEquals(10, (sid = 10));
		Assert.assertEquals(1, (sid = 1));
		Assert.assertEquals(-10, (sid = -10));
		Assert.assertEquals(-1, (sid = -1));
	}

	@Test
	public void testTimeUnit() {
		long daySeconds = TimeUnit.SECONDS.convert(1, TimeUnit.DAYS);
		Assert.assertEquals(86400, daySeconds);
		
		long seconds = TimeUnit.SECONDS.convert(1, TimeUnit.HOURS);
		Assert.assertEquals(3600, seconds);
		
		long minutes = TimeUnit.MINUTES.convert(1, TimeUnit.HOURS);
		Assert.assertEquals(60, minutes);
	}

	public static void main(String[] args) {
		// print where java class is loaded from
		ClassLoader loader = Test.class.getClassLoader();
		System.out.println(loader.getResource("org/slf4j/spi/LocationAwareLogger.class"));

		int x = 4;
		cInt(x);
		System.out.println(x);

		Integer y = new Integer(4);
		cInt(y);
		System.out.println(y);

		String z = new String("4");
		cStringObj(z);
		System.out.println(z);
	}

	public static void cInt(int x) {
		x = 3;
	}

	public static void cIntegerObj(Integer y) {
		y = new Integer(3);
	}

	public static void cStringObj(String y) {
		y = new String("3");
	}

	// TODO: take a look PropertyDescriptor
	public void testStringUtil() {
		Integer arg0 = new Integer(1);
		boolean isEmpty = org.springframework.util.StringUtils.isEmpty(arg0);
		Assert.assertFalse(isEmpty);
	}

	@Test
	public void testCollect() {
		List<User> users = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			users.add(new User(Long.valueOf(i), "name" + i, i + 1));
		}

		log.debug("Returning user ids: {}", collect(users, "id"));
		log.debug("Returning user names: {}", collect(users, "name"));
	}

	@Test
	public void switch2() {
		int key = 1;
		final int one = 1, two = 2, three = 3;
		switch (key) {
		case 0:
			log.info("case 0");
			break;
		case one:
		case three:
			log.info("case 1 or 3");
			break;
		case two:
			log.info("case 2");
			break;
		default:
			log.info("case default");
			break;
		}

		String keyString = "string1";
		final String stringOne = "string1", stringTwo = "string2";
		switch (keyString) {
		case "string0":
			log.info("case string0");
			break;
		case stringOne:
			log.info("case string1");
			break;
		case stringTwo:
			log.info("case string2");
			break;
		default:
			log.info("case default");
			break;
		}
	}

	/**
	 * The type of the Expression must be char, byte, short, int, Character,
	 * Byte, Short, Integer, String, or an enum type (§8.9), or a compile-time
	 * error occurs. The switch Statement
	 * http://docs.oracle.com/javase/specs/jls/se7/html/jls-14.html#jls-14.11
	 * 
	 * <a href=
	 * "http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.28"
	 * >Constant Expressions/a>
	 */
	@Test
	public void switch1() {
		final String inCase = "str";
		String str = "str";
		switch (str) {
		case inCase:
			log.info("str[{}]", str);
			break;
		default:
			log.info("nothing");
		}

		final int three = 3;
		@SuppressWarnings("unused")
		final Integer four = 4;
		Integer i = new Integer(1) + new Integer(2);
		switch (i) {
		// four cannot be used here because final static Integer not considered
		// a constant when runtime:
		//
		// The case in the switch statements should be constants at compile
		// time. The command
		//
		// final int b=2
		//
		// assigns the value of 2 to b, right at the compile time. But the
		// following command assigns the value of 2 to b at Runtime.
		//
		// final int b;
		// b = 2;
		//
		// Thus, the compiler complains, when it can't find a constant in one of
		// the cases of the switch statement.

		// http://stackoverflow.com/questions/16255270/final-variable-case-in-switch-statement/16255479#16255479
		case three:
			System.out.println("three");
			break;
		case 4:// four
			System.out.println("three");
			break;
		default:
			System.out.println("other");
			break;
		}
	}

	public void testMap() {
		Map<String, Integer> stat = new HashMap<>();
		stat.put("CREATED", 0);
		String status = "CREATED";
		stat.put(status, stat.get(status) + 1);
		log.info("stat = {}", stat);
		// status = "UPDATED";
		// stat.put(status, stat.get(status) + 1);
		log.info("stat = {}", stat);

		stat.clear();
		log.info("stat = {}", stat);
	}

	public void testMutilpleParams() {
		String[] array = { "a", "b" };
		mutilpleParams(array);
		mutilpleParams("aa", "bb");
		mutilpleParams("aaa");
	}

	public void mutilpleParams(String... strings) {
		log.info(" = {}", Arrays.asList(strings));
	}

	public void execommand() throws IOException {
		String cmd = "D:\\sp\\work\\script\\testBat.bat";
		cmd = "cmd /C start cmd /K bash --login -i";
		cmd = "explorer /select, /e,D:\\sudoers.txt";
		Runtime.getRuntime().exec(cmd);
		Runtime.getRuntime().exec(cmd, null, new File("D:\\sudoers.txt"));
		String result = org.pu.utils.Utils.exeCmd(cmd, "gbk");
		log.info("result = {}", result);
	}

	public void testInteger() {
		int num = 6553800;
		log.info("6553800 int = {}", num);
		Integer one = 1;
		Integer oneInit = new Integer(1);
		Integer oneInitAnother = new Integer(1);
		Assert.assertTrue(1 == one);
		Assert.assertTrue(1 == oneInit);
		Assert.assertTrue(oneInit.equals(1));
		Assert.assertTrue(oneInit.equals(oneInitAnother));
		Assert.assertFalse(oneInit == oneInitAnother);// object compare
	}

	public void testWait() throws InterruptedException {
		this.wait();
	}

	public void testGetPackage() {
		Package a = getClass().getPackage();
		log.info("a = {}", a);
	}

	public void testlog() {
		log.error("ACL {} doesn't exist", "aclName");
		// /C:/sp/Dropbox/base/testProject/target/test-classes/
		log.info("path = {}", getClass().getResource("/").getPath());

		getClass().getResourceAsStream("");
	}

	public void testEncode() {
		String code = System.getProperty("sun.io.unicode.encoding");
		log.info("code = {}", code);
	}

	public void testRename() {
		List<String> names = Arrays.asList("a", "a", "b", "c", "a", "d", "b", "b");
		log.info("names = {}", names);
		log.info("renames = {}", rename(names));
	}

	private List<String> rename(List<String> names) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			if (!map.containsKey(name)) {
				map.put(name, 1);
				String newName = name + "_" + 1;
				names.set(i, newName);
			} else {
				int count = map.get(name);
				int value = count + 1;
				map.put(name, value);
				String newName = name + "_" + value;
				names.set(i, newName);
			}
		}
		return names;
	}

}
