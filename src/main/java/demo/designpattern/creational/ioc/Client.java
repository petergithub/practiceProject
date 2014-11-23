package demo.designpattern.creational.ioc;

public class Client {
	public void doSomething1() {
		Service1 service = Service1Factory.getService1Instance();
		service.execute();
	}

	public void doSomething() {
		Service1 service1 = (Service1) ServiceLocator.lookup("service1");
		service1.execute();
	}

	public static void main(String[] args) {
		// Initialize the services
		ServiceLocator.configure();
		new Client().doSomething();
	}
}
