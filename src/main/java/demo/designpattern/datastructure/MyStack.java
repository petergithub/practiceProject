package demo.designpattern.datastructure;

import java.util.LinkedList;

public class MyStack {
	private LinkedList<Object> stack = new LinkedList<Object>();

	public void push(Object o) {
		stack.addFirst(o);
	}

	public Object pop() {
		return stack.removeFirst();
	}

	public Object peek() {
		return stack.getFirst();
	}

	public boolean empty() {
		return stack.isEmpty();
	}

	public static void main(String args[]) {
		MyStack stack = new MyStack();
		stack.push("one");
		stack.push("two");
		stack.push("three");

		System.out.println(stack.pop());
		System.out.println(stack.pop());
		System.out.println(stack.pop());
		System.out.println(stack.empty());
	}
}
