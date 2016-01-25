package demo.javabasics;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;

/**
 * 在现实的场景中经常有这样的需求，在纷繁的日志文件中，通过搜索某一个关键字（如订单号），能方便清晰的列出某一业务（如支付）的完整的处理流程。
 * 
 * <pre>
 * logger.info(&quot;[{}] entering pay&quot;, orderId);
 * logger.info(&quot;[{}] check whether order is repeated &quot;, orderId);
 * logger.info(&quot;[{}] save order to db&quot;, orderId);
 * logger.error(&quot;[{}]&quot;, orderId, e);
 * </pre>
 * 
 * 这样的话即使在多用户并发的情况下（如servlet和dubbo service）也容易根据某一关键字（如订单号、ip）来定位出完整的业务链。
 * 另外除了NDC外还有一个工具类--MDC–也适用于这一场景，使用方式和NDC差不多，仍沿用上述示例，仅需部分改动：
 * 
 * @author <a href='https://www.evernote.com/Home.action#n=50f79df9-353d-4a77-9aa5-790763b1aac6&ses=4&sh=2&sds=5&'>log4j
 *         的一个实用工具类--NDC–可在每个日志前自动加上一个tag--介绍</href>
 */
public class PracticeTestNDCDemo {
	private static final Logger logger = Logger.getLogger(PracticeTestNDCDemo.class);

	public static void main(String[] args) {
		PracticeTestNDCDemo app = new PracticeTestNDCDemo();
		app.testNdc(System.currentTimeMillis());
		app.testMdc(12345);
	}

	/**
	 * 同时配置文件中输出模板需要添加一个字符x(表示从NDC取消息)。如下所示：
	 * 
	 * <pre>
	 * <layout class="org.apache.log4j.PatternLayout"> 
	 * 	<param name="ConversionPattern" value="[%d{dd HH:mm:ss,SSS\} %-5p] [%t] %c - %x-%m%n" /> 
	 * </layout>
	 * </pre>
	 * 
	 * @param orderId
	 */
	private void testNdc(long orderId) { // 入口方法
		NDC.push("[" + orderId + "]"); // 进入方法设置tag
		logger.info("entering test1"); // 正常记录日志 无需显式添加tag
		test2();
		test3();
		test6();
		NDC.remove(); // 离开方法删除tag
	}

	/**
	 * 同时配置文件中输出模板需要添加一个字符X(表示从MDC取消息)。如下所示：
	 * 
	 * <pre>
	 * <layout class="org.apache.log4j.PatternLayout"> 
	 * 	<param name="ConversionPattern" value="[%d{dd HH:mm:ss,SSS\} %-5p] [%t] %c -%X{orderId}-%m%n" /> 
	 * </layout>
	 * </pre>
	 * 
	 * @param orderId
	 */
	private void testMdc(int orderId) {
		MDC.put("orderId", "[" + orderId + "]"); // NDC.push --> MDC.put(key , value)
		logger.info("entering test1");
		test2();
		test3();
		test6();
		MDC.remove("orderId"); // NDC.remove --> MDC.remove(key)
	}

	private void test6() {
		logger.info("entering test6");
	}

	private void test3() {
		logger.info("entering test3");
		test5();
	}

	private void test5() {
		logger.info("entering test5");
	}

	private void test2() {
		logger.info("entering test2");
		test4();
	}

	private void test4() {
		logger.info("entering test4");
	}

}
