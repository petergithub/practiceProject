package demo.network.rpc.framework;

import demo.network.rpc.HelloWorldService;

public class ServiceConsumer {
	public static void main(String[] args) {
		try {
			// 返回的是服务的一个代理
			HelloWorldService service = RpcServer.referService(HelloWorldService.class, "127.0.0.1");
			String message = service.sayHello("hello server");
			System.out.println("ServiceConsumer.message[" + message +"]" );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
