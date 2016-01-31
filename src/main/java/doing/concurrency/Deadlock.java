package doing.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://docs.oracle.com/javase/tutorial/essential/concurrency/deadlock.html
 * 
 * http://stackoverflow.com/questions/25312797/java-deadlock-code-explanation
 * thread 1: alphonse.bow(). To enter this method, thread 1 acquires the lock of
 * alphonse, since the bow() method is synchronized.
 * 
 * thread 2: gaston.bow(). To enter this method, thread 2 acquires the lock of
 * gaston, since the bow() method is synchronized.
 * 
 * thread 1: gaston.bowBack(). To enter this method, thread 1 needs to acquire
 * the lock of gaston, since the bowBack() method is synchronized. It waits
 * until thread 2 has released the lock of gaston
 * 
 * thread 2: alphonse.bowBack(). To enter this method, thread 2 needs to acquire
 * the lock of alphonse, since the bowBack() method is synchronized. It waits
 * until thread 1 has released the lock of alphonse
 * 
 * The two threads end up waiting for each other. It's a deadlock.
 * 
 * @version Date：Jan 31, 2016 5:17:17 PM
 */
public class Deadlock {
	private static final Logger log = LoggerFactory.getLogger(Deadlock.class);
	
	static class Friend {
		private final String name;

		public Friend(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public synchronized void bow(Friend bower) {
			System.out.format("%s: %s has bowed to me!%n", this.name, bower.getName());
			log.info("acquired the lock of {}, acquiring lock on {}", this.name, bower.getName());
			bower.bowBack(this);
		}

		public synchronized void bowBack(Friend bower) {
			System.out.format("%s: %s has bowed back to me!%n", this.name, bower.getName());
		}
	}

	public static void main(String[] args) {
		final Friend alphonse = new Friend("Alphonse");
		final Friend gaston = new Friend("Gaston");
		new Thread(new Runnable() {
			public void run() {
				alphonse.bow(gaston);
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				gaston.bow(alphonse);
			}
		}).start();
	}
}
