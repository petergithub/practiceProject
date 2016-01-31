package doing.concurrency;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

/**
 * http://uule.iteye.com/blog/1101994
 * thread.Join把指定的线程加入到当前线程，可以将两个交替执行的线程合并为顺序执行的线程。比如在线程B中调用了线程A的Join()方法，
 * 直到线程A执行完毕后，才会继续执行线程B。
 * 
 * t.join(); //使调用线程 t 在此之前执行完毕。
 * 
 * t.join(1000); //等待 t 线程，等待时间是1000毫秒
 * 
 * @version Date：Jan 31, 2016 4:37:44 PM
 */
public class PracticeThreadJoin implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(PracticeThreadJoin.class);

	public static int count = 0;

	public void run() {
		for (int index = 0; index < 5; index++) {
			count = count + 1;
		}
	}

	@Test
	public void testWithJoin() {
		count = 0;
		Thread thread = new Thread(new PracticeThreadJoin());
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			log.error("InterruptedException in PracticeJoin.testJoin()", e);
		}

		// wait for thread end with join method
		Assert.assertEquals(5, count);
	}

	@Test
	public void testWithoutJoin() {
		count = 0;
		Thread thread = new Thread(new PracticeThreadJoin());
		thread.start();

		// execute this before thread ends
		Assert.assertEquals(0, count);
	}
}