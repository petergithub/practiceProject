package doing.concurrency.producer.consumer;

import org.apache.log4j.Logger;

/**
 * 多线程.生产者消费者
 */
public class Storage {
	protected static final Logger logger = Logger.getLogger(Storage.class);
	private int count;
	private int size;

	public Storage(int size) {
		this.size = size;
	}

	public synchronized void addData(String name) {
		while (count >= size) {
			try {
				logger.info(name + " into wait");
				this.wait();
				logger.info(name + " out wait");
			} catch (InterruptedException e) {
				logger.error("Exception in addData()", e);
			}
		}
		this.notify();
		count++;
		logger.info(name + " make data count: " + count);
	}

	public synchronized void delData(String name) {
		while (count <= 0) {
			try {
				logger.info(name + " into wait");
				this.wait();
				logger.info(name + " out wait");
			} catch (InterruptedException e) {
				logger.error("Exception in delData()", e);
			}
		}
		this.notify();
		logger.info(name + " use data count: " + count);
		count--;
	}
}
