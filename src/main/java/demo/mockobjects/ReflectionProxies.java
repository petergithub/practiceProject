package demo.mockobjects;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * Creating Mocks and Stubs with Reflection Proxies
 * <p>
 * @author kdgregory
 * @version Date: May 13, 2012 2:23:29 PM
 *          refer to http://www.kdgregory.com/index.php?page=junit.proxy
 */
public class ReflectionProxies {
	protected static final Logger logger = Logger
			.getLogger(ReflectionProxies.class);

	/**
	 * Reflection proxies, added to the API with JDK 1.3, allow you to
	 * create a runtime object that implements an interface. Unlike a
	 * compiled class, this object doesn't have to fully implement the
	 * interface; instead, you can pick and choose the methods you want.
	 * This ability is what makes proxies especially useful as stub objects
	 * for testing.
	 * There are two parts to a proxy: first is the proxy object itself, an
	 * instance of Proxy. Second is an implementation of InvocationHandler.
	 * Creating the proxy instance is simple: you call
	 * Proxy.newProxyInstance(), passing it information about the interfaces
	 * your proxy will implement. For example, to create a proxy for
	 * ResultSet:
	 */
	public void testReflectionProxies() {
		ResultSet rslt = (ResultSet) Proxy.newProxyInstance(
				ResultSet.class.getClassLoader(),
				new Class[] { ResultSet.class }, new MyResultSetHandler());
		executeInTest(rslt);

		// ReflectionProxies More Flexibility
		ResultSet rsltFlexibility = new MyProxy().toStub(ResultSet.class);
		executeInTest(rsltFlexibility);
	}

	private void executeInTest(ResultSet rslt) {
		try {
			for (int i = 0; rslt.next() && i < 1; i++) {
				String foo = rslt.getString(0);
				logger.info("foo = " + foo + " in method executeInTest()");
			}
			rslt.getArray(0);
		} catch (UnsupportedOperationException e) {
			logger.error("Expected Exception when call rslt.getArray()", e);
		} catch (SQLException e) {
			logger.error("Exception in ReflectionProxies.executeInTest()", e);
		}
	}

	/**
	 * A Pattern for Building Proxies
	 * I noted earlier that I like my invocation handlers to be self-contained.
	 * To this end, they all tend to look like the following:
	 * <p>
	 * The last piece of interest is the toStub() method: this is boilerplate
	 * code, but is tied to the particular proxy. While you could simply copy it
	 * into every test method, that would lead to more maintenance.
	 */
	private static class MyProxy implements InvocationHandler {
		// methods to configure proxy return values; use Builder pattern

		// assertions that particular methods were called
		public <T> T toStub(Class<T> klassToBeProxied) {
			return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { klassToBeProxied }, this);
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// if-else chain for expected methods
			if (method.getName().equals("next"))
				return Boolean.TRUE;
			else if (method.getName().equals("getString")) return "foo";

			throw new UnsupportedOperationException(method.getName());
		}
	}

	private static class MyResultSetHandler implements InvocationHandler {
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if (method.getName().equals("next"))
				return Boolean.TRUE;
			else if (method.getName().equals("getString")) return "foo";

			throw new UnsupportedOperationException(method.getName());
		}
	}

	/**
	 * Example: closeQuietly()
	 * <p>
	 * Imagine yourself in a world that doesn't have the Jakarta Commons
	 * DbUtils, and you are tasked with writing an equivalent of the
	 * closeQuietly() method. You would probably write test cases that exercise
	 * the three possible outcomes (success, SQLException,
	 * NullPointerException), and you'd like to verify that your method is
	 * actually closing the ResultSet. Using a proxy object, you might end up
	 * with test methods like these:
	 */
	private void closeQuietly(ResultSet resultSet) {
		// close resultSet
		try {
			if (resultSet != null) resultSet.close();
		} catch (SQLException e) {
			logger.error("Exception in ReflectionProxies.closeQuietly()", e);
		}
	}

	public void testCloseQuietly() throws Exception {
		ResultSetProxy proxy = new ResultSetProxy();
		closeQuietly(proxy.toStub());
		proxy.assertCloseCalled();
	}

	public void testCloseQuietlyWithException() throws Exception {
		ResultSetProxy proxy = new ResultSetProxy().setThrowOnClose(true);
		closeQuietly(proxy.toStub());
		proxy.assertCloseCalled();
	}

	@org.junit.Test
	public void testCloseQuietlyWhenNull() throws Exception {
		closeQuietly(null);
	}

	/**
	 * This example shows all of the pieces that I described above:
	 * <p>
	 * A builder pattern for configuration: we create the object then call an
	 * explicit setter method to configure special behavior. The default
	 * behavior requires no explicit configuration.
	 * <p>
	 * Using a toStub() method instead of repeating the boilerplate code in each
	 * test.
	 * <p>
	 * Asserting that the expected interaction actually happened.
	 * <p>
	 * Here's the proxy class that makes this work:
	 */
	private static class ResultSetProxy implements InvocationHandler {
		private boolean _throwOnClose;
		private boolean _closeCalled;

		public ResultSetProxy setThrowOnClose(boolean value) {
			_throwOnClose = value;
			return this;
		}

		public void assertCloseCalled() {
			junit.framework.Assert.assertTrue("close not called", _closeCalled);
		}

		public ResultSet toStub() {
			return (ResultSet) Proxy.newProxyInstance(this.getClass()
					.getClassLoader(), new Class[] { ResultSet.class }, this);
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			if (method.getName().equals("close")) {
				_closeCalled = true;
				if (_throwOnClose)
					throw new SQLException("something happened here");
				else
					return null;
			}
			throw new UnsupportedOperationException(method.getName());
		}
	}
}
