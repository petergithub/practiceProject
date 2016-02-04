package doing.concurrency.oracle;

/**
 * http://docs.oracle.com/javase/tutorial/essential/concurrency/QandE/questions.html
 * @version Date：Feb 4, 2016 2:59:54 PM
 */
public class BadThreads {

	static String message;

	private static class CorrectorThread extends Thread {

		public void run() {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
			}
			// Key statement 1:
			message = "Mares do eat oats.";
		}
	}

	public static void main(String args[]) throws InterruptedException {

		(new CorrectorThread()).start();
		message = "Mares do not eat oats.";
		Thread.sleep(2000);
		// Key statement 2:
		System.out.println(message);
	}
}