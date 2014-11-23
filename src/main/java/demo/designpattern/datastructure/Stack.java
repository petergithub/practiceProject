package demo.designpattern.datastructure;

import java.util.LinkedList;

import org.apache.log4j.Logger;

/**
 * @author Shang Pu
 * @version Date: Apr 20, 2012 9:50:52 PM
 */
public class Stack<E> extends LinkedList<E> {
	protected static final Logger logger = Logger.getLogger(Stack.class);
	private static final long serialVersionUID = 1L;

	public E pop() {
		return super.getFirst();
	}

	public void push(E o) {
		super.addFirst(o);
	}

	public int size() {
		return super.size();
	}

	@org.junit.Test
	public void testStack() {
		Stack<String> strStack = new Stack<String>();
		strStack.add("1");
		strStack.add("2");
		strStack.add("3");
		logger.info("strStack = " + strStack+ " in method testStack()");
		logger.info("strStack.size() = " + strStack.size()+ " in method testStack()");
	}
}
