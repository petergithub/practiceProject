package doing.concurrency.threads;

/**
 * testSyncClass
 * It's possible to synchronize a static method. When this occurs, a lock is
 * obtained for the class itself. This is demonstrated by the static hello()
 * method in the SyncExample class below. When we create a synchronized block in
 * a static method, we need to synchronize on an object, so what object should
 * we synchronize on? We can synchronize on the Class object that represents the
 * class that is being synchronized. This is demonstrated in the static
 * goodbye() method of SyncExample. We synchronize on SyncExample.class.
 * <p>
 * Note that in this example, the synchronized block synchronizes on the
 * SyncExample Class object. When using a synchronized block, we can also
 * synchronize on another object. That object will be locked while the code in
 * the synchronized block is being executed.
 * <p>
 * refer to <a href='http://www.avajava.com
 * /tutorials/lessons/how-do-i-use-a-synchronized
 * -block-in-a-static-method.html'>How do I use a synchronized block in a static
 * method?</a>
 * 
 * <pre>
 * testSyncMethod
 * 当一个线程进入一个对象的一个synchronized方法后，其它线程是否可进入此对象的其它方法?
 * 1. 一个线程在访问一个对象的同步方法时，另一个线程可以同时访问这个对象的非同步方法testSyncMethod1()
 * 2. 一个线程在访问一个对象的同步方法时，另一个线程不能同时访问这个同步方法。（代码略）
 * 3. 一个线程在访问一个对象的同步方法时，另一个线程不能同时访问这个对象的另一个同步方法testSyncMethod3()。
 * </pre>
 * 
 * Java also offers three ways to define synchronized blocks.
 * <p>
 * Synchronized Class Method:
 * 
 * <pre>
 * class class_name {
 * 	static synchronized type method_name() {
 *       statement block
 *    }
 * }
 * All the statements in the method become the synchronized block, and the class
 * object is the lock.
 * </pre>
 * 
 * Synchronized Instance Method:
 * 
 * <pre>
 * class class_name {
 * 	synchronized type method_name() {
 *       statement block
 *    }
 * }
 * All the statements in the method become the synchronized block, and the
 * instance object is the lock.
 * </pre>
 * 
 * Synchronized Statement:
 * 
 * <pre>
 * class class_name {
 * 	type method_name() {
 *       synchronized (object) {
 *          statement block
 *       }
 *    }
 * }
 * All the statements specified in the parentheses of the synchronized statement
 * become the synchronized block, and the object specified in the statement is
 * the lock.
 * </pre>
 * refer to http://www.herongyang.com/Java/Synchronization-Support-in-Java-synchronized.html
 * 
 * @author Shang Pu
 * @version Date: Apr 24, 2012 2:18:01 PM
 */
public class SyncExample {

	public void testSyncClass() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				new SyncExample();
				hello();
				goodbye();
			}
		});
		t.start();
		hello();
		goodbye();
	}

	public static synchronized void hello() {
		System.out.println("hello");// add a break point here, and let the first
									// thread go over it
	}

	public static void goodbye() {
		synchronized (SyncExample.class) {
			System.out.println("goodbye");// add a break point here, and let the
											// first thread stop here,
			// you will see the second thread stop at the line 29 and waiting
			// for the first thread to run.
		}
	}

	/**
	 * 1. 一个线程在访问一个对象的同步方法时，另一个线程可以同时访问这个对象的非同步方法testSyncMethod1()
	 */
	public void testSyncMethod1() {
		final SyncExample object = new SyncExample();
		Thread t1 = new Thread() {
			public void run() {
				object.synchronizedMethod();
			}
		};
		t1.start();
		Thread t2 = new Thread() {
			public void run() {
				object.generalMethod();
			}
		};
		t2.start();
	}

	/**
	 * 3. 一个线程在访问一个对象的同步方法时，另一个线程不能同时访问这个对象的另一个同步方法。
	 */
	@org.junit.Test
	public void testSyncMethod3() {
		final SyncExample object = new SyncExample();
		Thread t1 = new Thread() {
			public void run() {
				object.synchronizedMethod();
			}
		};
		t1.start();
		Thread t2 = new Thread() {
			public void run() {
				object.synchronizedMethod2();
			}
		};
		t2.start();
	}

	public void generalMethod() {
		System.out.println("Invoked generalMethod...");
	}

	public synchronized void synchronizedMethod() {
		System.out.println("Invoked synchronizedMethod!");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized void synchronizedMethod2() {
		System.out.println("Invoked synchronizedMethod2!");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
