package demo.javabasics.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 因静态变量放置顺序导致的类加载失败的例子. 输出为:
 * 
 * <pre>
 * in ClassLoaderOrderTest()
 * Exception in thread "main" java.lang.ExceptionInInitializerError
 * Caused by: java.lang.NullPointerException
 * at com.tcl.recipevideohunter.other.ClassLoaderOrderTest.<init>(ClassLoaderOrderTest.java:23)
 * at com.tcl.recipevideohunter.other.ClassLoaderOrderTest.<clinit>(ClassLoaderOrderTest.java:7)
 * </pre>
 * 
 * 解决: 静态变量logger放在第一行即可.
 */
public class ClassLoaderOrderTest {
	private static ClassLoaderOrderTest test = new ClassLoaderOrderTest();
	private Configuration config = new Configuration();

	public static ClassLoaderOrderTest getInstance() {
		return test;
	}

	private static Logger logger = LoggerFactory.getLogger(ClassLoaderOrderTest.class);

	private ClassLoaderOrderTest() {
		try {
			System.out.println("in ClassLoaderOrderTest()");
			config.setSomeProperty("aaa", "111");
			logger.info("init ok.");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public static void main(String[] args) {
		ClassLoaderOrderTest.getInstance();
	}

	class Configuration {
		public void setSomeProperty(String key, String val) {

		}
	}
}
