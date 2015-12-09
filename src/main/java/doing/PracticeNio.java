package doing;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Channel的实现
 * 
 * 这些是Java NIO中最重要的通道的实现：
 * 
 * FileChannel DatagramChannel SocketChannel ServerSocketChannel
 * 
 * FileChannel 从文件中读写数据。
 * 
 * DatagramChannel 能通过UDP读写网络中的数据。
 * 
 * SocketChannel 能通过TCP读写网络中的数据。
 * 
 * ServerSocketChannel可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
 * 
 * @author Shang Pu
 * @version Date: Dec 8, 2015 10:54:26 AM
 */
public class PracticeNio extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeNio.class);

	/**
	 * FileChannel读取数据到Buffer中的示例
	 */
	@Test
	public void testChannel() {
		try {
			RandomAccessFile aFile = new RandomAccessFile("/data/tmp/nio-data.txt", "rw");
			FileChannel inChannel = aFile.getChannel();

			//create buffer with capacity of 48 bytes
			ByteBuffer buf = ByteBuffer.allocate(48);
			int bytesRead = inChannel.read(buf);//read into buffer.
			//int bytesWritten = inChannel.write(buf);read from buffer into channel. 
			while (bytesRead != -1) {

				System.out.println("Read " + bytesRead);
				buf.flip();//make buffer ready for read

				while (buf.hasRemaining()) {
					System.out.print((char) buf.get());// read 1 byte at a time
				}

				buf.clear();//make buffer ready for writing
				bytesRead = inChannel.read(buf);
			}
			aFile.close();
		} catch (IOException e) {
			log.error("IOException in PracticeNio.testChannel()", e);
		}
	}
}
