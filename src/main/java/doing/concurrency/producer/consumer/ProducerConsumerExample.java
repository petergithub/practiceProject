package doing.concurrency.producer.consumer;

/**
 * 多线程.生产者消费者
 */
public class ProducerConsumerExample {
	public static void main(String arg[]) {
		Storage storage = new Storage(5);
		Producer p1 = new Producer("Producer1", storage);
		Producer p2 = new Producer("Producer2", storage);
		Consumer c1 = new Consumer("Consumer1", storage);

		p1.start();
		p2.start();
		c1.start();
	}
}
