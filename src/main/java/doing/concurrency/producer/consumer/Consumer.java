package doing.concurrency.producer.consumer;

import org.apache.log4j.Logger;

public class Consumer extends Thread {
	protected static final Logger logger = Logger.getLogger(Consumer.class);
	private String name;
	private Storage storage;

	public Consumer(String name, Storage storage) {
		this.name = name;
		this.storage = storage;
	}

	public void run() {
		while (true) {
			storage.delData(name);

			try {
				sleep((int) Math.random() * 3000);
			} catch (InterruptedException e) {
				logger.error("Exception in delData()", e);
			}
		}
	}
}
