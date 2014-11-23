package demo.designpattern.creational.ioc;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
	private static ServiceLocator locator;
	private Map<String, Object> services = new HashMap<String, Object>();

	public static void configure() {
		load(new ServiceLocator());
		Service2 service2 = new Service2Impl();
		locator.services.put("service2", service2);
		Service1 service1 = new Service1Impl(service2);
		locator.services.put("service1", service1);
	}

	private static void load(ServiceLocator serviceLocator) {
		locator = serviceLocator;
	}

	public static Object lookup(String serviceName) {
		return locator.services.get(serviceName);
	}

	public static void registerService(String name, Object service) {
		locator.services.put(name, service);
	}
}
