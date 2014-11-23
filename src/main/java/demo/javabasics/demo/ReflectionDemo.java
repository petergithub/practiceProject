package demo.javabasics.demo;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

import demo.BasicDemo;

/**
 * @author Shang Pu
 * @version Date: May 14, 2012 9:57:26 AM
 */
public class ReflectionDemo {
	protected static final Logger logger = Logger.getLogger(ReflectionDemo.class);
	private static java.io.PrintStream out = System.out;

	@org.junit.Test
	public void testClassArrayReflection() {
		out.println("");
		out.println("   byte[] class information...");
		byte[] b = new byte[10];
		arrayIntrospection(b.getClass());

		out.println("");
		out.println("   int[] class information...");
		int[] i = new int[100];
		arrayIntrospection(i.getClass());

		out.println("");
		out.println("   byte[][] class information...");
		byte[][] bb = new byte[10][100];
		arrayIntrospection(bb.getClass());
	}

	public static void arrayIntrospection(Class<?> cls) {
		out.println("Class name:");
		out.println("   " + cls.getName());

		Class<?> p = cls.getSuperclass();
		out.println("Parent class:");
		out.println("   " + p);

		Class<?> t = cls.getComponentType();
		out.println("Element type:");
		out.println("   " + t.getName());

		Constructor<?>[] c = cls.getDeclaredConstructors();
		out.println("Constructors:");
		for (int i = 0; i < c.length; i++)
			out.println("   " + c[i].toString());

		Field[] f = cls.getDeclaredFields();
		out.println("Fields:");
		for (int i = 0; i < f.length; i++)
			out.println("   " + f[i].toString());

		Method[] m = cls.getDeclaredMethods();
		out.println("Methods:");
		for (int i = 0; i < m.length; i++)
			out.println("   " + m[i].toString());
	}

	public void testClassForName() {
		BasicDemo test = new BasicDemo();
		logger.info(test.getClass());
		String className = "com.helloworld.TestClassHelloWorld";
		try {
			Class<?> cl = Class.forName(className);
			Method[] methods = cl.getDeclaredMethods();
			for (Method m : methods) {
				logger.info(m);
			}
			BasicDemo t = (BasicDemo) cl.newInstance();
			t.testScanner();
		} catch (Exception e) {
			logger.error("Exception in testClassForName()", e);
		}
	}

	public void testReflection() {
		String name = "com.helloworld.TestClassHelloWorld";
		try {
			Class<?> cl = Class.forName(name);
			Class<?> supercl = cl.getSuperclass();
			System.out.println("class " + name);
			if (supercl != null && supercl != Object.class)
				System.out.println("extends " + supercl.getName());

			System.out.println("\n{\n");
			printConstructors(cl);
			System.out.println();
			printMethods(cl);
			System.out.println();
			printFields(cl);
			System.out.println();
		} catch (ClassNotFoundException e) {
			logger.error("Exception in testReflection()", e);
		}
	}

	/**
	 * Prints all constructors of a class
	 */
	public static void printConstructors(Class<?> cl) {
		Constructor<?>[] constructors = cl.getDeclaredConstructors();
		for (Constructor<?> c : constructors) {
			String name = c.getName();
			System.out.print("  " + Modifier.toString(c.getModifiers()));
			System.out.print(" " + name + "(");

			// print parameter types
			Class<?>[] paramTypes = c.getParameterTypes();
			for (int j = 0; j < paramTypes.length; j++) {
				if (j > 0) System.out.print(",  ");
				System.out.print(paramTypes[j].getName());
			}
			System.out.println(");");
		}
	}

	/**
	 * create an object via its multiparameter constructor using reflection
	 */
	public void testClassConstructor() throws Throwable {
		Class<ConstructorTest> ctClass = ConstructorTest.class;

		// creating an object calling no argument constructor via newInstance of
		// Class object
		ConstructorTest ctNoArgs = ctClass.newInstance();
		ctNoArgs.setPub("created this with ctClass.newInstance()");
		System.out.println("pub:" + ctNoArgs.getPub());

		// creating an object by getting Constructor object (with parameters)
		// and calling
		// newInstance (with parameters) on it
		Constructor<ConstructorTest> constructor = ctClass
				.getConstructor(new Class[] { String.class, String.class,
						String.class });
		ConstructorTest ctArgs = constructor.newInstance(new Object[] {
				"first", "second", "third" });
		ctArgs.setPub("created this with constructor.newInstance(new Object[] { \"first\", \"second\", \"third\" })");

		System.out.println("\npub:" + ctArgs.getPub());
		System.out.println("pro:" + ctArgs.getPro());
		System.out.println("pri:" + ctArgs.getPri());
	}

	class ConstructorTest {
		private String pri;
		protected String pro;
		public String pub;

		public ConstructorTest() {
		}

		public ConstructorTest(String pri, String pro, String pub) {
			this.pri = pri;
			this.pro = pro;
			this.pub = pub;
		}

		public String getPri() {
			return pri;
		}

		public void setPri(String pri) {
			this.pri = pri;
		}

		public String getPro() {
			return pro;
		}

		public void setPro(String pro) {
			this.pro = pro;
		}

		public String getPub() {
			return pub;
		}

		public void setPub(String pub) {
			this.pub = pub;
		}

	}

	/**
	 * Prints all methods of a class
	 */
	public static void printMethods(Class<?> cl) {
		Method[] methods = cl.getDeclaredMethods();
		for (Method m : methods) {
			Class<?> retType = m.getReturnType();
			// String name = m.getName();

			// print modifiers, return type and method name
			System.out.print("  " + Modifier.toString(m.getModifiers()));
			System.out.print(" " + retType.getName() + " " + "(");

			// print parameter types
			Class<?>[] paramTypes = m.getParameterTypes();
			for (int j = 0; j < paramTypes.length; j++) {
				if (j > 0) System.out.print(",  ");
				System.out.print(paramTypes[j].getName());
			}
			System.out.println(");");
		}
	}

