package demo.proxy;

import org.junit.Test;

public class TestProxy {
	@Test
	public void testStaticProxy() {
		Subject sub = new SubjectStaticProxy();
		sub.doSomething();
	}

	@Test
	public void testDynamicProxy() {
		// 绑定该类实现的所有接口
		Subject sub = (Subject) SubjectDynamicProxy.getProxy(new SubjectImpl());
		sub.doSomething();
	}
}