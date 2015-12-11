package demo.network.rpc.simple;

import org.junit.Test;

import demo.network.rpc.HelloWorldService;
import demo.network.rpc.HelloWorldServiceImpl;

public class TestRpc {

	public void testRpc() {
		Object proxy = RPCProxyClient.getProxy(HelloWorldService.class);
		HelloWorldService helloWorldService = (HelloWorldService) proxy;
		String result = helloWorldService.sayHello("test");
		System.out.println("calling from main: " + result);
	}
	
	@Test
	public void testRpcProxyObject() {
		Object proxy = RPCProxyClient.getProxyObjectFailedCast(HelloWorldService.class);
		HelloWorldService helloWorldService = (HelloWorldService) proxy;
		String result = helloWorldService.sayHello("test");
		System.out.println("calling from main: " + result);
	}

	public void testNormal() {
		HelloWorldService helloWorldService = new HelloWorldServiceImpl();
		helloWorldService.sayHello("test");
	}
}