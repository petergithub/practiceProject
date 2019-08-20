package doing;

import bean.User;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import org.junit.Assert;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * @author Shang Pu
 */
public class Practice extends TestBase {
	private final static Logger log = LoggerFactory.getLogger(Practice.class);


	@Test
	public void longLength() {
		Long timestamp = System.currentTimeMillis();
		Assert.assertTrue(timestamp.toString().length() == 13);

		Long timestamp10 = timestamp / 1000;
		Assert.assertTrue(timestamp10.toString().length() == 10);
		Assert.assertTrue(timestamp2TenDigits(timestamp).toString().length() == 10);
		Assert.assertTrue(timestamp2TenDigits(timestamp10).toString().length() == 10);
	}

	private Long timestamp2TenDigits(Long lastReportTime) {
		int TIMESTAMP_WITH_MILLIS = 13;
		int MILLS_TO_SECOND = 1000;

		if (lastReportTime.toString().length() == TIMESTAMP_WITH_MILLIS) {
			lastReportTime = lastReportTime / MILLS_TO_SECOND;
		}
		return lastReportTime;
	}

    @Test
    public void doublePrecise() {
        double weight= 50.3;
        double target = 50.2999992;
        double change = -1.2;
        
        double percent = (weight - (target - change))/change;
        log.info("percent: {}", percent);
    }
    
	@Test
	public void base64() {
        final String text = "Base64 finally in Java 8!";
         
        final String encoded = Base64
            .getEncoder()
            .encodeToString( text.getBytes( StandardCharsets.UTF_8 ) );
        System.out.println( encoded );
         
        final String decoded = new String( 
            Base64.getDecoder().decode( encoded ),
            StandardCharsets.UTF_8 );
        System.out.println( decoded );
    }
	
	@Test
	public void sortMapByValue() {
	    Map<String, Integer> map = ImmutableMap.of("a", 1, "b", 2, "c", 3);
	    log.info("map: {}", map);
	    Map<String, Integer> sortedMap = sortMapByValue1(map);
	    log.info("sortedMap: {}", sortedMap);

        map = new HashMap<String, Integer>();
        map.put("b", 30);
        map.put("a", 10);
        map.put("c", 50);
        
        log.info("map: {}", map);
        sortedMap = sortMapByValue2(map);
        log.info("sortedMap: {}", sortedMap);
        
        log.info("combineMap: {}", combineParamsTogether(map));
	}
	

    private String combineParamsTogether(Map<String, Integer> map) {
        Set<String> names = map.keySet();
        // remove parameter sign

        StringBuilder result = new StringBuilder();

        // 使用TreeMap,默认按照字母排序
        TreeMap<String, String> parameter = new TreeMap<>();

        if (parameter.size() > 0) {
            List<String> comboMap = new ArrayList<String>();
            for (Entry<String, String> entry : parameter.entrySet()) {
                String comboResult = entry.getKey() + entry.getValue();
                comboMap.add(comboResult);
            }
            Joiner joiner = Joiner.on("&");
            result.append(joiner.join(comboMap));
        }
        return result.toString();
    }
	

    private static Map<String, Integer> sortMapByValue1(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> mapList = new ArrayList<Map.Entry<String, Integer>>(
                map.entrySet());
        Collections.sort(mapList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                int flag = o2.getValue() - o1.getValue();
                if (flag == 0){
                    flag = o1.getKey().compareTo(o2.getKey());
                }
                return flag;
            }
        });
        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        /*for(Map.Entry<String, Integer> entry:mapList){
            result.put(entry.getKey(), entry.getValue());
        }*/
