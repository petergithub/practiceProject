package demo.designpattern.structural.proxy;

import static java.lang.reflect.Proxy.newProxyInstance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
public class ProxyClient {
	public static void main(String[] args) {
		// IncreaseImpl test = new IncreaseImpl(157);// run synchronizedIncrease
		// max=157 times
		ProxyClient proxy = new ProxyClient();
		System.out.println("Test non-thread-safe increment...");
		proxy.testIncrease();
		System.out.println("Test non-thread-safe increment successful.");
		System.out.println("Test thread-safe increment...");
		proxy.testSynchronizedIncrease();
		System.out.println("Test thread-safe increment successful.");
	}
	
	public void testIncrease() {
		boolean illegalState = false;
		while (!illegalState) {
			try {
				IncreaseImpl increasable = new IncreaseImpl();
				increasable.increase(increasable);
			} catch (Throwable e) {
				// assert expected exception message
				Assert.assertEquals("Count state is illegal!", e.getMessage());
				illegalState = true;
			}
		}
		Assert.assertEquals(illegalState, true);
	}

	@org.junit.Test
	public void testSynchronizedIncrease() {
		IncreaseImpl increasable = ProxySynchronizedHandler
				.newProxy(new IncreaseImpl());
		for (int i = 0; i < increasable.getMax(); i++) {
			try {
				increasable.increase(increasable);
			} catch (Throwable e) {
				// fail to test, throw a new exception
				throw new RuntimeException("Synchronized Test Failed", e);
			}
		}
	}
}
class ProxySynchronizedHandler implements InvocationHandler {
	private IncreaseImpl delegate;

	public static IncreaseImpl newProxy(IncreaseImpl delegate) {
		IncreaseImpl o = (IncreaseImpl) newProxyInstance(delegate.getClass()
				.getClassLoader(), delegate.getClass().getInterfaces(),
				new ProxySynchronizedHandler(delegate));
		return o;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		synchronized (delegate) {
			return method.invoke(delegate, args);
		}
	}

	public ProxySynchronizedHandler(IncreaseImpl delegate) {
		this.delegate = delegate;
	}
}

class IncreaseImpl implements Increasable {
	private int max = 100000;
	private long count;

	@Override
	public void increase(int delta) {
		count = 0;
		for (int i = 0; i < getMax(); i++) {
			count += delta;
		}
		if (count % delta != 0) {
			throw new IllegalStateException("Count state is illegal!");
		}
	}

	public void increase(final IncreaseImpl increasable) throws Throwable {
		ExecutorService pool = Executors.newFixedThreadPool(2);
		Future<?> f1 = pool.submit(new Runnable() {
			@Override
			public void run() {
				increasable.increase(13);
			}
		});
		Future<?> f2 = pool.submit(new Runnable() {
			@Override
			public void run() {
				increasable.increase(19);
			}
		});
		try {
			f1.get();
			f2.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			throw e.getCause();
		} finally {
			pool.shutdown();
		}
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
}

interface Increasable {
	void increase(int delta);
}
