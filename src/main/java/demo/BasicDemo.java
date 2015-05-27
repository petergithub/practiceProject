package demo;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import doing.TestClass;


/**
 * @author Shang Pu
 */
public class BasicDemo {
	private static final Logger log = LoggerFactory.getLogger(BasicDemo.class);

	@org.junit.Test
	public void testCallJs() {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		// JavaScript code in a String
		String script = "function hello(name) { print('Hello, ' + name); }";
		try {
			// evaluate script
			engine.eval(script);

			// javax.script.Invocable is an optional interface.
			// Check whether your script engine implements or not!
			// Note that the JavaScript engine implements Invocable interface.
			Invocable inv = (Invocable) engine;

			// invoke the global function named "hello"
			inv.invokeFunction("hello", "Scripting!!");
		} catch (ScriptException e) {
			log.error("Exception in TestClass.testCallJs1()", e);
		} catch (NoSuchMethodException e) {
			log.error("Exception in TestClass.testCallJs1()", e);
		}
	}

	public void testLoadResource() {
		// String resource = "log4j.properties"; // Resource In Class Folder
		// String resource =
		// "C:\\sp\\work\\.m2\\repository\\javax\\mail\\mail\\1.4.5\\mail-1.4.5.jar";
		String resource = "mail-1.4.5.jar";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream stream = classLoader.getResourceAsStream(resource);
		log.info("with ContextClassLoader stream = {}", stream);

		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		stream = systemClassLoader.getResourceAsStream(resource);
		log.info("with systemClassLoader stream = {}", stream);
	}

