package doing.concurrency.oracle.producer.consumer.guarded.block;

/**
 * http://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html
 * @version Date：Feb 4, 2016 4:19:46 PM
 */
public class ProducerConsumerExample {
	public static void main(String[] args) {
		Drop drop = new Drop();
		(new Thread(new Producer(drop))).start();
		(new Thread(new Consumer(drop))).start();
	}
}
