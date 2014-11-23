package demo.designpattern.creational;

public class FactoryMethodClient {
	private Factory factory;

	public FactoryMethodClient(Factory factory) {
		this.factory = factory;
	}

	public void doSomething() {
		@SuppressWarnings("unused")
		Product product = factory.createProduct();
		// to do something...
	}

	public static void main(String[] args) {
		FactoryMethodClient client = new FactoryMethodClient(new ConcreteFactory());
		client.doSomething();
	}
}

class ConcreteFactory implements Factory {
	@Override
	public Product createProduct() {
		return new ConcreteProduct();
	}
}

interface Factory { // an interface mainly to create Product objects
	Product createProduct();
}

class ConcreteProduct implements Product {
}

interface Product {
}
