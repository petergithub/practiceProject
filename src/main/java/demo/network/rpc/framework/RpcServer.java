package demo.network.rpc.framework;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 实现一个简单的RpcServer 例子： HelloService为自定义的服务
 * 
 * <pre>
 * {
 * 	@code
 * 	// 先导出服务以供远端的客户端调用
 * 	public class ServiceProvider {
 * 		public static void main(String[] args) {
 * 			HelloService service = new HelloServiceImpl();
 * 			try {
 * 				RpcServer.exportService(service);
 * 			} catch (IOException e) {
 * 				e.printStackTrace();
 * 			}
 * 		}
 * 
 * 	}
 * }
 * </pre>
 * 
 * <pre>
 * {
 * 	@code
 * 	// 引用用远程发布的服务
 * 	public class ServiceConsumer {
 * 		public static void main(String[] args) {
 * 			try {
 * 				// 返回的是服务的一个代理
 * 				HelloService service = RpcServer.referService(HelloService.class, "127.0.0.1");
 * 				String message = service.sayHello("lili");
 * 				System.out.println(message);
 * 			} catch (Exception e) {
 * 				e.printStackTrace();
 * 			}
 * 		}
 * 	}
 * }
 * </pre>
 *
 * 该类实现的是一个简单的RpcServer，在此基础上可以进行以下的扩展
 * 线程池处理调用请求、通信层不使用BIO使用NIO甚至是NIO框架（Netty,Mina等）
 * 使用序列化框架比如protostuff、避免反射调用损耗性能使用动态代理生成代理类、增加 注册中心可以使用zk来完成......
 *
 */
public class RpcServer {
	// 服务端端口
	public static int port = 9999;

	/**
	 * 导出服务，需要做的是打开服务端口解析请求，并调用相应的方法，并返回结果
	 * 
	 * @throws IOException
	 */
	public static void exportService(final Object service) throws IOException {
		if (null == service) {
			throw new IllegalArgumentException("service is null");
		}
		System.out.println("Export service " + service + "on port " + port);

		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(port);
		for (;;) {
			try {
				final Socket socket = serverSocket.accept();
				// 一请求一线程处理
				new Thread(new Runnable() {
					public void run() {
						try {
							ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
							// 方法名
							String methodName = in.readUTF();

							Object result = null;
							try {
								result = invoke(service, in, methodName);
								ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
								try {
									out.writeObject(result);
									out.flush();
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									out.close();
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							} finally {
								in.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (null != socket) {
								try {
									socket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}

					// 解析参数并反射调用返回调用结果
					private Object invoke(final Object service, ObjectInputStream in, String methodName)
							throws Exception {
						Object result = null;
						// 参数类型
						Class<?>[] parameterTypes = (Class<?>[]) in.readObject();
						// 参数值
						Object[] args = (Object[]) in.readObject();
						try {
							// 反射获取方法名
							Method method = service.getClass().getMethod(methodName, parameterTypes);
							if (null == method) {
								throw new NoSuchMethodException();
							}
							// 反射调用
							result = method.invoke(service, args);
							// 回写结果
						} catch (Throwable t) {
							t.printStackTrace();
							result = t;
						}
						return result;
					}

				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 引入服务
	 */
	@SuppressWarnings("unchecked")
	public static <T> T referService(final Class<T> service, final String host) throws Exception {
		if (null == service) {
			throw new IllegalArgumentException("service is null");
		}
		System.out.println("refer service " + service);

		return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
				new InvocationHandler() {

					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Socket socket = new Socket(host, port);
						try {
							ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
							try {
								out.writeUTF(method.getName());
								out.writeObject(method.getParameterTypes());
								out.writeObject(args);
								ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

								try {
									Object result = in.readObject();
									if (result instanceof Throwable) {
										throw (Throwable) result;
									}
									return result;
								} finally {
									in.close();
								}
							} finally {
								out.close();
							}
						} finally {
							socket.close();
						}
					}
				});
	}
}
