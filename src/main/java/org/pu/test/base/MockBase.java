package org.pu.test.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import junit.framework.Assert;

/**
 * A simple mock object using a reflection proxy: you tell it the type of interface that you want to
 * mock, and it records invocations on that interface. You can make assertions on those invocations.
 * <p>
 * All invoked methods return <code>null</code>. Override {@link #invoke} if you want to change this
 * behavior, but be sure to call the superclass implementation (otherwise it won't record the
 * invocation).
 * <p>
 * A single instance can mock multiple objects, however method invocations will be interleaved.
 * Doing so is primarily useful when you simply need a dummy object as a method parameter, and don't
 * actually use it.
 * <p>
 * refer to http://www.kdgregory.com/index.php?page=junit.proxy
 */
public class MockBase implements InvocationHandler {
	private ArrayList<String> calls = new ArrayList<String>();
	private ArrayList<Object[]> args = new ArrayList<Object[]>();

	public <T> T getInstance(Class<T> classToMock) {
		return classToMock.cast(Proxy.newProxyInstance(this.getClass().getClassLoader(),
				new Class[] { classToMock }, this));
	}

	public Object invoke(Object proxy, Method method, Object[] arg) throws Throwable {
		calls.add(method.getName());
		if (arg == null) arg = new Object[0];
		args.add(arg);
		return null;
	}

	/**
	 * Asserts that we received the expected number of invocations.
	 */
	public void assertCallCount(int expected) {
		Assert.assertEquals("call count", expected, calls.size());
	}

	/**
	 * Asserts the method name and parameter of a specific invocation. Note that calls are numbered
	 * from 0.
	 */
	public void assertCall(int callNum, String methodName, Object... arg) {
		Assert.assertEquals("incorrect method", methodName, calls.get(callNum));
		Assert.assertEquals("argument count", arg.length, args.get(callNum).length);
		for (int i = 0; i < arg.length; i++)
			Assert.assertEquals("argument " + i, arg[i], args.get(callNum)[i]);
	}
}
