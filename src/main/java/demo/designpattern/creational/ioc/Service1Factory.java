package demo.designpattern.creational.ioc;

public class Service1Factory {
	public static Service1 getService1Instance() {
		Service2 service2 = Service2Factory.getService2Instance();
		return new Service1Impl(service2);
	}
}

interface Service1 {
	void execute();
}

class Service1Impl implements Service1 {
	private Service2 service2;

	public Service1Impl(Service2 service2) {
		this.service2 = service2;
	}

	public void execute() {
		System.out.println("Service1 is doing something.");
		service2.execute();
	}
}
