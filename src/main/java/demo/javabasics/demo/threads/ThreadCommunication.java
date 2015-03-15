package demo.javabasics.demo.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 写两个线程，一个线程打印 1到52，另一个线程打印字母A到Z。打印顺序为12A34B56C……5152Z。要求用线程间的通信。
 * http://blog.csdn.net/shenbau/article/details/28093379 
 * 
 * 其他解法： 采用Lock 和 Condition http://mouselearnjava.iteye.com/blog/1949693
 * 
 * @version Date: Mar 15, 2015 5:01:25 PM
 * @author Shang Pu
 */
public class ThreadCommunication {
	private static final Logger log = LoggerFactory.getLogger(ThreadCommunication.class);

//	@Test
	public void testThreadCommunication() {
		Object obj = new Object();
		NumberPrinter numberPrinter = new NumberPrinter(obj);
		LetterPrinter letterPrinter = new LetterPrinter(obj);
		numberPrinter.start();
		letterPrinter.start();
	}
	
	@Test
	public void testThreadCommunication2() {
		ThreadCommunication2.main(null);
	}

	public void testPrintChar() {
		for (int i = 65; i <= 90; i++) {
			log.info("i = {}", (char) i);
		}
		for (char i = 'A'; i <= 'Z'; i++) {
			System.out.print(i);
		}
	}

	class NumberPrinter extends Thread {
		private Object obj;

		public NumberPrinter(Object obj) {
			this.obj = obj;
		}

		public void run() {
			synchronized (obj) {
				for (int i = 1; i <= 52; i++) {
					System.out.print(i);
					if (i % 2 == 0) {
						obj.notifyAll();
						try {
							obj.wait();
						} catch (InterruptedException e) {
							log.error("InterruptedException in .run()", e);
						}
					}
				}
			}
		}
	}

	class LetterPrinter extends Thread {
		private Object obj;

		public LetterPrinter(Object obj) {
			this.obj = obj;
		}

		public void run() {
			synchronized (obj) {
				for (char i = 'A'; i <= 'Z'; i++) {
					System.out.print(i);
					obj.notifyAll();
					try {
						if (i != 'Z') {// 最后一个就不要等了
							obj.wait();
						}
					} catch (InterruptedException e) {
						log.error("InterruptedException in .run()", e);
					}
				}
			}
		}
	}
}

class ThreadCommunication2 {
	private final Lock lock = new ReentrantLock();
	private final Condition numberCondition = lock.newCondition();
	private final Condition letterCondition = lock.newCondition();
	private static char currentThread = 'A';

	public static void main(String[] args) {
		ThreadCommunication2 test = new ThreadCommunication2();
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(test.new NumberPrinter());
		service.execute(test.new LetterPrinter());
		service.shutdown();
	}

	private class NumberPrinter implements Runnable {
		public void run() {
			for (int i = 1; i <= 52; i++) {
				lock.lock();

				try {
					while (currentThread != 'A') {
						try {
							numberCondition.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					System.out.print(i);
					if (i % 2 == 0) {
						currentThread = 'B';
						letterCondition.signal();
					}
				} finally {
					lock.unlock();
				}
			}
		}
	}

	private class LetterPrinter implements Runnable {
		@Override
		public void run() {
			for (char c = 'A'; c <= 'Z'; c++) {
				lock.lock();
				try {
					while (currentThread != 'B') {
						try {
							letterCondition.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					System.out.print(c);
					currentThread = 'A';
					numberCondition.signal();
				} finally {
					lock.unlock();
				}
			}
		}
	}
}
