package demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.Date;

import org.junit.Test;

public class StreamsFiles {

	// 1) File file = new File ("hello.txt");
	// FileInputStream in=new FileInputStream(file); // 直接进制制流读取

	// 2) File file = new File ("hello.txt");
	// FileInputStream in=new FileInputStream(file);
	// InputStreamReader inReader=new InputStreamReader(in); //
	// 通过InputStreamReader进行二进制流到字符流的转换
	// BufferedReader bufReader=new BufferedReader(inReader); // 嵌套一个buffer以提高效率

	// 3) File file = new File ("hello.txt");
	// FileReader fileReader=new FileReader(file); // 直接对文本进行操作
	// BufferedReader bufReader=new BufferedReader(fileReader);

	// COPY (每次只读写1个字节，非常慢)
	public void copy() {
		Date beginTime = new Date();
		try {
			File inFile = new File("C:\\Downloads\\jigloo352.zip");
			File outFile = new File("C:\\Downloads\\jigloo352.2.zip");
			FileOutputStream fos = new FileOutputStream(outFile);
			FileInputStream fis = new FileInputStream(inFile);
			int c;
			while ((c = fis.read()) != -1)
				fos.write(c);
			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileStreamsTest: " + e);
		} catch (IOException e) {
			System.err.println("FileStreamsTest: " + e);
		}
		Date endTime = new Date();
		System.out.println(((endTime.getTime() - beginTime.getTime()) / 1000.0) + "秒");
	}

	// bufferCopy
	public void bufferCopy() {
		Date beginTime = new Date();
		try {
			byte[] buf = new byte[1024];
			File inFile = new File("C:\\Downloads\\jigloo352.zip");
			File outFile = new File("C:\\Downloads\\jigloo352.2.zip");
			FileInputStream fis = new FileInputStream(inFile);
			FileOutputStream fos = new FileOutputStream(outFile);
			int c;
			while ((c = fis.read(buf)) > 0) {
				fos.write(buf, 0, c); // 读多少写多少，否则拷贝的文件比原来文件大。
			}
			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileStreamsTest: " + e);
		} catch (IOException e) {
			System.err.println("FileStreamsTest: " + e);
		}
		Date endTime = new Date();
		System.out.println(((endTime.getTime() - beginTime.getTime()) / 1000.0) + "秒");
	}

	// bufferStreamCopy
	public void bufferStreamCopy() {
		Date beginTime = new Date();
		try {
			byte[] buf = new byte[1024];
			FileInputStream fis = new FileInputStream("C:\\Downloads\\jigloo352.zip");
			FileOutputStream fos = new FileOutputStream("C:\\Downloads\\jigloo3522.zip");
			BufferedInputStream bfis = new BufferedInputStream(fis);
			BufferedOutputStream bfos = new BufferedOutputStream(fos);
			int c;
			while ((c = bfis.read(buf)) > 0) {
				bfos.write(buf, 0, c);
			}
			bfos.flush(); // 强制清空buffer, 否则写的文件比原来文件小。
			bfis.close();
			bfos.close();
			fis.close();
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileStreamsTest: " + e);
		} catch (IOException e) {
			System.err.println("FileStreamsTest: " + e);
		}
		Date endTime = new Date();
		System.out.println(((endTime.getTime() - beginTime.getTime()) / 1000.0) + "秒");
	}

	// 中文读写 chinese
	public void chinese() {
		try {
			FileInputStream fis = new FileInputStream("c:\\utf8.txt");
			InputStreamReader dis = new InputStreamReader(fis, "utf8");
			BufferedReader reader = new BufferedReader(dis);
			String s;
			while ((s = reader.readLine()) != null) {
				System.out.println(s);
			}
			dis.close();
			fis.close();

			fis = new FileInputStream("c:\\chinese.txt");
			dis = new InputStreamReader(fis, "gb2312");
			reader = new BufferedReader(dis);
			while ((s = reader.readLine()) != null) {
				System.out.println(s);
			}
			dis.close();
			fis.close();

		} catch (IOException e) {
		}
	}

	// TODO: 编码转换
	public void convertEncode() {
		try {
			FileInputStream fis = new FileInputStream("c:\\utf8.txt");
			FileOutputStream fos = new FileOutputStream("c:\\gb2312.txt");

			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "utf8"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, "gb2312"));
			String s;
			while ((s = reader.readLine()) != null) {
				writer.write(s + "\r\n");
			}
			writer.close();
			fis.close();
			fos.close();
		} catch (IOException e) {
		}
	}

	// TODO: 流重定向 Redirecting
	// @Test
	public void redirecting() throws IOException {
		PrintStream console = System.out;
		BufferedInputStream in = new BufferedInputStream(new FileInputStream("c:\\chinese.txt"));
		PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream("c:\\cn.txt")));
		System.setIn(in);
		System.setOut(out);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s;
		while ((s = br.readLine()) != null)
			System.out.println(s);
		out.close();
		System.setOut(console);
	}

	// 非文本数据读写
	// @Test
	public void primitiveStream() throws IOException {
		FileOutputStream fos = new FileOutputStream("c:\\a.txt");
		DataOutputStream dos = new DataOutputStream(fos);
		try {
			dos.writeBoolean(true);
			dos.writeByte((byte) 123);
			dos.writeChar('J');
			dos.writeDouble(3.141592654);
			dos.writeFloat(2.7182f);
			dos.writeInt(1234567890);
			dos.writeLong(998877665544332211L);
			dos.writeShort((short) 11223);
		} finally {
			dos.close();
		}
		DataInputStream dis = new DataInputStream(new FileInputStream("c:\\a.txt"));
		try {
			System.out.println("\t " + dis.readBoolean());
			System.out.println("\t " + dis.readByte());
			System.out.println("\t " + dis.readChar());
			System.out.println("\t " + dis.readDouble());
			System.out.println("\t " + dis.readFloat());
			System.out.println("\t " + dis.readInt());
			System.out.println("\t " + dis.readLong());
			System.out.println("\t " + dis.readShort());
		} finally {
			dis.close();
		}
	}

	// 随机文件 randfile
	@Test
	public void randfile() {
		int dataArray[] = { 12, 31, 56, 23, 27, 1, 43, 65, 4, 99 };
		try {
			RandomAccessFile randf = new RandomAccessFile("c:\\temp.dat", "rw");

			// randf.seek(randf.length());
			// randf.writeBytes("test");
			for (int i = 0; i < dataArray.length; i++) {
				randf.writeInt(dataArray[i]);
			}
			for (int i = dataArray.length - 1; i >= 0; i--) {
				randf.seek(i * 4);
				System.out.println(randf.readInt());
			}
			randf.close();
		} catch (IOException e) {
			System.out.println("File access error: " + e);
		}
	}

}
