package doing;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Apr 15, 2012 11:08:53 AM
 */
public class Practice extends TestBase {
	private final static Logger log = LoggerFactory.getLogger(Practice.class);
	String code;

	// TODO: take a look PropertyDescriptor
	public void testStringUtil() {
		Integer arg0 = new Integer(1);
		boolean isEmpty = org.springframework.util.StringUtils.isEmpty(arg0);
		Assert.assertFalse(isEmpty);
	}

	@Test
	public void testMap() {
		Map<String, Integer> stat = new HashMap<>();
		stat.put("CREATED", 0);
		String status = "CREATED";
		stat.put(status, stat.get(status) + 1);
		log.info("stat = {}", stat);
//		status = "UPDATED";
//		stat.put(status, stat.get(status) + 1);
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

	public void testSwitch() {
		Integer i = new Integer(1) + new Integer(2);
		switch (i) {
		case 3:
			System.out.println("three");
			break;
		default:
			System.out.println("other");
			break;
		}
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
