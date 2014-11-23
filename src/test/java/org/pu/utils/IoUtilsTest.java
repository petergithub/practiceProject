package org.pu.utils;

import static junit.framework.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Aug 28, 2012 6:01:31 PM
 */
public class IoUtilsTest {
	private final Logger log = LoggerFactory.getLogger(IoUtilsTest.class);
	String path = "C:\\cache\\";

	@Test
	public void testPath() {
		String filePathWithoutPackageFolder = "/C:/sp/Dropbox/base/testProject/target/test-classes/";
		String filePathWithPackageFolder = filePathWithoutPackageFolder + "org/pu/utils/";
		String filePath = filePathWithPackageFolder + "IoUtilsTest.class";
		
		assertEquals(filePathWithoutPackageFolder, getClass().getResource("/").getPath());
		assertEquals(filePathWithoutPackageFolder, ClassLoader.getSystemResource("").getPath());
		assertEquals(filePathWithPackageFolder, getClass().getResource("").getPath());
		assertEquals(filePath, ClassLoader.getSystemResource("org/pu/utils/IoUtilsTest.class").getPath());
		Assert.assertNull(ClassLoader.getSystemResource("/"));
		Assert.assertNull(ClassLoader.getSystemResource("IoUtilsTest.class"));
	}

	public void copyLines() throws IOException {
		BufferedReader inputStream = null;
		PrintWriter outputStream = null;
		try {
			inputStream = new BufferedReader(new FileReader("xanadu.txt"));
			outputStream = new PrintWriter(new FileWriter("characteroutput.txt"));

			String l;
			while ((l = inputStream.readLine()) != null) {
				outputStream.println(l);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public void copyCharacters() throws IOException {
		FileReader inputStream = null;
		FileWriter outputStream = null;
		try {
			inputStream = new FileReader("xanadu.txt");
			outputStream = new FileWriter("characteroutput.txt");

			int c;
			while ((c = inputStream.read()) != -1) {
				outputStream.write(c);
			}
		} finally {
			IoUtils.close(inputStream);
			IoUtils.close(outputStream);
		}
	}

	public void copyBytes() throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;

		try {
			in = new FileInputStream("xanadu.txt");
			out = new FileOutputStream("outagain.txt");
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
		} finally {
			IoUtils.close(in);
			IoUtils.close(out);
		}
	}

	public void testReadFile() {
		long overlayTime = 0;
		try {
			String time = IoUtils.getContent("c:/cache/time.txt");
			log.info("before add overlayTime " + overlayTime / 1000.0 + "s");
			overlayTime = Long.valueOf(time.trim());
			IoUtils.write("c:/cache/time.txt", String.valueOf(overlayTime));
		} catch (IOException e) {
			log.error("write " + overlayTime);
		}
	}

	/**
	 * FileWritter, a character stream to write characters to file. By default, it will replace all
	 * the existing content with new content, however, when you specified a true (boolean) value as
	 * the second argument in FileWritter constructor, it will keep the existing content and append
	 * the new content in the end of the file.
	 */
	public void AppendToFileFileWritterExample() {
		try {
			String data = " This content will append to the end of the file";
			File file = new File("javaio-appendfile.txt");
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(data);
			bufferWritter.close();

			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * BufferedWriter is a character streams class to handle the character data. Unlike bytes stream
	 * (convert data into bytes), you can just write the strings, arrays or characters data directly
	 * to file.
	 */
	public void WriteToFileBufferedWriterExample() {
		try {
			String content = "This is the content to write into file";
			File file = new File("/users/mkyong/filename.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * FileOutputStream is a bytes stream class that’s used to handle raw binary data. To write the
	 * data to file, you have to convert the data into bytes and save it to file. See below full
	 * example.
	 */
	public void WriteFileFileOutputStreamExample() {
		FileOutputStream fop = null;
		File file;
		String content = "This is the text content";
		try {
			file = new File("c:/newfile.txt");
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void BufferedReaderExampleJdk6() {
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader("C:\\testing.txt"));
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * updated example in JDK 7, which use try-with-resources new feature to close file automatically.
	 */
	public static void BufferedReaderExampleJdk7() {
		/*
		 * try (BufferedReader br = new BufferedReader(new FileReader( "C:\\testing.txt"))) { String
		 * sCurrentLine; while ((sCurrentLine = br.readLine()) != null) {
		 * System.out.println(sCurrentLine); } } catch (IOException e) { e.printStackTrace(); }
		 */
	}

	@SuppressWarnings("deprecation")
	public void BufferedInputStreamExample() {
		File file = new File("C:\\testing.txt");
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			while (dis.available() != 0) {
				System.out.println(dis.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				bis.close();
				dis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void StringToInputStreamExample() throws IOException {
		String str = "This is a String ~ GoGoGo";

		// convert String into InputStream
		InputStream is = new ByteArrayInputStream(str.getBytes());

		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
	}

	public void getStringFromInputStream() throws IOException {
		// intilize an InputStream
		InputStream is = new ByteArrayInputStream("file content..blah blah".getBytes());

		String result = IoUtils.getString(is);

		System.out.println(result);
		System.out.println("Done");
	}

	public void randWrite() {
		log.debug("Enter randWrite()");
		try {
			RandomAccessFile raf = new RandomAccessFile("c:/cache/test1.txt", "rw");
			raf.writeUTF("中国你好");
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.debug("Exit randWrite()");
	}

	public void randRead() throws Exception {
		RandomAccessFile raf = new RandomAccessFile("c:/cache/test1.txt", "r");
		String str = raf.readUTF();
		raf.close();
		log.info("str = {}", str);
	}

	public void naiveWrite() throws IOException {
		FileWriter fw = new FileWriter("c:/cache/test.txt");
		fw.write("中文你好");
		fw.close();
	}

	public void naiveRead() throws IOException {
		FileReader fr = new FileReader("c:/cache/test.txt");
		BufferedReader br = new BufferedReader(fr);
		String str = br.readLine();
		br.close();
		fr.close();
		log.info("str = {}", str);
	}

	public void write() throws IOException {
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
				"c:/cache/testOutputStreamWriter.txt"), "utf-8");
		osw.write("中国万岁");
		osw.close();
	}

	public void read() throws IOException {
		InputStreamReader isr = new InputStreamReader(new FileInputStream(
				"c:/cache/testOutputStreamWriter.txt"), "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String str = br.readLine();
		br.close();
		isr.close();
		log.info("str = {}", str);
	}

	public void testGetContent() throws IOException {
		String content = IoUtils.getContent("C:\\sp\\doing\\out.txt");
		log.info("content = {}", content);
	}

	public void testCreateZip() {
		File targetFile = new File(path + "newZip.zip");
		String[] arr = { path + "a" };
		try {
			IoUtils.createZip(arr, targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testExtractZip() {
		log.debug("Enter testExtractZip()");
		String dirToUnzip = path + "tttt";
		try {
			IoUtils.extractZip(path + "test.zip", dirToUnzip);
		} catch (Exception e) {
			log.error("Exception in testUnzip()", e);
		}
		log.debug("Exit testExtractZip()");
	}

	public void testUnzipSpecifyFile() {
		log.debug("Enter testUnzipSpecifyFile()");
		File zippedFile = new File(path + "testa.zip");
		String file = "a.txt";
		try {
			String dirToUnzip = path + "ab";
			IoUtils.extractZip(zippedFile, dirToUnzip, file);
		} catch (Exception e) {
			log.error("Exception in testUnzip()", e);
		}
		log.debug("Exit testUnzipSpecifyFile()");
	}

	public void testGetFreeSpaceInMegaBytes() throws IOException {
		long usage = IoUtils.getFreeSpaceInMegaBytes("c:/");
		log.info("usage = {}", usage);
	}

	public void testGetSpaceUsage() throws IOException {
		long usage = IoUtils.getSpaceUsage("c:/sp/doing");
		log.info("usage = {}", usage);
	}

	public void testMoveFile() {
		File fa = new File("C:\\temp\\a.log");
		File fb = new File("C:\\temp\\documentum\\b.log");
		IoUtils.moveFile(fa, fb);
	}

	public void testIoOperation() throws IOException {
		FileWriter out = new FileWriter("output.txt");
		out.append("out.append\r\n");
		out.write("out.write\r\n");
		out.close();

		FileReader in = new FileReader("output.txt");
		try {
			log.info(String.valueOf(in.read()));
			log.info(System.getProperty("line.separator"));
		} finally {
			IoUtils.close(in);
		}
	}

	public void testIoOperation2() throws IOException {
		// 将文件长度写入txt
		String length = "";
		PrintWriter writer = new PrintWriter(new FileWriter("D:\\clength.txt"));
		writer.print(length);
		writer.close();

		// 读取文件
		FileReader reader = new FileReader("D:\\clength.txt");
		BufferedReader br = new BufferedReader(reader);
		try {
			br.read();
		} finally {
			IoUtils.close(br);
		}
	}
}
