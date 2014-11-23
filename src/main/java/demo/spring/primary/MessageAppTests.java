package demo.spring.primary;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author http://zezutom.blogspot.com/2014/01/spring-series-primary.html
 * @version Date: Oct 10, 2014 4:07:13 PM
 * *************************************************************************
 * SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:demo/spring/primary/demo-springexacples.xml")
public class MessageAppTests {

	@Autowired
	private IMessage message;

	@Test
	public void helloWorldShouldBeTheDefaultMessage() {
		Assert.assertEquals(String.format("Hello world!"), message.getMessage());
	}

	// Auto wiring the app context is comfortable and desired
	@Autowired
	private ApplicationContext context;

	// Note that the IMessage bean is now being obtained
	// directly from the application context
	@Test
	public void primaryBeanShouldBePickedByTheGetBeanCall() {
		// Now, if you assume the test will pass you are right... Provided you are lucky enough to work
		// with one of the more recent versions of the Spring framework. In my experience, any version
		// below 3.2.6 gives out about a non-unique bean
		Assert.assertEquals("Hello world!", context.getBean(IMessage.class).getMessage());
	}
}
