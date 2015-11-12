package demo.frame.spring;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloApp {
	private static final String CLIENT_CONTEXT_CONFIG_LOCATION = "helloworld/spring/spring.xml";

	public static void main(String[] args) throws Exception {
		ListableBeanFactory factory = new ClassPathXmlApplicationContext(CLIENT_CONTEXT_CONFIG_LOCATION);
		GreetingService greetingService = (GreetingService) factory
				.getBean("greetingService");

		greetingService.sayGreeting();
		((ClassPathXmlApplicationContext)factory).close();
	}
}
