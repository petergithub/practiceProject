package demo.javabasics;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Apr 23, 2012 12:04:30 PM
 */
public class Interview {
	private static final Logger logger = LoggerFactory.getLogger(Interview.class);

	public void testIntegerEqual() {
		Integer i = 100;
		Integer j = 100;
		Assert.assertEquals(i, j);
		logger.info("i==j is {}", i == j); // true
		i = 200;
		j = 200;
		logger.info("i==j is {}", i == j);// false
		Assert.assertNotSame(i, j);
		// it is a object if it is less than -127 and greater than 128
		// else it is a int type
	}

	@Test
	public void testMath() {
		long round1 = Math.round(12.5);
		long round2 = Math.round(-12.5);
		Assert.assertEquals(round1, 13);
		Assert.assertEquals(round2, -12);
	}

	public void testYun() throws Exception {
		int a = 0;
		int c = 0;
		do {
			--c;
			a = a - 1;
		} while (a > 0);
		Assert.assertEquals(-1, c);

		int x = 1;
		int y = 2;
		int z = 3;
		y += z-- / ++x;
		Assert.assertEquals(3, y);

		int[] xarr = { 2, 3, -8, 7, 9 };
		int max = xarr[0];
		for (int i = 1; i < xarr.length; i++) {
			if (xarr[i] > max) max = xarr[i];
		}
		Assert.assertEquals(9, max);
	}

	// public static void append(StringBuffer a, StringBuffer
	// b)中的变量a,b只是main方法中的变量a,b的引用地址副本，
	// 也就是说相当于append中的a与main中的a是指向同一个地址，
	// append中的b与main中的b是指向同一个地址，所以a.append(b)
	// 操作影响到了main方法中的a,但是append方法中的b=a;操作只是改变了
	// append方法的中b的指向，并没有改变main方法中b的指向
	public void testReference() {
		StringBuffer a = new StringBuffer("a");
		StringBuffer b = new StringBuffer("b");
		append(a, b);
		logger.info("after execute append() a = " + a.toString() + ", b = " + b.toString());
		b = a;
		logger.info("after b=a: a = " + a.toString() + ", b = " + b.toString());
	}

	private void append(StringBuffer a, StringBuffer b) {
		logger.debug("Enter append(" + a + ", " + b + ")");
		a.append(b);
		logger.info("after a.append(b): a = " + a + ", b = " + b + ")");
		b = a;
		logger.debug("Exit append(" + a + ", " + b + ")");
	}

	/**
	 * 测试调用静态方法的参数名同名问题 结果: 隐藏静态方法的参数
	 */
	public void testReference2() {
		String str = "1234";
		changeStr(str);
		Assert.assertEquals(str, "1234");
	}

	public static void changeStr(String str) {
		str = "welcome";
	}

	@SuppressWarnings("unused")
	public void testContructor() {
		B b;
		A a;
		a = new B();
		a = new A();
	}

	class A {
		public A() {
			logger.info("A");
		}
	}

	class B extends A {
		public B() {
			logger.info("B");
		}
	}

	/**
	 * 测试switch的default语句写在第一行的结果， 结果: 它与case语句一样。
	 **/
	public void testSwitch() {
		int i = 10;
		switchDefault(i);
	}

	public static void switchDefault(int val) {
		switch (val) {
			default:
				System.out.println("no value given");
			case 1:
				System.out.println("one");
			case 10:
				System.out.println("ten");
			case 5:
				System.out.println("five");
		}
	}

	/**
	 * You might be tempted to say false, but you'd be wrong. It prints true, because all instances of
	 * a generic class have the same run-time class, regardless of their actual type parameters.
	 */
	public void genericType() {
		List<String> l1 = new ArrayList<String>();
		List<Integer> l2 = new ArrayList<Integer>();
		Assert.assertTrue(l1.getClass() == l2.getClass());
	}
}
