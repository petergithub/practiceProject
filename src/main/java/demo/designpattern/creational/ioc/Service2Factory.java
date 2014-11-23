package demo.designpattern.creational.ioc;

public class Service2Factory {
	public static Service2 getService2Instance() {
		return new Service2Impl();
	}
}

interface Service2 {
	void execute();
}

class Service2Impl implements Service2 {
	public void execute() {
		System.out.println("Service2 is doing something.");
	}
}
