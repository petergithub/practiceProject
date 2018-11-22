package doing.concurrency.producer.consumer;

public class Producer extends Thread {
	private String name;
	private Storage storage;
	int time;

	public Producer(String name, Storage storage) {
		this.name = name;
		this.storage = storage;
	}

	public void run() {
		while (true) {
			// System.out.println(name + " into");
			storage.addData(name);
			// System.out.println(name+ " out");
			try {
				time = (int) Math.random() * 3000;
				sleep(time);
			} catch (InterruptedException e) {
			}
		}
	}
}
