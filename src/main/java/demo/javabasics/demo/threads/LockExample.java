package demo.javabasics.demo.threads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 主要相同点: Lock 能完成 synchronized 所实现的所有功能
 * <p>
 * 主要不同点: Lock 有比 synchronized 更精确的语义和更好的性能。synchronized 会自动释放锁，而 Lock
 * 一定要求程序员手动释放锁，而且必须在 finally 从句中释放
 * refer to http://www.javalobby.org/java/forums/t45090.html
 * <pre>
 * // Not so Good
 * public class MyClass {
 * 	private SomeUnsafeResource opAResource;
 * 	private SomeOtherUnsafeResource opBResource;
 * 
 * 	// not related to doOperationB in anyway
 * 	public synchronized void doOperationA(String parameter) {
 * 		// This operation doesn't involve the resource
 * 		Object result = doExpensiveCalculation(parameter);
 * 		opAResource.doSingleThreadOperation(result);
 * 	}
 * 
 * 	public synchronized Object doOperationB(String parameter) {
 * 		Object result = opBResource.doOtherSingleThreadOperation(parameter);
 * 		// This operation doesn't involve the resource
 * 		return doSomePostProcessing(result);
 * 	}
 * }
 * 
 * // Better
 * public class MyClass {
 * 	// Use unique locks for independent operations
 * 	Lock opALock = new ReentrantLock();
 * 	Lock opBLock = new ReentrantLock();
 * 	private SomeUnsafeResource opAResource;
 * 	private SomeOtherUnsafeResource opBResource;
 * 
 * 	// not related to doOperationB in anyway
 * 	public void doOperationA(String parameter) {
 * 		// This operation doesn't involve the resource
 * 		Object result = doExpensiveCalculation(parameter);
 * 		// only lock around the single threaded part of the operation.
 * 		opALock.lock();
 * 		try {
 * 			opAResource.doSingleThreadOperation(result);
 * 		} finally {
 * 			opALock.unlock();
 * 		}
 * 	}
 * 
 * 	public Object doOperationB(String parameter) {
 * 		opBLock.lock();
 * 		Object result = null;
 * 		try {
 * 			result = opBResource.doOtherSingleThreadOperation(parameter);
 * 		} finally {
 * 			opB.unlock();
 * 		}
 * 		// This operation doesn't involve the resource
 * 		return doSomePostProcessing(result);
 * 	}
 * }
 * </pre>
 * @author Shang Pu
 * @version Date: Apr 16, 2012 5:16:11 PM
 */

public class LockExample {

	{
		// pre Java 5 code:
		Object lockNeed = new Object();
		// ...
		synchronized (lockNeed) {
			// do something here that requires synchronized access
		}

		// Java 5 Code Using Locks
		Lock lock = new ReentrantLock();
		lock.lock();
		try {
			// do something here that requires synchronized access
		} finally {
			lock.unlock();
		}
	}
}
