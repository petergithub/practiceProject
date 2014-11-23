package demo.javabasics.demo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;
import org.pu.utils.IoUtils;


/**
 * this class is used for test method testSerializable
 * <p>
 * 完全定制序列化过程: 如果一个类要完全负责自己的序列化 ，则实现Externalizable接口而不是Serializable接口。
 * Externalizable接口定义包括两个方法writeExternal()与readExternal()。
 * 利用这些方法可以控制对象数据成员如何写入字节流.类实现
 * Externalizable时，头写入对象流中，然后类完全负责序列化和恢复数据成员，除了头以外，根本没有自动序列化。
 * 这里要注意了。声明类实现Externalizable接口会有重大的安全风险。
 * writeExternal()与readExternal()方法声明为public，恶意类可以用这些方法读取和写入对象数据
 * 。如果对象包含敏感信息，则要格外小心。这包括使用安全套接或加密整个字节流
 */
public class SerializableDemo implements java.io.Serializable {
	private static final long serialVersionUID = -6755165284431348963L;
	protected static final Logger logger = Logger
			.getLogger(SerializableDemo.class);

	private java.util.Date date = new java.util.Date();
	private String username;
	private transient String password;

	public void testSerializable() throws IOException, ClassNotFoundException {
		// java.io包有两个序列化对象的类。ObjectOutputStream负责将对象写入字节流，ObjectInputStream从字节流重构对象。
		// 我们先了解ObjectOutputStream类吧。ObjectOutputStream类扩展DataOutput接口。
		// writeObject()方法是最重要的方法，用于对象序列化。如果对象包含其他对象的引用，则writeObject()方法递归序列化
		// 这些对象。每个ObjectOutputStream维护序列化的对象引用表，防止发送同一对象的多个拷贝。（这点很重要）由于
		// writeObject()可以序列化整组交叉引用的对象，因此同一ObjectOutputStream实例可能不小心被请求序列化同一对象。
		// 这时，进行反引用序列化，而不是再次写入对象字节流。
		SerializableDemo serial = new SerializableDemo("Morgan",
				"morgan83");
		logger.info("before writeObject: logon a = " + serial); // 输出原来object的信息

		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
				"Logon.out"));
		out.writeObject(serial);
		out.close();

		int seconds = 1;
		long time = System.currentTimeMillis() + seconds * 1000;
		// while (System.currentTimeMillis() < time)
		// ; // 等待。
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			logger.error("Exception in testSerializable()", e);
		}

		// 从文件中读出对象
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(
				"Logon.out"));
		logger.info("Recovering object at " + new java.util.Date());
		try {
			serial = (SerializableDemo) in.readObject();
		} finally {
			IoUtils.close(in);
		}
		logger.info("logon a = " + serial);
		// 这里会显示password: (n/a) 因为password 是transient的。
		// 将数据成员声明为transient后，序列化过程就无法将其加进对象字节流中，没有从transient数据成员发送的数据。后面数据反序列化时，要重建数据成员（因为它是类定义的一部分），但不包含任何数据，因为这个数据成员不向流中写入任何数据。记住，对象流不序列化static或
		// transient标记的成员。这样我们可以利用这点通过重写writeObject()/readObject()方法来做一些特别的性能优化操作和解决一些特别问题。使用writeObject()与readObject()方法时，还要注意按写入的顺序读取这些数据成员.you
		// can refer to java.util.ArrayList.writeObject(ObjectOutputStream)
	}

	/**
	 * TODO: test static variable, which can not be serializable
	 */
	private int age = 9;

	SerializableDemo(String name, String pwd) {
		username = name;
		password = pwd;
	}

	public String toString() {
		String pwd = (password == null) ? "(transient: n/a)" : password;
		return "logon info: \n " + "username: " + username + "\n date: " + date
				+ "\n password: " + pwd + "\n age: " + age;
	}

	/**
	 * 对象流不序列化static或transient. But we can add two private methods to handle
	 * these fields: writeObject()与readObject(), and we don't need to change
	 * anything in our calling method.
	 * <p>
	 * Notice the order for write and read these fields
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(password);
		out.writeInt(age);
		// out.writeInt(this.testStatic);
		// out.writeInt(this.testTransient);
	}

	/**
	 * refer to writeObject: @see writeObject
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		this.password = (String) in.readObject();
		age = in.readInt();
		// this.testStatic = in.readInt();
		// this.testTransient = in.readInt();
	}
}
