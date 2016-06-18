package doing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bean.User;
import junit.framework.Assert;

/**
 * @author Shang Pu
 * @version Date: Apr 15, 2012 11:08:53 AM
 */
public class Practice extends TestBase {
	private final static Logger log = LoggerFactory.getLogger(Practice.class);
	String code;

	public static void main(String[] args) {
		// print where java class is loaded from
        ClassLoader loader = Test.class.getClassLoader();
        System.out.println(loader.getResource("org/slf4j/spi/LocationAwareLogger.class"));
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
		for (int i = 0; i< 4; i++) {
			users.add(new User(Long.valueOf(i), "name" + i, i+1));
		}
		
		log.debug("Returning user ids: {}", collect(users, "id"));
		log.debug("Returning user names: {}", collect(users, "name"));
	}

	/**
	 * The type of the Expression must be char, byte, short, int, Character,
	 * Byte, Short, Integer, String, or an enum type (§8.9), or a compile-time
	 * error occurs.
	 * The switch Statement 
	 * http://docs.oracle.com/javase/specs/jls/se7/html/jls-14.html#jls-14.11
	 * 
	 * <a href=
	 * "http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.28"
	 * >Constant Expressions/a>
	 */
	@Test
	public void testSwitch() {
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
