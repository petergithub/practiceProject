package doing.concurrency.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PracticeThreadPool {
	private static final Logger log = LoggerFactory.getLogger(PracticeThreadPool.class);

	static class ThreadPoolExecutorCustom {
		public static void main(String[] args) {
			// 创建等待队列
			BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<Runnable>(20);
			// 创建线程池，池中保存的线程数为3，允许的最大线程数为5
			ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 5, 50, TimeUnit.MILLISECONDS,
					bqueue);
			// 创建七个任务
			Runnable t1 = new SleepThread();
			Runnable t2 = new SleepThread();
			Runnable t3 = new SleepThread();
			Runnable t4 = new SleepThread();
			Runnable t5 = new SleepThread();
			Runnable t6 = new SleepThread();
			Runnable t7 = new SleepThread();
			// 每个任务会在一个线程上执行
			pool.execute(t1);
			pool.execute(t2);
			pool.execute(t3);
			pool.execute(t4);
			pool.execute(t5);
			pool.execute(t6);
			pool.execute(t7);
			// 关闭线程池
			pool.shutdown();
		}
	}

	static class ExecutorServiceDiscardRejectPolicy {
		private static final int THREADS_SIZE = 1;
		private static final int CAPACITY = 1;

		public static void main(String[] args) throws Exception {

			// 创建线程池。线程池的"最大池大小"和"核心池大小"都为1(THREADS_SIZE)，"线程池"的阻塞队列容量为1(CAPACITY)。
			ThreadPoolExecutor pool = new ThreadPoolExecutor(THREADS_SIZE, THREADS_SIZE, 0,
					TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(CAPACITY));
			
			// run the first 
//			pool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
			
			// run the last one
//			pool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
			
			// throws error
//			pool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());  
			
			// different execute order
			pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());  

			// 新建10个任务，并将它们添加到线程池中。
			for (int i = 0; i < 10; i++) {
				Runnable myrun = new NameSleepThread("task-" + i);
				pool.execute(myrun);
			}
			// 关闭线程池
			pool.shutdown();
		}
	}

	static class ExecutorServiceCustom1 {
		public static void main(String[] args) {
			int processors = Runtime.getRuntime().availableProcessors();
			int corePoolSize = processors;
			int maximumPoolSize = processors * 5;
			int capacity = 2;
			corePoolSize = 2;
			maximumPoolSize = corePoolSize * 5;

			LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(capacity);
			ExecutorService THREAD_POOL = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60L,
					TimeUnit.SECONDS, workQueue, new ThreadFactory() {

						@Override
						public Thread newThread(Runnable r) {
							Thread thread = new Thread(r);
							thread.setDaemon(true);

							thread.setPriority(4);
							return thread;
						}

					}, new ThreadPoolExecutor.AbortPolicy());

			int maxSafetyThread = maximumPoolSize + capacity;
			int intRejectedExecutionException = maxSafetyThread + 1;

			for (int i = 0; i < intRejectedExecutionException + 2; i++) {
				log.info("workQueue.size(): {}", workQueue.size());
				try {
					THREAD_POOL.execute(new SleepThread());
				} catch (RejectedExecutionException e) {
					log.error("Exception in PracticeThreadPool.ExecutorServiceCustom1.main()", e);
				}
				System.out.println("************* iterate " + i + " *************");
			}
			THREAD_POOL.shutdown();
		}
	}

	static class ExecutorServiceCustom2 {
		public static void main(String[] args) {
			// ExecutorService executorService =
			// Executors.newCachedThreadPool();
			ExecutorService executorService = Executors.newFixedThreadPool(5);
			// ExecutorService executorService =
			// Executors.newSingleThreadExecutor();
			for (int i = 0; i < 15; i++) {
				executorService.execute(new EmptyThread());
				System.out.println("************* a" + i + " *************");
			}
			executorService.shutdown();
		}
	}

	static class ExecutorServiceExecuteCallable {
		public static void main(String[] args) {
			ExecutorService executorService = Executors.newCachedThreadPool();
			List<Future<String>> resultList = new ArrayList<Future<String>>();

			// 创建10个任务并执行
			for (int i = 0; i < 10; i++) {
				// 使用ExecutorService执行Callable类型的任务，并将结果保存在future变量中
				Future<String> future = executorService.submit(new TaskWithResult(i));
				// 将任务执行结果存储到List中
				resultList.add(future);
			}

			// 遍历任务的结果
			for (Future<String> fs : resultList) {
				try {
					while (!fs.isDone())
						;// Future返回如果没有完成，则一直循环等待，直到Future返回完成
					System.out.println(fs.get()); // 打印各个线程（任务）执行的结果
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} finally {
					// 启动一次顺序关闭，执行以前提交的任务，但不接受新任务
					executorService.shutdown();
				}
			}
		}

		static class TaskWithResult implements Callable<String> {
			private int id;

			public TaskWithResult(int id) {
				this.id = id;
			}

			/**
			 * 任务的具体过程，一旦任务传给ExecutorService的submit方法， 则该方法自动在一个线程上执行
			 */
			public String call() throws Exception {
				System.out.println("call()方法被自动调用！！！    " + Thread.currentThread().getName());
				// 该返回结果将被Future的get方法得到
				return "call()方法被自动调用，任务返回的结果是：" + id + "    " + Thread.currentThread().getName();
			}
		}
	}

	static class SleepThread implements Runnable {
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + "正在执行。。。");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static class EmptyThread implements Runnable {
		public void run() {
			System.out.println(Thread.currentThread().getName() + "线程被调用了。");
		}
	}

	static class NameSleepThread implements Runnable {
		private String name;

		public NameSleepThread(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			try {
				System.out.println(this.name + " is running.");
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}