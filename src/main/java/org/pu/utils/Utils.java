package org.pu.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Apr 20, 2012 12:59:20 PM
 */
public class Utils {
	protected final static Logger log = LoggerFactory.getLogger(Utils.class);
	public static final String UTF8 = "UTF-8";

	public interface ExecutionTimer {
		void execute();
	}

	/**
	 * get the stack trace of an exception as a String
	 */
	public static String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		return writer.toString();
	}

	public static String digMessage(Throwable t) {
		String msg = t.getMessage();
		if (msg == null && t.getCause() != null) {
			return digMessage(t.getCause());
		}
		return msg;
	}

	/**
	 * 测试CallBack接口的execute方法使用时间
	 */
	public static long getExecuteTime(ExecutionTimer timer) {
		// long start = new Date().getTime();
		// long start = Calendar.getInstance().getTimeInMillis();;
		// long start = System.nanoTime();
		long start = System.currentTimeMillis();
		timer.execute();
		long end = System.currentTimeMillis();
		long duration = end - start;
		log.info("TimeConsumeInterface executed in {} ms", duration);
		return duration;
	}

	public static String getHumanReadableFileSize(int fileSize) {
		String displaySize = "";
		int kSize = 2 << 9;
		int mSize = 2 << 19;
		if (fileSize > mSize) {
			double realMSize = (double) fileSize / (double) mSize;
			realMSize = ((int) (realMSize * 100)) / (double) 100;
			displaySize = realMSize + " MB";
		} else if (fileSize > kSize) {
			double realKSize = (double) fileSize / (double) kSize;
			realKSize = ((int) (realKSize * 100)) / (double) 100;
			displaySize = realKSize + " KB";
		} else {
			displaySize = fileSize + " Bytes";
		}
		return displaySize;
	}

	/**
	 * check if there is duplicate class in current loaded class and located in more than one jar file
	 */
	public static boolean isDuplicate(Class<?> cls) throws IOException {
		return isDuplicate(cls.getName().replace('.', '/') + ".class");
	}

	public static boolean isDuplicate(String path) throws IOException {
		log.debug("Enter isDuplicate({})", path);
		// 在ClassPath搜文件
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(path);
		Set<String> files = new HashSet<String>();
		// find all locations of a Java class
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			if (url != null) {
				String file = url.getFile();
				if (file != null && file.length() > 0) {
					files.add(file);
				}
			}
		}
		boolean result = false;
		// 如果有多个，就表示重复
		if (files.size() > 1) {
			log.info("Duplicate class {} in {} jar {}", new Object[] { path, files.size(), files });
			result = true;
		} else {
			log.info("it is not duplicate");
		}
		log.debug("Exit isDuplicate() isDuplicate = {}", result);
		return result;
	}

	/**
	 * get the path of klass
	 * <p>
	 * 确定我当前所加载的类是位于哪个路径或者存在于哪个jar包当中 但是有一个限制经由系统类加载器所加载的类不能使用该方法来获取路径，假如你对
	 * String类做上面的测试就会发生空指针异常，因为所得到的CodeSource为空.
	 * 工具的缺点是ProtectionDomain是里没记录某些类的路径，例如rt.jar里的在这个工具的输出就是null。Bootstrap class loader加载的类都会是这样。
	 * <p>
	 * 你可以在程序启动的时候使用-verbose来查看系统类的装载信息，使用方式有如: java -verbose MyApp
	 */
	public static String getPath(Class<?> klass) {
		ProtectionDomain protectionDomain = klass.getProtectionDomain();
		if (protectionDomain == null) return null;

		CodeSource codesource = protectionDomain.getCodeSource();
		if (codesource == null) return null;

		URL location = codesource.getLocation();
		if (location == null) return null;
		return location.toString();
	}

	public static String getClassLoader(Class<?> klass) {
		ClassLoader loader = klass.getClassLoader();
		return loader != null ? getClassName(loader.getClass()) + " @ " + loader.getParent()
				: "<bootstrap>";
	}

	/**
	 * get class name
	 */
	public static String getClassName(Class<?> klass) {
		return klass != null ? klass.getName().replace('/', '.') : null;
	}

	/**
	 * get jar file location with the class
	 */
	public static String getJarPath(Class<?> clazz) {
		String jarPath = clazz.getProtectionDomain().getCodeSource().toString();
		log.info("jarPath = {} in method getJarPath()", jarPath);
		return jarPath;
	}

	/**
	 * 获取Classpath中所有jar的路径
	 */
	public static List<String> getAllJarPaths() {
		List<String> jarPathList = new ArrayList<String>();
		try {
			// Enumeration<URL> urls =
			// Utils.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
			Enumeration<URL> urls = ClassLoader.getSystemClassLoader().getResources(
					"META-INF/MANIFEST.MF");
			while (urls.hasMoreElements()) {
				URL url = (URL) urls.nextElement();
				String path = url.toString();
				if (path.contains("jar:file:")) {
					path = path.replace("jar:file:/", "").replace("!/META-INF/MANIFEST.MF", "");
					jarPathList.add(path);
				}
			}
		} catch (IOException e) {
			log.error("Exception in Utils.getAllJarPaths()", e);
		}
		return jarPathList;
	}

	/**
	 * 获取Classpath中所有jar
	 */
	public static List<String> getAllJars() {
		List<String> jarPathList = getAllJarPaths();
		List<String> jarList = new ArrayList<String>();
		for (String path : jarPathList) {
			path = path.substring(path.lastIndexOf('/') + 1);
			jarList.add(path);
		}
		return jarList;
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao extends
	 * HibernateDao&ltUser&gt
	 * 
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	public static Class<?> getSuperClassGenricType(final Class<?> clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. 如public UserDao extends
	 * HibernateDao<User,Long>
	 * 
	 * @param klass The class to introspect
	 * @param index the Index of the generic declaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
	public static Class<?> getSuperClassGenricType(final Class<?> klass, final int index) {
		Type genType = klass.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			log.warn("{}'s superclass not ParameterizedType", klass.getSimpleName());
			log.debug("superClassGenricType = {}", Object.class);
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			log.warn("Index: {}, Size of {}'s Parameterized Type: {}",
					new Object[] { index, klass.getSimpleName(), params.length });
			log.debug("superClassGenricType = {}", Object.class);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			log.warn("{} not set the actual class on superclass generic parameter", klass.getSimpleName());
			log.debug("superClassGenricType = {}", Object.class);
			return Object.class;
		}
		log.debug("superClassGenricType = {}", params[index]);
		return (Class<?>) params[index];
	}

	public static String getVersion(Class<?> klass, String defaultVersion) {
		String version = "";
		final Pattern VERSION = Pattern.compile("([0-9][0-9\\.\\-]*)\\.jar");
		try {
			// 首先查找MANIFEST.MF规范中的版本号
			Package klassPackage = klass.getPackage();
			version = klassPackage.getImplementationVersion();
			if (version == null || version.length() == 0) {
				version = klassPackage.getSpecificationVersion();
			}
			if (version == null || version.length() == 0) {
				// 如果MANIFEST.MF规范中没有版本号，基于jar包名获取版本号
				String file = klass.getProtectionDomain().getCodeSource().getLocation().getFile();
				if (file != null && file.length() > 0 && file.endsWith(".jar")) {
					Matcher matcher = VERSION.matcher(file);
					while (matcher.find() && matcher.groupCount() > 0) {
						version = matcher.group(1);
					}
				}
			}
			// 返回版本号，如果为空返回缺省版本号
			log.info("the version of {} is {}", klass, version);
			return version == null || version.length() == 0 ? defaultVersion : version;
		} catch (Throwable e) { // 防御性容错
			// 忽略异常，返回缺省版本号
			log.error(e.getMessage(), e);
			return defaultVersion;
		}
	}

	public static List<?> deepClone(List<?> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);

		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		return (List<?>) in.readObject();
	}

	public static Object deepClone(Object src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(byteOut);

		o.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		return in.readObject();
	}

	public static void displayMsg(String msg, boolean openNotepad) {
		log.info(msg);
		if (openNotepad) {
			try {
				Runtime.getRuntime().exec("notepad");
			} catch (IOException e) {
				log.error("Exception in Utils.displayMsg()", e);
			}
		}
		JOptionPane.showMessageDialog(null, msg);
	}

	/**
	 * return true if sleep
	 * <p>
	 * return false if throw exception
	 */
	public static boolean sleep(long millis) {
		try {
			Thread.sleep(millis);
			return true;
		} catch (InterruptedException e) {
			log.error("InterruptedException in Utils.sleep()", e);
			return false;
		}
	}
}
