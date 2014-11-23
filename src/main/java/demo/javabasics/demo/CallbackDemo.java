package demo.javabasics.demo;

import org.pu.utils.Utils.ExecutionTimer;

// 下面使用java回调函数来实现一个测试函数运行时间的工具类: 

// 如果我们要测试一个类的方法的执行时间，通常我们会这样做: 
class TestObject {
	/**
	 * 一个用来被测试的方法，进行了一个比较耗时的循环
	 */
	public static void testMethod() {
		for (int i = 0; i < 100000000; i++) {
		}
	}

	/**
	 * 一个简单的测试方法执行时间的方法
	 */
	public void testTime() {
		long begin = System.currentTimeMillis(); // 测试起始时间
		testMethod(); // 测试方法
		long end = System.currentTimeMillis(); // 测试结束时间
		System.out.println("[use time]:" + (end - begin)); // 打印使用时间
	}

	public static void main(String[] args) {
		TestObject test = new TestObject();
		test.testTime();
	}
}

// 大家看到了testTime()方法，就只有"//测试方法"是需要改变的，下面我们来做一个函数实现相同功能但更灵活: 
// 首先定一个回调接口: (we will use utils.CallBack in this example)
// interface CallBack {
// 执行回调操作的方法
// void execute();
// }

// 然后再写一个工具类Tools
class Tools {
	/**
	 * 测试函数使用时间，通过定义CallBack接口的execute方法
	 */
	public void testTime(ExecutionTimer timeConsume) {
		long begin = System.currentTimeMillis(); // 测试起始时间
		timeConsume.execute(); // /进行回调操作
		long end = System.currentTimeMillis(); // 测试结束时间
		System.out.println("[use time]:" + (end - begin)); // 打印使用时间
	}
}

public class CallbackDemo {
	public static void main(String[] args) {
		Tools tool = new Tools();
		tool.testTime(new ExecutionTimer() {
			// 定义execute方法
			public void execute() {
				// 这里可以加放一个或多个要测试运行时间的方法
				TestObject.testMethod();
			}
		});
	}
}
