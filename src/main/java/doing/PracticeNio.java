package doing;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://www.ibm.com/developerworks/cn/education/java/j-nio/
 * 
 * @author Shang Pu
 * @version Date: Dec 8, 2015 10:54:26 AM
 */
public class PracticeNio extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeNio.class);

	public void useFloatBuffer() {
		FloatBuffer buffer = FloatBuffer.allocate(10);

		for (int i = 0; i < buffer.capacity(); ++i) {
			float f = (float) Math.sin((((float) i) / 10) * (2 * Math.PI));
			buffer.put(f);
		}

		buffer.flip();

		while (buffer.hasRemaining()) {
			float f = buffer.get();
			System.out.println(f);
		}
	}

	public void typesInByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(64);

		buffer.putInt(30);
		buffer.putLong(7000000000000L);
		buffer.putDouble(Math.PI);

		buffer.flip();

		System.out.println(buffer.getInt());
		System.out.println(buffer.getLong());
		System.out.println(buffer.getDouble());
	}

	@Test
	public void useScatterGather() throws Exception {
		int port = 10000;

		final int firstHeaderLength = 2;
		final int secondHeaderLength = 4;
		final int bodyLength = 6;
		ServerSocketChannel ssc = ServerSocketChannel.open();
		InetSocketAddress address = new InetSocketAddress(port);
		ssc.socket().bind(address);

		int messageLength = firstHeaderLength + secondHeaderLength + bodyLength;

		ByteBuffer buffers[] = new ByteBuffer[3];
		buffers[0] = ByteBuffer.allocate(firstHeaderLength);
		buffers[1] = ByteBuffer.allocate(secondHeaderLength);
		buffers[2] = ByteBuffer.allocate(bodyLength);

		SocketChannel sc = ssc.accept();

		while (true) {

			// Scatter-read into buffers
			int bytesRead = 0;
			while (bytesRead < messageLength) {
				long r = sc.read(buffers);
				bytesRead += r;

				System.out.println("r " + r);
				for (int i = 0; i < buffers.length; ++i) {
					ByteBuffer bb = buffers[i];
					System.out.println("b " + i + " " + bb.position() + " " + bb.limit());
				}
			}

			// Process message here

			// Flip buffers
			for (int i = 0; i < buffers.length; ++i) {
				ByteBuffer bb = buffers[i];
				bb.flip();
			}

			// Scatter-write back out
			long bytesWritten = 0;
			while (bytesWritten < messageLength) {
				long r = sc.write(buffers);
				bytesWritten += r;
			}

			// Clear buffers
			for (int i = 0; i < buffers.length; ++i) {
				ByteBuffer bb = buffers[i];
				bb.clear();
			}

			System.out.println(bytesRead + " " + bytesWritten + " " + messageLength);
		}
	}

	public void useFileLocks() throws Exception {
		final int start = 10;
		final int end = 20;

		// Get file channel
		RandomAccessFile raf = new RandomAccessFile("usefilelocks.txt", "rw");
		FileChannel fc = raf.getChannel();

		// Get lock
		System.out.println("trying to get lock");
		FileLock lock = fc.lock(start, end, false);
		System.out.println("got lock!");

		// Pause
		System.out.println("pausing");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ie) {
		}

		// Release lock
		System.out.println("going to release lock");
		lock.release();
		System.out.println("released lock");

		raf.close();
	}

	public void multiPortEcho() throws IOException {
		int ports[] = { 100, 1000 };
		ByteBuffer echoBuffer = ByteBuffer.allocate(1024);

		// Create a new selector
		Selector selector = Selector.open();

		// Open a listener on each port, and register each one
		// with the selector
		for (int i = 0; i < ports.length; ++i) {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			// 将 ServerSocketChannel 设置为 非阻塞的 。我们必须对每一个要使用的套接字通道调用这个方法，否则异步 I/O
			// 就不能工作。
			ssc.configureBlocking(false);
			ServerSocket ss = ssc.socket();
			InetSocketAddress address = new InetSocketAddress(ports[i]);
			ss.bind(address);

			// SelectionKey 代表这个通道在此 Selector 上的这个注册。当某个 Selector
			// 通知您某个传入事件时，它是通过提供对应于该事件的 SelectionKey 来进行的。SelectionKey
			// 还可以用于取消通道的注册
			SelectionKey key = ssc.register(selector, SelectionKey.OP_ACCEPT);

			System.out.println("Going to listen on " + ports[i] + " with key " + key);
		}

		while (true) {
			// 返回所发生的事件的数量
			int num = selector.select();
			System.out.println("num " + num);

			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectedKeys.iterator();

			while (it.hasNext()) {
				SelectionKey key = it.next();

				if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
					// Accept the new connection
					ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
					SocketChannel sc = ssc.accept();
					sc.configureBlocking(false);

					// Add the new connection to the selector
					SelectionKey newKey = sc.register(selector, SelectionKey.OP_READ);
					it.remove();

					System.out.println("Got connection from " + sc + " with newKey " + newKey);
				} else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
					// Read the data
					SocketChannel sc = (SocketChannel) key.channel();

					// Echo data
					int bytesEchoed = 0;
					while (true) {
						echoBuffer.clear();

						int r = sc.read(echoBuffer);

						if (r <= 0) {
							break;
						}

						echoBuffer.flip();

						sc.write(echoBuffer);
						bytesEchoed += r;
					}

					System.out.println("Echoed " + bytesEchoed + " from " + sc);

					it.remove();
				}

			}

			// System.out.println( "going to clear" );
			// selectedKeys.clear();
			// System.out.println( "cleared" );
		}
	}

	/**
	 * <pre>
	 * FileChannel读取数据到Buffer中的示例
	 * 这些是Java NIO中最重要的通道的实现：
	 * FileChannel DatagramChannel SocketChannel ServerSocketChannel
	 * FileChannel 从文件中读写数据。
	 * DatagramChannel 能通过UDP读写网络中的数据。
	 * SocketChannel 能通过TCP读写网络中的数据。
	 * ServerSocketChannel可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
	 * </pre>
	 */
	@Test
	public void testChannel() {
		try {
			RandomAccessFile aFile = new RandomAccessFile("/data/tmp/nio-data.txt", "rw");
			FileChannel inChannel = aFile.getChannel();

			// create buffer with capacity of 48 bytes
			ByteBuffer buf = ByteBuffer.allocate(48);
			int bytesRead = inChannel.read(buf);// read into buffer.
			// int bytesWritten = inChannel.write(buf);read from buffer into
			// channel.
			while (bytesRead != -1) {

				System.out.println("Read " + bytesRead);
				buf.flip();// make buffer ready for read

				while (buf.hasRemaining()) {
					System.out.print((char) buf.get());// read 1 byte at a time
				}

				buf.clear();// make buffer ready for writing
				bytesRead = inChannel.read(buf);
			}
			aFile.close();
		} catch (IOException e) {
			log.error("IOException in PracticeNio.testChannel()", e);
		}
	}
}