	/**
	 * When converting text file from one encoding to another, you need to make sure that all
	 * characters in the text file are valid characters in the character set of the output encoding.
	 * <p>
	 * This program allows you to convert a text file in one encoding to another file in a different
	 * encoding.
	 */
	public void testEncodingConverter() {
		String[] args = {};
		String inFile = args[0];
		String inCharsetName = args[1];
		String outFile = args[2];
		String outCharsetName = args[3];
		try {
			InputStreamReader in = new InputStreamReader(new FileInputStream(inFile), inCharsetName);
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(outFile), outCharsetName);
			int c = in.read();
			int n = 0;
			while (c != -1) {
				out.write(c);
				n++;
				c = in.read();
			}
			in.close();
			out.close();
			System.out.println("Number of characters: " + n);
			System.out.println("Number of input bytes: " + (new File(inFile)).length());
			System.out.println("Number of output bytes: " + (new File(outFile)).length());
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	public void testCharset() {
		Map<String, Charset> charsets = Charset.availableCharsets();
		for (String name : charsets.keySet()) {
			log.info(name);
		}
	}

	public void testLocale() {
		// get the default locale
		Locale l = Locale.getDefault();
		System.out.println("   Language, Country, Variant, Name");
		System.out.println("");
		System.out.println("Default locale: ");
		System.out.println("   " + l.getLanguage() + ", " + l.getCountry() + ", " + ", "
				+ l.getVariant() + ", " + l.getDisplayName());
		// get a predefined locale
		l = Locale.CANADA_FRENCH;
		System.out.println("A predefined locale - Locale.CANADA_FRENCH:");
		System.out.println("   " + l.getLanguage() + ", " + l.getCountry() + ", " + ", "
				+ l.getVariant() + ", " + l.getDisplayName());
		// define a new locale
		l = new Locale("en", "CN");
		System.out.println("User defined locale -Locale(\"en\",\"CN\"):");
		System.out.println("   " + l.getLanguage() + ", " + l.getCountry() + ", " + ", "
				+ l.getVariant() + ", " + l.getDisplayName());
		// define another new locale
		l = new Locale("ll", "CC");
		System.out.println("User defined locale -Locale(\"ll\",\"CC\"):");
		System.out.println("   " + l.getLanguage() + ", " + l.getCountry() + ", " + ", "
				+ l.getVariant() + ", " + l.getDisplayName());
		// get the supported locales
		Locale[] s = Locale.getAvailableLocales();
		System.out.println("Supported locales: ");
		for (int i = 0; i < s.length; i++) {
			System.out.println("   " + s[i].getLanguage() + ", " + s[i].getCountry() + ", "
					+ s[i].getVariant() + ", " + s[i].getDisplayName());
		}
	}

	public void testThreadGroupTree() {
		Thread t = Thread.currentThread();
		ThreadGroup group = t.getThreadGroup();
		group = group.getParent();
		printInfo(group, "");
		group.list();
	}

	private static void printInfo(ThreadGroup group, String str) {
		System.out.println(str + "ThreadGroup name = " + group.getName());
		System.out.println(str + "   Has parent thread group = " + (group.getParent() != null));
		int n = group.activeCount();
		System.out.println(str + "   # of active threads = " + n);
		Thread[] l = new Thread[n];
		n = group.enumerate(l, false);
		for (int i = 0; i < n; i++) {
			System.out.println(str + "   Thread name = " + l[i].getName());
		}
		n = group.activeGroupCount();
		System.out.println(str + "   # of active sub thread groups = " + n);
		ThreadGroup[] s = new ThreadGroup[n];
		n = group.enumerate(s, false);
		for (int i = 0; i < n; i++) {
			printInfo(s[i], "   " + str);
		}
	}

	public void testCopyStringToClipboard() {
		String str = "String destined for clipboard";
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection strSel = new StringSelection(str);
		clipboard.setContents(strSel, null);
	}

	/**
	 * <pre>
	 * The test result confirms that:
	 * The System ClassLoader is implemented by the sun.misc.Launcher$ExtClassLoader class.
	 * The Extensions ClassLoader is implemented by the sun.misc.Launcher$AppClassLoader class.
	 * The Bootstrap ClassLoad is represented as null.
	 * java.lang.String is a system class, stored in <JAVA_HOME>/lib/rt.jar, and loaded by the Bootstrap ClassLoader.
	 * sun.security.pkcs11.P11Util is an extension class, stored in <JAVA_HOME>/lib/ext/sunpkcs11.jar, and loaded by the Extensions ClassLoader.
	 * ClassLoaderTest is my own class, stored in ./ClassLoaderTest.class, and loaded by the System ClassLoader.
	 * </pre>
	 */
	public void testClassLoader() {
		java.io.PrintStream out = System.out;
		Object o = null;
		Class<? extends Object> c = null;
		ClassLoader l = null;

		l = ClassLoader.getSystemClassLoader();
		out.println("");
		out.println("Built-in ClassLoaders");
		out.println("System ClassLoader: " + l.getClass().getName());
		l = l.getParent();
		out.println("Extensions ClassLoader: " + l.getClass().getName());
		l = l.getParent();
		out.println("Bootstrap ClassLoader: " + l);

		o = new java.lang.String();
		c = o.getClass();
		l = c.getClassLoader();
		out.println("");
		out.println("ClassLoader for java.lang.String: " + l);

		o = new TestClass();
		c = o.getClass();
		l = c.getClassLoader();
		out.println("");
		out.println("ClassLoader for ClassLoaderTest: " + l.getClass().getName());
	}

	/**
	 * This tutorial will examine the differences between a file's path, absolute path, and canonical
	 * path. The FilePaths class will display data about several files and directories in the project.
	 * <p>
	 * Looking at the output tells us about path, absolute file, absolute path, canonical file, and
	 * canonical path. First off, you probably noticed that getAbsoluteFile() returns the same result
	 * as getAbsolutePath(), and getCanonicalFile() returns the same result as getCanonicalPath().
	 * <p>
	 * Next, you should notice that getPath() returns the file path that we used when we instantiated
	 * the File object with the String file path, but with the current system's path separation
	 * character. Since I'm on Windows XP, this is a backslash. If this path is relative, getPath()
	 * returns the relative path, whereas if we specified a full path when instantiating the File
	 * object, the full path is returned by getPath(). The getPath() method also returns "." and ".."
	 * if we used them in specifying the relative path.
	 * <p>
	 * Next, notice that getAbsolutePath() returns an absolute path, but that this path can contain
	 * things like "." and "..". The getCanonicalPath() method, on the other hand, returns an absolute
	 * path, but it removes unnecessary parts of the path, such as "." and ".." and any other
	 * unnecessary directories involved in the "." or ".." path. This is shown clearly in the
	 * file1.txt example above.
	 * <p>
	 * On Windows systems, the getCanonicalPath() method will also uppercase the drive letter. Notice
	 * that getPath() and getAbsolutePath() return the lowercase drive letter that we entered, but
	 * getCanonicalPath() returns the uppercase drive letter.
	 * <p>
	 * refert to <a href=
	 * 'http://www.avajava.com/tutorials/lessons/whats-the-difference-between-a-files-path-absolute-path-and-canonical
	 * - p a t h . h t m l ' > What ' s the difference between a file's path, absolute path, and
	 * canonical path?</a>
	 */

	public void testFilePath() throws IOException {
		String[] fileArray = { "C:\\projects\\workspace\\testing\\f1\\f2\\f3\\file5.txt",
				"folder/file3.txt", "../testing/file1.txt", "../testing", "f1/f2" };

		for (String f : fileArray) {
			File file = new File(f);
			System.out.println("========================================");
			System.out.println("          name:" + file.getName());
			System.out.println("  is directory:" + file.isDirectory());
			System.out.println("        exists:" + file.exists());
			System.out.println("          path:" + file.getPath());
			System.out.println(" absolute file:" + file.getAbsoluteFile());
			System.out.println(" absolute path:" + file.getAbsolutePath());
			System.out.println("canonical file:" + file.getCanonicalFile());
			System.out.println("canonical path:" + file.getCanonicalPath());
		}
	}

	public void testSeparator() {
		// separator = /
		String separator = File.separator;
		log.info("separator = " + separator);
		char separatorChar = File.separatorChar;
		log.info("separatorChar = " + separatorChar);
		String fileSeparator = System.getProperty("file.separator");
		log.info("fileSeparator = " + fileSeparator);

		// pathSeparator = ; in windows
		String pathSeparator = File.pathSeparator;
		log.info("pathSeparator = " + pathSeparator);
		char pathSeparatorChar = File.pathSeparatorChar;
		log.info("pathSeparatorChar = " + pathSeparatorChar);
	}

	public void testConvertCode() {
		// 垂直tab \v java不支持
		log.info("qqqqqqq");
		log.info("");
		log.info("qqqqqqq");
		char c = (char) 11;
		log.info("11");
		log.info(String.valueOf(c));
		log.info("11");

		// return
		log.info("r");
		log.info("\r");
		int rint = (int) "\r".charAt(0);
		log.info(String.valueOf(rint));
		log.info("r");

		char c2 = (char) 13;
		log.info("13");
		log.info(String.valueOf(c2));
		log.info("13");

		// new line
		log.info("n");
		log.info("\n");
		int nint = (int) "\n".charAt(0);
		log.info(String.valueOf(nint));
		log.info("n");

		char c3 = (char) 10;
		log.info("10");
		log.info(String.valueOf(c3));
		log.info("10");

		// tab
		char c4 = (char) 9;
		log.info("9");
		log.info("aaa" + c4 + "bbb");
		log.info("aaa" + "\t" + "bbb");
		log.info("aaa" + "\u0009" + "bbb");
		log.info("9");

		// 空格
		char c5 = (char) 32;
		log.info("32");
		log.info("aaa" + c5 + "bbb");
		log.info("aaa" + " " + "bbb");
		log.info("aaa" + "\u0020" + "bbb");
		log.info("32");
	}

	// get system info
	public void testProperties() throws IOException {
		Map<String, String> env = System.getenv();
		env.get("TMP");
		// The method System.getenv() should not be used to access environment
		// variables because not all platforms have support for environment
		// variables. Recommendation 1. Use system properties instead of
		// environment variables.
		System.getenv("tmp");
		String dir = System.getProperty("user.dir");
		log.info("dir = " + dir + " in testSystem test()");

		Properties sysprops = System.getProperties();
		sysprops.store(System.out, "System Properties");
		sysprops.get("java.io.tmpdir");
		sysprops.get("java.class.path");

		Enumeration<Object> enuKeys = sysprops.keys();
		while (enuKeys.hasMoreElements()) {
			String key = (String) enuKeys.nextElement();
			String value = sysprops.getProperty(key);
			System.out.println(key + ": " + value);
		}

		// "\r\n" on Windows, "\n" on UNIX, "\r" on Macs
		System.getProperty("line.separator");

		java.io.OutputStream os = new java.io.FileOutputStream("props.xml");
		sysprops.storeToXML(os, "comment");
		log.info(String.valueOf(sysprops.keySet().size()));
		log.info(sysprops.toString());
	}

	/**
	 * update log4j.properties dynamic
	 */
	public void testLog4jLoadingConfiguration() throws IOException {
		// load log4j.properties into memory
		String propertiesPath = "C:/sp/Dropbox/Base/DctmTools/config/log4j.properties";
		org.apache.log4j.PropertyConfigurator.configure(propertiesPath);

		// load log4j.properties as properties
		Properties props = new Properties();
		FileInputStream in = new FileInputStream(propertiesPath);
		props.load(in);
		in.close();

		// change value
		String logDir = props.getProperty("log.dir");
		log.info(logDir);
		String dynamicLogDir = "c:/Documentum";// log directory somehow
		// chosen...
		props.put("log.dir", dynamicLogDir); // overwrite "log.dir"

		org.apache.log4j.PropertyConfigurator.configure(props);
		log.info(props.getProperty("log.dir"));
	}

	/**
	 * Accessing JVM arguments from Java
	 */
	public void getJvmArguments() {
		RuntimeMXBean runtimemxBean = ManagementFactory.getRuntimeMXBean();
		List<String> aList = runtimemxBean.getInputArguments();

		for (int i = 0; i < aList.size(); i++) {
			System.out.println(aList.get(i));
		}
	}

	// 堆检测
	public void testMemoryMXBean() {
		MemoryMXBean aMemoryMXBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heapMemoryUsage = aMemoryMXBean.getHeapMemoryUsage();
		log.info("heapMemoryUsage = " + heapMemoryUsage);
		long usedHeapMemory = heapMemoryUsage.getUsed();
		log.info("usedHeapMemory = " + usedHeapMemory);
		long maxHeapMemory = heapMemoryUsage.getMax();
		log.info("maxHeapMemory = " + maxHeapMemory);
	}

	/**
	 * Runtime的exec方法 可以在Java去运行外部的另一程序
	 */
	public void testRuntime() {
		Runtime rt = Runtime.getRuntime();
		try {
			String cmd = "ping 127.0.0.1";
			// rt.exec("notepad");
			Process ps = rt.exec(cmd); // 执行命令 notepad
			ps.waitFor();
			InputStream is = ps.getInputStream(); // getErrorStream();
			int data;
			while ((data = is.read()) != -1) {
				System.out.print((char) data);
			}

			if (ps.exitValue() == 0)
				log.info("External program terminate normally.");
			else
				log.info("External program terminate abnormally.");
		} catch (Exception e) {
			log.error("Exception in testRuntime()", e);
		}
	}

	/**
	 * loadLibrary() - Loads a native library in the library path with the given library name.Native
	 * libraries are DLL files on Windows systems.
	 * <p>
	 * load() - Loads a native library in the file system with the given full path name.load() can
	 * also loads EXE files on Windows systems.
	 */
	public void testRuntimeLoadLibrary() {
		Runtime rt = Runtime.getRuntime();
		try {
			java.io.PrintStream out = System.out;
			out.println("Loading a native library...");
			// put the dll file in class folder
			// C:/ProgramFiles/Java/jdk1.5.0_22/bin/beanreg.dll
			rt.loadLibrary("beanreg");

			out.println("Loading a native code...");
			rt.load("C:/ProgramFiles/Java/jdk1.5.0_22/bin/java.exe");
		} catch (Exception e) {
			log.error("Exception in testRuntimeLoadLibrary()", e);
		}
	}

	/**
	 * 如果你需要在你的程序关闭前采取什么措施，那么关闭钩子（shutdown hook）是很有用的。
	 */
	public void testRuntimeShutdownHook() {
		log.debug("Enter testRuntimeShutdownHook()");
		Runtime rt = Runtime.getRuntime();
		rt.addShutdownHook(new Thread() {
			public void run() {
				log.info("run in shutting down hook");
			}
		});
		int status = 0;
		rt.exit(status);// will run shutdown hooks
		rt.halt(status);// this method does not cause shutdown hooks to be
		// started
		log.debug("Exit testRuntimeShutdownHook()");
	}

	/**
	 * <pre>
	 * 在2002年4月发布的JDK1.4中，正式引入了NIO。JDK在原有标准IO的基础上，提供了一组多路复用IO的解决方案。
	 * 通过在一个Selector上挂接多个Channel，通过统一的轮询线程检测，每当有数据到达，触发监听事件，
	 * 将事件分发出去，而不是让每一个channel长期消耗阻塞一个线程等待数据流到达。
	 * 所以，只有在对资源争夺剧烈的高并发场景下，才能见到NIO的明显优势。
	 * 相较于面向流的传统方式这种面向块的访问方式会丢失一些简易性和灵活性。
	 * 下面给出一个NIO接口读取文件的简单例子（仅示意用）
	 * </pre>
	 */
	public void testNewIoRead() {
		String file = "";
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			FileChannel channel = in.getChannel();

			ByteBuffer buffer = ByteBuffer.allocate(1024);
			channel.read(buffer);
			byte[] b = buffer.array();
			System.out.println(new String(b));
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testSwing() {
		JFrame jf = new JFrame("mybole");
		jf.setSize(600, 400);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton btn = new JButton("winsun");
		jf.add(btn);
		jf.setVisible(true);
	}

	/**
	 * input with JOptionPane
	 */
	public void testJOptionPane() {
		String name = JOptionPane.showInputDialog("What is your name?");
		String input = JOptionPane.showInputDialog("How old are you?");
		int age = Integer.parseInt(input);
		log.info("Hello, " + name + ". Next year, you'll be " + (age + 1));
	}

	/**
	 * input with scanner
	 */
	public void testScanner() {
		java.util.Scanner in = new java.util.Scanner(System.in);
		// get first input
		System.out.print("What is your name? ");
		String name = in.nextLine();

		// get second input
		System.out.print("How old are you? ");
		int age = in.nextInt();

		// display output on console
		log.info("Hello, " + name + ". Next year, you'll be " + (age + 1));
		in.close();
	}

	public void testSystemIn() throws IOException {
		System.in.read();
	}
	
	public void testConvertRGBtoHex() {
		int r = 184;
		int g = 204;
		int b = 228;
		//Use capital X's if you want your resulting hex-digits to be capitalized (#FFFFFF vs. #ffffff)
		String format = "#%02X%02X%02X"; 
		String hex = String.format(format, r, g, b);
		log.info("hex2 = {}", hex);
		
		Color color = new Color(r,g,b);
		String hex2 = Integer.toHexString(color.getRGB() & 0xffffff);
		if (hex2.length() < 6) {
		    hex2 = "0" + hex2;
		}
		hex2 = "#" + hex2;
		log.info("hex = {}", hex2);
	}
}
