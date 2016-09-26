package doing.concurrency.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 如果不想分支线程阻塞主线程，又想取得分支线程的执行结果，就用FutureTask
 * 
 * http://westyi.iteye.com/blog/714935
 * 
 * Future模式可以这样来描述：我有一个任务，提交给了Future，Future替我完成这个任务。期间我自己可以去做任何想做的事情。一段时
 * 间之后，我就便可以从Future那儿取出结果。就相当于下了一张订货单，一段时间后可以拿着提订单来提货，这期间可以干别的任何事情。其中Future
 * 接口就是订货单，真正处理订单的是Executor类，它根据Future接口的要求来生产产品。
 * 
 * Future接口提供方法来检测任务是否被执行完，等待任务执行完获得结果，也可以设置任务执行的超时时间。这个设置超时的方法就是实现Java程
 * 序执行超时的关键。
 * 
 * @author Shang Pu
 * @version Date: Jul 11, 2016 6:37:13 PM
 */
public class PracticeFuture extends TestBase {

	private static final Logger log = LoggerFactory.getLogger(PracticeFuture.class);

	@Test
	public void test() {
		ExecutorService executor = getExecutorService1();
		executor = getExecutorService2();

		// 使用Callable接口作为构造参数
		FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
			public String call() {
				// 真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
				return "test";
			}
		});
		executor.execute(future);

		// 在这里可以做别的任何事情

		try {
			// 取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果
			String result = future.get(5000, TimeUnit.MILLISECONDS);
			log.info("result[{}]", result);
		} catch (InterruptedException e) {
			future.cancel(true);
		} catch (ExecutionException e) {
			future.cancel(true);
		} catch (TimeoutException e) {
			future.cancel(true);
		} finally {
			executor.shutdown();
		}
	}

	private ExecutorService getExecutorService1() {
		return Executors.newSingleThreadExecutor();
	}

	/**
	 * 异步执行文件上传的线程池，由于是IO型为主的操作行为，所以这里建议开尽量多的线程，这里的线程
	 * 全部设置为demanon是为了当程序退出的时候所有的线程自动结束
	 * 
	 * @return
	 */
	private ExecutorService getExecutorService2() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		ExecutorService THREAD_POOL = new ThreadPoolExecutor(availableProcessors,
				availableProcessors * 10, 60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(100), new ThreadFactory() {

					@Override
					public Thread newThread(Runnable r) {
						Thread thread = new Thread(r);
						thread.setDaemon(true);

						thread.setPriority(4);
						return thread;
					}

				}, new ThreadPoolExecutor.CallerRunsPolicy());
		return THREAD_POOL;
	}
}
