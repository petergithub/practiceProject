package demo.network.rpc.simple;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * http://blog.jobbole.com/92290/
 * 
 * 怎么封装通信细节才能让用户像以本地调用方式调用远程服务呢？对java来说就是使用代理！java代理有两种方式：1） jdk
 * 动态代理；2）字节码生成。尽管字节码生成方式实现的代理更为强大和高效，但代码不易维护，大部分公司实现RPC框架时还是选择动态代理方式。
 * 
 * 下面简单介绍下动态代理怎么实现我们的需求。我们需要实现RPCProxyClient代理类，代理类的invoke方法中封装了与远端服务通信的细节，
 * 消费方首先从RPCProxyClient获得服务提供方的接口
 * ，当执行helloWorldService.sayHello(“test”)方法时就会调用invoke方法
 * 
 * @author Shang Pu
 * @version Date：Dec 10, 2015 4:13:28 PM
 * @param <T>
 */
public class RPCProxyClient<T> implements InvocationHandler {

	private T obj;

	public RPCProxyClient(T obj) {
		this.setObj(obj);
	}

	/**
	 * 得到被代理对象;
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxy(final Class<T> obj) {
		return (T) Proxy.newProxyInstance(obj.getClassLoader(), new Class<?>[] { obj },
				new RPCProxyClient<Class<T>>(obj));
	}

	public static Object getProxyObjectFailedCast(final Object obj) {
//		return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass()
//				.getInterfaces(), new RPCProxyClient<Object>(obj));
		

		Object proxy = Proxy.newProxyInstance(obj.getClass().getClassLoader(),
				obj.getClass()
				.getInterfaces(), new RPCProxyClient<Object>(obj));
		return proxy;
	}

	/**
	 * 调用此方法执行
	 */
	public String invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 结果参数;
		String result = new String("invoke");
		System.out.println("print from invoke: " + result);
		// ...执行通信相关逻辑
		return result;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}
}
