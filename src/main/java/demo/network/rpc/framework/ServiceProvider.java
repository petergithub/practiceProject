package demo.network.rpc.framework;

import java.io.IOException;

import demo.network.rpc.HelloWorldService;
import demo.network.rpc.HelloWorldServiceImpl;

public class ServiceProvider {
	public static void main(String[] args) {
		HelloWorldService service = new HelloWorldServiceImpl();
		try {
			RpcServer.exportService(service);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}