	/**
	 * call a method using reflection
	 */
	public void testClassMethod() throws Exception {
		Testing t = new Testing("val1", false);
		Class<?> tClass = t.getClass();

		Method gs1Method = tClass.getMethod("getString1", new Class[] {});
		String str1 = (String) gs1Method.invoke(t, new Object[] {});
		System.out.println("getString1 returned: " + str1);

		Method ss1Method = tClass.getMethod("setString1",
				new Class[] { String.class });
		System.out.println("calling setString1 with 'val2'");
		ss1Method.invoke(t, new Object[] { "val2" });

		str1 = (String) gs1Method.invoke(t, new Object[] {});
		System.out.println("getString1 returned: " + str1);

		try {
			Method pd1Method = tClass.getMethod("getProtectedDude",
					new Class[] {});
			logger.info("pd1Method = " + pd1Method
					+ " in method testClassMethod()");
		} catch (NoSuchMethodException e) {
			logger.info("Expected NoSuchMethodException when try to call getMethod using a private or protected method");
		}

		Method[] methods = tClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			System.out.println("method: " + methods[i]);
		}

		System.out.println("getting declared method 'setProtectedDude'");
		Method proSetMethod = tClass.getDeclaredMethod("setProtectedDude",
				new Class[] { String.class });
		System.out.println("invoking setProtectedDude with 'blah'");
		proSetMethod.invoke(t, new Object[] { "blah" });

		System.out.println("\ngetting declared method 'getProtectedDude'");
		Method proGetMethod = tClass.getDeclaredMethod("getProtectedDude",
				new Class[] {});
		System.out.println("invoking getProtectedDude");
		String str1Declared = (String) proGetMethod.invoke(t, new Object[] {});
		System.out.println("getProtectedDude returned: " + str1Declared);
	}

	@SuppressWarnings("unused")
	class Testing {
		private String string1;
		private boolean boolean1;
		private String privateDude;
		private String protectedDude;

		public Testing() {
		}

		public Testing(String string1, boolean boolean1) {
			this.string1 = string1;
			this.boolean1 = boolean1;
		}

		private String getPrivateDude() {
			return privateDude;
		}

		private void setPrivateDude(String privateDude) {
			this.privateDude = privateDude;
		}

		protected String getProtectedDude() {
			return protectedDude;
		}

		protected void setProtectedDude(String protectedDude) {
			this.protectedDude = protectedDude;
		}

		public boolean isBoolean1() {
			return boolean1;
		}

		public void setBoolean1(boolean boolean1) {
			this.boolean1 = boolean1;
		}

		public String getString1() {
			return string1;
		}

		public void setString1(String string1) {
			this.string1 = string1;
		}

	}

	/**
	 * Prints all fields of a class
	 */
	public static void printFields(Class<?> cl) {
		Field[] fields = cl.getDeclaredFields();
		for (Field f : fields) {
			// String name = f.getName();
			Class<?> type = f.getType();
			System.out.print("  " + Modifier.toString(f.getModifiers()));
			System.out.println(" " + type.getName() + " " + ";");
		}
	}

	/**
	 * get and set a field using reflection
	 */
	@SuppressWarnings("unused")
	public void testClassField() throws Exception {
		FieldTest ft = new FieldTest();
		Class<?> ftClass = ft.getClass();

		Field f1 = ftClass.getField("pub");
		f1.set(ft, "reflecting on life");
		String str1 = (String) f1.get(ft);
		System.out.println("pub field: " + str1);

		Field f2 = ftClass.getField("parentPub");
		f2.set(ft, "again");
		String str2 = (String) f2.get(ft);
		System.out.println("\nparentPub field: " + str2);

		try {
			System.out.println("\nThis will throw a NoSuchFieldException");
			Field f3 = ftClass.getField("pro");
		} catch (Exception e) {
			logger.info("Expected NoSuchFieldException when try to call getField() on a protected or private field ");
		}

		Field f1Declared = ftClass.getDeclaredField("pub");
		f1Declared.set(ft, "this is public");
		String str1Declared = (String) f1Declared.get(ft);
		System.out.println("pub field: " + str1Declared);

		Field f2Declared = ftClass.getDeclaredField("pro");
		f2Declared.set(ft, "this is protected");
		String str2Declared = (String) f2Declared.get(ft);
		System.out.println("\npro field: " + str2Declared);

		// Field f4Declared = ftClass.getDeclaredField("pri");
		// f4Declared.set(ft, "this is private");
		// String str4Declared = (String) f4Declared.get(ft);
		// System.out.println("\npro field: " + str4Declared);
		try {
			System.out.println("\nThis will throw a NoSuchFieldException");
			Field f3Declared = ftClass.getDeclaredField("parentPub");
		} catch (Exception e) {
			logger.info("Expected NoSuchFieldException when try to call getDeclaredField() to get an inherited public field.");
		}

	}

	@SuppressWarnings("unused")
	class ParentFieldTest {
		private String parentPri;
		protected String parentPro;
		public String parentPub;

		public ParentFieldTest() {
		}
	}

	@SuppressWarnings("unused")
	class FieldTest extends ParentFieldTest {
		private String pri;
		protected String pro;
		public String pub;

		public FieldTest() {
		}

		public FieldTest(String pri, String pro, String pub) {
			this.pri = pri;
			this.pro = pro;
			this.pub = pub;
		}
	}

}
