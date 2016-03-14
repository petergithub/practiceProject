package doing.concurrency.future.jdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 由于Future是非常常用的多线程设计模式，因此在JDK中内置了Future模式的实现。这些类在java.util.concurrent包里面。
 * 其中最为重要的是FutureTask类
 * ，它实现了Runnable接口，作为单独的线程运行。在其run()方法中，通过Sync内部类调用Callable接口，
 * 并维护Callable接口的返回对象。
 * 当使用FutureTask.get()方法时，将返回Callable接口的返回对象。同样，针对上述的实例，如果使用JDK自带的实现，则需要作如下调整。
 * 
 * 首先，Data接口和FutureData就不需要了，JDK帮我们实现了。
 * 
 * @author Shang Pu
 * @version Date：Dec 15, 2015 4:45:57 PM
 */
public class Application {
	public static void main(String[] args) throws Exception {
		FutureTask<String> futureTask = new FutureTask<String>(new RealData("name"));
		ExecutorService executor = Executors.newFixedThreadPool(1); // 使用线程池
		// 执行FutureTask，相当于上例中的client.request("name")发送请求
		executor.submit(futureTask);
		// 这里可以用一个sleep代替对其他业务逻辑的处理
		// 在处理这些业务逻辑过程中，RealData也正在创建，从而充分了利用等待时间
		Thread.sleep(2000);
		// 使用真实数据
		// 如果call()没有执行完成依然会等待
		System.out.println("数据=" + futureTask.get());
	}
}