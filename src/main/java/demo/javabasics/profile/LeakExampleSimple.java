package demo.javabasics.profile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

// PrefTest 性能测试工作室 - http://cnblogs.com/preftest
public class LeakExampleSimple {
	static Vector<String> myVector = new Vector<String>();
	static HashSet<Integer> pendingRequests = new HashSet<Integer>();

	public static void main(String[] args) throws IOException {
		LeakExampleSimple javaLeaks = new LeakExampleSimple();
		for (int i = 0; true; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) { /* 不做任何事情 */
			}
			System.out.println("Iteration: " + i);
			javaLeaks.slowlyLeakingVector(1000, 10);
			javaLeaks.leakingRequestLog(5000);
			javaLeaks.noLeak(100000);
		}
	}

	public void slowlyLeakingVector(int iter, int count) {
		for (int i = 0; i < iter; i++) {
			for (int n = 0; n < count; n++) {
				myVector.add(Integer.toString(n + i));
			}
			for (int n = count - 1; n > 0; n--) {
				// 应该是 n>=0
				myVector.removeElementAt(n);
			}
		}
	}

	public void leakingRequestLog(int iter) {
		Random requestQueue = new Random();
		for (int i = 0; i < iter; i++) {
			int newRequest = requestQueue.nextInt();
			pendingRequests.add(new Integer(newRequest));
			// 忘记移除对象了
		}
	}

	public void noLeak(int size) {
		HashSet<String> tmpStore = new HashSet<String>();
		for (int i = 0; i < size; ++i) {
			String leakingUnit = new String("Object: " + i);
			tmpStore.add(leakingUnit);
		}
		// 虽然在函数内分配了很多内存，但是函数执行完后，这些内存都能被“垃圾回收”
	}
}
