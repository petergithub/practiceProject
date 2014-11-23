package demo.designpattern.datastructure;

import java.util.LinkedList;

public class MyQueue {
	private LinkedList<Object> queue = new LinkedList<Object>();

	public void enqueue(Object o) {
		queue.addLast(o);
	}

	public Object dequeue() {
		return queue.removeFirst();
	}

	public boolean empty() {
		return queue.isEmpty();
	}

	public static void main(String args[]) {
		MyQueue queue = new MyQueue();
		queue.enqueue("one");
		queue.enqueue("two");
		queue.enqueue("three");

		System.out.println(queue.dequeue());
		System.out.println(queue.dequeue());
		System.out.println(queue.dequeue());
		System.out.println(queue.empty());
	}
}
