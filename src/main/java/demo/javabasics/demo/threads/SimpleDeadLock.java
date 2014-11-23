package demo.javabasics.demo.threads;

/**
 * SimpleDeadLock.java
 * Copyright (c) 2002 by Dr. Herong Yang
 * <p>
 * deadlock can only happen when the program is running multiple threads, and
 * multiple locks are being used by multiple threads. Therefore:
 * <li>A single-threaded program will never have deadlocks.</li>
 * <li>A program with one lock will never have deadlocks.</li>
 */
public class SimpleDeadLock extends Thread {
	public static Object l1 = new Object();
	public static Object l2 = new Object();

	public static void main(String[] a) {
		Thread t1 = new Thread1();
		Thread t2 = new Thread2();
		t1.start();
		t2.start();
	}

	private static class Thread1 extends Thread {
		public void run() {
			synchronized (l1) {
				System.out.println("Thread 1: Holding lock 1...");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
				System.out.println("Thread 1: Waiting for lock 2...");
				synchronized (l2) {
					System.out.println("Thread 2: Holding lock 1 & 2...");
				}
			}
		}
	}

	private static class Thread2 extends Thread {
		public void run() {
			synchronized (l2) {
				System.out.println("Thread 2: Holding lock 2...");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
				System.out.println("Thread 2: Waiting for lock 1...");
				synchronized (l1) {
					System.out.println("Thread 2: Holding lock 2 & 1...");
				}
			}
		}
	}
}
