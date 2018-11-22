package demo.javabasics.vs;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverrideVsHide {
	public static final int parent = 10;
	public static final int child = 20;
	
	/**
	 * 静态方法不能被override，只能被hide
	 * <p>
	 * 在SUN的SL-275中明确写道: you CANNOT override a STATIC method but you can hide it!
	 */
	@Test
	@SuppressWarnings("static-access")
	public void testOverride() {
		Parent parent = new Child();
		assertEquals("parent static say", parent.staticSay());
		assertEquals("child normal say", parent.normalSay());
		assertEquals(OverrideVsHide.parent, parent.value);

		Child child = new Child();
		assertEquals("child static say", child.staticSay());
		assertEquals("child normal say", child.normalSay());
		assertEquals(OverrideVsHide.child, child.value);
		
		assertEquals("child execute", child.callExecute());
	}

	/**
	 * 属性的隐藏。子类继承父类的操作，变量 2009-5-11 18:02:00
	 */
	public void testHide() {
		Parent parent = new Parent();
		parent.setValueInParent(4);
		parent.printInParent();
		assertEquals(4, parent.value);

		Child child = new Child();
		child.printInChild();
		child.printInParent();
		child.setValueInParent(6);// set parent.value = 6, child.value is not changed
		child.printInChild();
		child.printInParent();
		
//		print in Parent, value = 4
//		print in Child super.value = 11, value = 20
//		print in Parent, value = 11
//		print in Child super.value = 7, value = 20
//		print in Parent, value = 7
	}
}

/**
 * this class is used for test method testOverride
 */
class Child extends Parent {
	private static final Logger log = LoggerFactory.getLogger(Child.class);
	public int value = OverrideVsHide.child;

	public void printInChild() {
		super.value = super.value + 1;
		log.info("print in Child super.value = " + super.value + ", value = " + value);
	}

	public static String staticSay() {
		return "child static say";
	}

	public String normalSay() {
		return "child normal say";
	}
	
	public String callExecute() {
		log.debug("Enter child callExecute()");
		String result = super.callExecute();
		log.debug("Exit child callExecute()");
		return result;
	}

	public String execute() {
		log.debug("Enter child execute()");
		String result = "child execute";
		log.debug("Exit child execute()");
		return result;
	}
}

/**
 * this class is used for test method testOverride
 */
class Parent {
	private static final Logger log = LoggerFactory.getLogger(Parent.class);
	public int value = OverrideVsHide.parent;

	public void setValueInParent(int i) {
		value = i;
	}

	public void printInParent() {
		log.info("print in Parent, value = " + value);
	}

	public static String staticSay() {
		return "parent static say";
	}

	public String normalSay() {
		return "parent normal say";
	}
	
	public String callExecute() {
		log.debug("Enter parent callExecute()");
		String result = execute();
		log.debug("Exit parent callExecute()");
		return result;
	}

	public String execute() {
		log.debug("Enter parent execute()");
		String result = "parent execute";
		log.debug("Exit parent execute()");
		return result;
	}
}
