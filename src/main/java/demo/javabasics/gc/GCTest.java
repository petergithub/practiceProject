package demo.javabasics.gc;

/**
 * GCTest.java
 * Testing garbage collection behavior.
 * Suggested JVM options: -Xms2m -Xmx8m
 * Copyright (c) 2003 by Dr. Herong Yang, herongyang.com
 * refer to
 * http://www.herongyang.com/JVM/GC-Garbage-Collection-Test-Program.html
 * <p>
 * Note that
 * <li>An inner class MyObject is used to represent a generic object. It has a
 * fixed size of 128KB plus some overhead.</li>
 * <li>Another inner class MyList is used to as a manager to register and
 * release objects. It uses a linked list data structure to do the job.</li>
 * <li>A third inner class Monitor is used to sample memory usage and object
 * counts data.</li>
 * <li>The main test, myTest(), is divided two sections: the initialization
 * section, where a number of objects are added to the list; the loop section,
 * where objects are added to the list, then removed from the list.</li>
 * <li>Two parameters are supported. The first one, initSteps, is used to
 * control how many objects we want to put into the list initially. The second
 * one, testSteps, is used to control the number objects to be added to the list
 * before removing the same number of objects from the list.</li>
 * <li>Two types of data are collected by the program: memory usage data,
 * including total memory, free memory, and the percentage of the free memory
 * over total memory; and counts of my objects, including number of total
 * object, number of active objects, and number of dead objects. The object
 * counts have been converted into memory sizes to make it easier to compare
 * them with memory usages.</li>
 * <li>Memory related data has been normalized into kilo bytes (BK).</li>
 * <li>The over head memory represents the memory that is used by the program,
 * but not by the allocated objects.
 */
class GCTest {
	static MyList objList = null;
	static int wait = 500; // milliseconds
	static int initSteps = 16; // 2 MB
	static int testSteps = 1;
	static int objSize = 128; // 1/8 MB

	public static void main(String[] arg) {
		if (arg.length > 0) initSteps = Integer.parseInt(arg[0]);
		if (arg.length > 1) testSteps = Integer.parseInt(arg[1]);
		objList = new MyList();
		Monitor m = new Monitor();
		m.setDaemon(true);
		m.start();
		myTest();
	}

	public static void myTest() {
		for (int m = 0; m < initSteps; m++) {
			mySleep(wait);
			objList.add(new MyObject());
		}
		for (int n = 0; n < 10 * 8 * 8 / testSteps; n++) {
			for (int m = 0; m < testSteps; m++) {
				mySleep(wait);
				objList.add(new MyObject());
			}
			for (int m = 0; m < testSteps; m++) {
				mySleep(wait);
				objList.removeTail();
				// objList.removeHead();
			}
		}
	}

	static void mySleep(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			System.out.println("Interreupted...");
		}
	}

	static class MyObject {
		private static long count = 0;
		@SuppressWarnings("unused")
		private long[] obj = null;
		public MyObject next = null;
		public MyObject prev = null;

		public MyObject() {
			count++;
			obj = new long[objSize * 128];
		}

		protected final void finalize() {
			count--;
		}

		static long getCount() {
			return count;
		}
	}

	static class MyList {
		static long count = 0;
		MyObject head = null;
		MyObject tail = null;

		static long getCount() {
			return count;
		}

		void add(MyObject o) {
			// add the new object to the head;
			if (head == null) {
				head = o;
				tail = o;
			} else {
				o.prev = head;
				head.next = o;
				head = o;
			}
			count++;
		}

		void removeTail() {
			if (tail != null) {
				if (tail.next == null) {
					tail = null;
					head = null;
				} else {
					tail = tail.next;
					tail.prev = null;
				}
				count--;
			}
		}

		void removeHead() {
			if (head != null) {
				if (head.prev == null) {
					tail = null;
					head = null;
				} else {
					head = head.prev;
					head.next = null;
				}
				count--;
			}
		}
	}

	static class Monitor extends Thread {
		public void run() {
			Runtime rt = Runtime.getRuntime();
			System.out
					.println("Time   Total   Free   Free   Total   Act.   Dead   Over");
			System.out
					.println("sec.    Mem.   Mem.   Per.    Obj.   Obj.   Obj.   Head");
			long dt0 = System.currentTimeMillis() / 1000;
			while (true) {
				long tm = rt.totalMemory() / 1024;
				long fm = rt.freeMemory() / 1024;
				long ratio = (100 * fm) / tm;
				long dt = System.currentTimeMillis() / 1000 - dt0;
				long to = MyObject.getCount() * objSize;
				long ao = MyList.getCount() * objSize;
				System.out.println(dt + "   " + tm + "   " + fm + "   " + ratio
						+ "%" + "   " + to + "   " + ao + "   " + (to - ao)
						+ "   " + (tm - fm - to));
				mySleep(wait);
			}
		}
	}
}
