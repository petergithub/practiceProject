package doing.concurrency.oracle.producer.consumer.blocking.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * http://docs.oracle.com/javase/tutorial/essential/concurrency/QandE/answers.html
 * @version Date：Feb 4, 2016 4:19:24 PM
 */
public class ProducerConsumerExample {
	public static void main(String[] args) {
		BlockingQueue<String> drop = new SynchronousQueue<String>();
		(new Thread(new Producer(drop))).start();
		(new Thread(new Consumer(drop))).start();
	}
}