package doing.concurrency.threads;

/**
 * DinerThread.java
 * Copyright (c) 2002 by Dr. Herong Yang
 */
import java.util.Random;

/**
 * The story of 5 dining philosophers is the most interesting illustration of
 * the deadlock problem. Here is the version of the story from the
 * "The Java Tutorial" by Sun Microsystems.
 * "Five philosophers are sitting at a round table. In front of each philosopher is a bowl of rice. Between each pair of philosophers is one chopstick. Before an individual philosopher can take a bite of rice, he must have two chopsticks, one taken from the left, and one taken from the right. The philosophers must find some way to share chopsticks such that they all get to eat. "
 * Deadlock will occur in this story, if all philosophers are taking the
 * chopstick from the left side at the same time, and no one is willing to put
 * down the chopstick until he gets the chopstick on the right side.
 * <p>
 * In the program, the diner table is represented by an array of characters. '|'
 * represents a chopstick, '-', '=' and 'o' represent a philosopher with no
 * chopstick, one chopstick and two chopsticks respectively.
 * <p>
 * There are 5 threads representing the 5 philosophers. Each of them will pause
 * for some time before trying to synchronize with the lock that is associated
 * with the chopstick on the left side. Once passed the first synchronization,
 * the thread will take the chopstick on the left side, and continue to try to
 * synchronize with the lock that is associated with the chopstick on the right
 * side. Once passed the second synchronization, the thread will take the
 * chopstick on the right side, and hold both chopsticks for a moment. The
 * threads will then put back both chopsticks and release both locks.
 * <p>
 * Execute the program several times. You will see that it behaves differently
 * each time. But soon or later, the diner table will be locked with each thread
 * holding a lock that is needed by the thread on the left side, and waiting for
 * a lock that is held by the thread on the right side.
 */
public class DinerThread extends Thread {
	public static final int numberOfThreads = 5;
	public static Object[] listOfLocks = new Object[numberOfThreads];
	public static char[] dinerTable = new char[4 * numberOfThreads];
	public static char[] lockedDiner = new char[4 * numberOfThreads];
	public static Random randomGenerator = new Random();
	public static int unitOfTime = 500;
	private int threadIndex;

	public static void main(String[] a) {
		for (int i = 0; i < numberOfThreads; i++)
			listOfLocks[i] = new Object();
		for (int i = 0; i < numberOfThreads; i++) {
			dinerTable[4 * i] = '|';
			dinerTable[4 * i + 1] = ' ';
			dinerTable[4 * i + 2] = '-';
			dinerTable[4 * i + 3] = ' ';
			lockedDiner[4 * i] = ' ';
			lockedDiner[4 * i + 1] = '|';
			lockedDiner[4 * i + 2] = '=';
			lockedDiner[4 * i + 3] = ' ';
		}
		for (int i = 0; i < numberOfThreads; i++) {
			Thread t = new DinerThread(i);
			t.setDaemon(true);
			t.start();
		}
		String lockedString = new String(lockedDiner);
		System.out.println("The diner table:");
		long step = 0;
		while (true) {
			step++;
			System.out.println((new String(dinerTable)) + "   " + step);
			if (lockedString.equals(new String(dinerTable))) break;
			try {
				Thread.sleep(unitOfTime);
			} catch (InterruptedException e) {
				System.out.println("Interrupted.");
			}
		}
		System.out.println("The diner is locked.");
	}

	public DinerThread(int i) {
		threadIndex = i;
	}

	public void run() {
		while (!isInterrupted()) {
			try {
				sleep(unitOfTime * randomGenerator.nextInt(6));
			} catch (InterruptedException e) {
				break;
			}
			// Try to get the chopstick on the left
			Object leftLock = listOfLocks[threadIndex];
			synchronized (leftLock) {
				int i = 4 * threadIndex;
				dinerTable[i] = ' ';
				dinerTable[i + 1] = '|';
				dinerTable[i + 2] = '=';
				try {
					sleep(unitOfTime * 1);
				} catch (InterruptedException e) {
					break;
				}
				// Try to get the chopstick on the right
				Object rightLock = listOfLocks[(threadIndex + 1)
						% numberOfThreads];
				synchronized (rightLock) {
					dinerTable[i + 2] = 'o';
					dinerTable[i + 3] = '|';
					dinerTable[(i + 4) % (4 * numberOfThreads)] = ' ';
					try {
						sleep(unitOfTime * 1);
					} catch (InterruptedException e) {
						break;
					}
					dinerTable[i] = '|';
					dinerTable[i + 1] = ' ';
					dinerTable[i + 2] = '-';
					dinerTable[i + 3] = ' ';
					dinerTable[(i + 4) % (4 * numberOfThreads)] = '|';
				}
			}
		}
	}
}
