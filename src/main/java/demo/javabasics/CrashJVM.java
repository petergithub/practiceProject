package demo.javabasics;

/**
 * @author Shang Pu
 * @version Date: Jul 18, 2012 4:39:02 PM
 */
public class CrashJVM {

	public void testMemoryCrash() {
		int max = 100;
		Runtime rt = Runtime.getRuntime();
		Object[] arr = new Object[max];
		for (int n = 0; n < max; n++) {
			System.out.println("Having " + n * 10 + " MB and adding 10 MB...");
			arr[n] = new long[10 * 1024 * 128];
			System.out.println(" Free memory: " + rt.freeMemory());
			System.out.println("Total memory: " + rt.totalMemory());
			try {
				Thread.sleep(2 * 1000);
			} catch (InterruptedException e) {
				System.out.println("Interreupted...");
			}
		}
	}

	static long n;
	public void testRecursiveCrash() {
		n = 0;
		recursive();
	}

	private static void recursive() {
		n++;
		try {
			recursive();
		} catch (StackOverflowError e) {
			System.out.println("Stack overflow error at: " + n);
		}
	}
}