//        for (int i = 0; i < mapList.size(); i++) {
//            Map.Entry<String, Integer> entry = mapList.get(i);
//            if (i > 0) { // 只取一位
//                break;
//            }
//            result.put(entry.getKey(), entry.getValue());
//        }
        return result;
    }
    
    
    public TreeMap<String, Integer> sortMapByValue2(Map<String, Integer> map){
        Comparator<String> comparator = new ValueComparator(map);
        //TreeMap is a map sorted by its keys. 
        //The comparator is used to sort the TreeMap by keys. 
        TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
        result.putAll(map);
        return result;
    }
    
 // a comparator that compares Strings
    class ValueComparator implements Comparator<String>{
     
        Map<String, Integer> map = new HashMap<String, Integer>();
     
        public ValueComparator(Map<String, Integer> map){
            this.map.putAll(map);
        }
     
        @Override
        public int compare(String s1, String s2) {
            if(map.get(s1) >= map.get(s2)){
                return -1;
            }else{
                return 1;
            }   
        }
    }
	@Test
	public void maxMemory() {
	    long MaxDirectMemorySize = Runtime.getRuntime().maxMemory();
	    log.info("MaxDirectMemorySize: {}", MaxDirectMemorySize);
	    
	    //Direct Memory是受GC控制的
	    //这段代码的执行会在堆外占用1k的内存，Java堆内只会占用一个对象的指针引用的大小，堆外的这1k的空间只有当bb对象被回收时，才会被回收，
	    // 这里会发现一个明显的不对称现象，就是堆外可能占用了很多，而堆内没占用多少，导致还没触发GC，
	    // 那就很容易出现Direct Memory造成物理内存耗光。
	    ByteBuffer bb = ByteBuffer.allocateDirect(1024);
	}
	
    @Test
    public void logException() {
        log.error("Exception in Practice.logException() para1: {} para2: {} para3: {}", 1,2,3, new Exception("test exception"));
    }
    
	@Test
	public void test() {
	    Assert.assertFalse(diffAbs(1.6D, 0.1D) > 1.5);
	}

    public static Double diffAbs(Double foo, Double bar) {
        return Math.abs(foo - bar);
    }
	
    @Test
    public void practiceGeoIP() throws IOException, GeoIp2Exception {
     // A File object pointing to your GeoIP2 or GeoLite2 database
        String cityDb = "/data/GeoLite2-City/GeoLite2-City.mmdb";
        
        String path = Practice.class.getClassLoader().getResource("log4j.dtd").getPath();
        log.info("path: {}", path);
        File pathFile = new File(path);
        log.info("pathFile: {}", pathFile);
        
        File database = new File(cityDb);

        // This creates the DatabaseReader object. To improve performance, reuse
        // the object across lookups. The object is thread-safe.
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        InetAddress ipAddress = InetAddress.getByName("47.91.92.62");

        // Replace "city" with the appropriate method for your database, e.g.,
        // "country".
//        CountryResponse countryResponse = reader.country(ipAddress);
//        log.info("countryResponse: {}", countryResponse);
        
        CityResponse response = reader.city(ipAddress);

        Country country = response.getCountry();
        System.out.println(country.getIsoCode());            // 'US'
        System.out.println(country.getName());               // 'United States'
        System.out.println(country.getNames().get("zh-CN")); // '美国'

        Subdivision subdivision = response.getMostSpecificSubdivision();
        System.out.println(subdivision.getName());    // 'Minnesota'
        System.out.println(subdivision.getIsoCode()); // 'MN'

        City city = response.getCity();
        System.out.println(city.getName()); // 'Minneapolis'

        Postal postal = response.getPostal();
        System.out.println(postal.getCode()); // '55455'

        Location location = response.getLocation();
        System.out.println(location.getLatitude());  // 44.9733
        System.out.println(location.getLongitude()); // -93.2323
    }
    
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

	public static void main2(String[] args) {
	    long MaxDirectMemorySize = Runtime.getRuntime().maxMemory();
	    System.out.println("MaxDirectMemorySize: " + MaxDirectMemorySize);
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

	@Test
	public void execommand() throws IOException {
		String cmd = "D:\\sp\\work\\script\\testBat.bat";
		cmd = "cmd /C start cmd /K bash --login -i";
		cmd = "explorer /select, /e,D:\\sudoers.txt";
		
		cmd = "open -a Finder /Users/pu/sp/Nustore\\ Files/Dropbox/base/";
		log.info("cmd {}", cmd);
		
		Runtime.getRuntime().exec(cmd, null, null);
//		Runtime.getRuntime().exec(cmd, null, new File("D:\\sudoers.txt"));
//		String result = org.pu.utils.Utils.exeCmd(cmd, "gbk");
//		log.info("result = {}", result);
	}
	
	@Test
	public void runtimeExec() throws IOException {
	 // note no leading forward slash
	    String fileRelationPath = "Users/pu/sp/Nustore Files/Dropbox/base/";
	    File workingDir = new File("/");
	    String[] cmd = new String[]{"open", "-R", fileRelationPath};

//	    Runtime.getRuntime().exec(cmd, null, workingDir);
	    
	 // Note leading forward slash:
	    File file = new File("/Users/pu/sp/Nustore Files/Dropbox/base/") ;
	    workingDir = file.getParentFile();
	    String filename = file.getName();

	    cmd = new String[] {"open", "-R", filename} ;
	    Runtime.getRuntime().exec(cmd, null, workingDir);
	    
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
