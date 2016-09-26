package org.pu.utils;

import static org.pu.utils.Constants.EOF;
import static org.pu.utils.Constants.EOL;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.pu.bean.FolderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Aug 23, 2013 11:10:37 AM
 */
public class IoUtils {

	private final static Logger log = LoggerFactory.getLogger(IoUtils.class);

	/**
	 * The default buffer size to use.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	/**
	 * get a list from file. the data in line will be return as a string
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public static List<String> getLineList(String filePath) throws IOException {
		List<String> list = new ArrayList<String>();
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException(filePath);
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String str = null;
			while ((str = br.readLine()) != null) {
				list.add(str);
			}
		} finally {
			close(br);
		}
		return list;
	}

	/**
	 * 从网络URL下载文件
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void downLoadFromUrl(String urlStr, String fileName, String savePath)
			throws IOException {
		log.debug("urlStr: {} fileName: {} savePath: {}", urlStr, fileName, savePath);
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);

		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}

		log.debug("info: {} download save as {}/{}", url, savePath, fileName);
	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	/**
	 * get a list from csv or txt file. the data in line will be split with "\t" and return as a
	 * string array
	 * 
	 * @param filePath
	 * @return List<String[]>
	 * @throws IOException
	 */
	public static List<String[]> getListFromFile(String filePath) throws IOException {
		List<String[]> list = new ArrayList<String[]>();
		BufferedReader in = null;
		try {
			String line;
			in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			while ((line = in.readLine()) != null) {
				list.add(line.substring(0, line.length()).split("\t"));
			}
		} finally {
			close(in);
		}
		return list;
	}

	public static String getContent(String filePath) throws IOException {
		log.debug("getContent({})", filePath);
		InputStream is = new FileInputStream(filePath);
		String content = "";
		try {
			content = getString(is);
		} finally {
			close(is);
		}
		return content;
	}

	public static String getContentInJar(String filePath) throws IOException {
		log.debug("loadContentInJar({})", filePath);
		InputStream is = IoUtils.class.getResourceAsStream("/" + filePath);
		String content = "";
		try {
			content = getString(is);
		} finally {
			close(is);
		}
		return content;
	}

	/**
	 * get String from InputStream
	 */
	public static String getString(InputStream is) throws IOException {
		StringBuilder content = new StringBuilder("");
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line;
		try {
			while ((line = in.readLine()) != null) {
				content.append(line).append(EOL);
			}
		} finally {
			close(in);
		}
		return content.toString();
	}

	/**
	 * load the property file in class folder
	 * 
	 * @param propertyFile
	 * @return Properties
	 */
	public static Properties getProperties(String propertyFile) {
		// InputStream in =
		// Utils.class.getClassLoader().getResourceAsStream(propertyFile);
		InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(propertyFile);

		if (in == null) {
			throw new RuntimeException("Unable to load resource: " + propertyFile);
		}

		Properties props = new Properties();
		try {
			props.load(in);
			in.close();
		} catch (IOException e) {
			log.error("Exception in IoUtils.getProperties()", e);
		}
		return props;
	}

	/**
	 * return value in properties file fileName.properties
	 * <p>
	 * return empty string if key is null or empty, or do not find property file,
	 */
	public static String getResourceValue(String key, String fileName) {
		log.debug("Enter getResourceString({}, {})", key, fileName);
		if (key == null || key.length() == 0) {
			log.error("key={}, Null or empty String id specified!", key);
			return "";
		}

		// get fileName.properties
		ResourceBundle resBundle = ResourceBundle.getBundle(fileName);
		if (resBundle == null) {
			log.error("Null Resource class!");
			return "";
		}
		String value = resBundle.getString(key);
		log.debug("Exit getResourceString({}={})", key, value);
		return value;
	}

	/**
	 * @param path: file name with package path e.g. com/name
	 * @return
	 */
	public static URL getResource(String path) {
		return ClassLoader.getSystemClassLoader().getResource(path);
	}

	/**
	 * @throws IllegalArgumentException if filename is inside the jar
	 */
	public static File getFile(String fileName) throws URISyntaxException {
		log.debug("Enter loadFile({})", fileName);
		Properties propss = System.getProperties();
		String userdir = propss.getProperty("user.dir");
		File file = null;
		log.debug("read file from jar folder level - userdir {}", userdir);
		file = new File(userdir + File.separator + fileName);
		if (!file.exists()) {
			log.debug("file is not exist, read file through the system class loader");
			URI uri = ClassLoader.getSystemResource(fileName).toURI();
			log.debug("uri = {}", uri);
			file = new File(uri);
		}
		log.debug("Exit loadFile()");
		return file;
	}

	public static void appendFile(String filePath, String content) throws IOException {
		RandomAccessFile randomFile = new RandomAccessFile(filePath, "rw");
		long fileLength = randomFile.length();
		randomFile.seek(fileLength);
		randomFile.writeBytes(content);
		randomFile.close();
	}

	/**
	 * write content to specify filePath, the existed file will be overwrite. Creates the directory
	 * including any necessary but nonexistent parent directories.
	 * 
	 * @param filePath e.g. c:/filename.txt
	 * @param content
	 * @throws IOException
	 */
	public static void write(String filePath, String content) throws IOException {
		File file = new File(filePath);
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file));
		try {
			File folder = file.getParentFile();
			if (folder != null) {
				folder.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			bWriter.write(content);
			bWriter.flush();
		} finally {
			close(bWriter);
		}
	}

	/**
	 * create a file, the existed file will be overwrite.
	 * 
	 * @param filePath String e.g. c:/filename.txt
	 * @param content String contents
	 * @throws IOException
	 */
	public static void writeFile(String filePath, String content) throws IOException {
		File file = new File(filePath);
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			printWriter = new PrintWriter(new FileWriter(file));
			printWriter.println(content);
		} finally {
			close(fileWriter);
			close(printWriter);
		}
	}

	public static void writeByteToFile(String fileName, byte[] byteArray) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(fileName));
			out.write(byteArray);
			out.flush();
		} catch (IOException e) {
			log.error("IOException in HaiJiaDoing.writeByteToFile()", e);
		} finally {
			IoUtils.close(out);
		}
	}

	public static String getRootPath() {
		String path = "";
		try {
			path = IoUtils.class.getResource("/").getPath();
			log.debug("path is: {}", path);
			if (path == null) {
				path = IoUtils.class.getClassLoader().getResource("./properties").getPath();
				log.debug("if path is null then, set it as: {}", path);
			}
		} catch (NullPointerException e) {
			path = System.getProperty("usr.dir");
			log.error("if exception set path as:  {}", path, e);
		}
		return path;
	}

	/**
	 * Creates the directory named by this abstract pathname, including any necessary but nonexistent
	 * parent directories. Note that if this operation fails it may have succeeded in creating some of
	 * the necessary parent directories.
	 * 
	 * @param path String e.g. c:/folderpath or c:/folderpath/filename
	 * @return <code>true</code> if and only if the directory was created, along with all necessary
	 *         parent directories; <code>false</code> otherwise
	 */
	public static boolean mkdirs(String path) {
		File folder = new File(path);
		return folder.mkdirs();
	}

	/**
	 * Remove file or folder, a folder with content won't be removed unless force flag is set to true
	 * 
	 * @param filePathName
	 * @param bForceFolderRemoval
	 * @throws FileNotFoundException
	 */
	public static void deleteFile(String filePathName, boolean bForceFolderRemoval)
			throws FileNotFoundException {
		File file = new File(filePathName);
		if (!file.exists()) {
			throw new FileNotFoundException(filePathName + " doesn't exists or not accessiable!");
		}

		if (file.isDirectory() && bForceFolderRemoval) {
			deleteFolder(file);
		} else {
			// For directory with sub files, the delete action will return
			// false, no action taken
			file.delete();
		}
	}

	private static void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File subFile = files[i];
				if (subFile.isFile()) {
					subFile.delete();
				} else if (subFile.isDirectory()) {
					deleteFolder(subFile);
				}
				subFile = null;
			}
			folder.delete();
		}
	}

	/**
	 * Rename a file or directory's name
	 * 
	 * @param srcFileName
	 * @param destFileName
	 * @throws FileNotFoundException
	 * @throws RenameFileFailureException
	 */
	public static void renameFile(String srcFileName, String destFileName) throws Exception {

		File srcFile = new File(srcFileName);

		if (!srcFile.exists()) {
			throw new FileNotFoundException(srcFileName + " doesn't exists or not accessiable!");
		}

		File targetFile = new File(destFileName);

		if (!srcFile.renameTo(targetFile)) {
			throw new IOException("Failed to rename file from " + srcFileName + " to " + destFileName);
		}
	}

	/**
	 * Check if a given path is directory or not.
	 * 
	 * @param filePathName
	 * @return true if it's directory
	 * @throws FileNotFoundException if the filePathName doesn't exists or not accessible.
	 */
	public static boolean isDirectory(String filePathName) throws FileNotFoundException {
		File file = new File(filePathName);
		if (!file.exists()) {
			throw new FileNotFoundException(filePathName + " doesn't exists or not accessiable!");
		}

		return file.isDirectory();
	}

	public static boolean copyFolderContent(String sourcePath, String destPath) throws IOException {
		File sourceDir = new File(sourcePath);
		if (!sourceDir.exists() || !sourceDir.canRead() || !sourceDir.isDirectory()) {
			return false;
		}

		File destDir = new File(destPath);
		if (destDir.exists()) {
			if (!destDir.isDirectory()) {
				return false;
			}
		} else {
			if (!destDir.mkdirs()) {
				return false;
			}
		}
		if (!destDir.canWrite()) {
			return false;
		}

		File[] files = sourceDir.listFiles();
		for (int iLoop = 0; iLoop < files.length; iLoop++) {
			File sourceFile = files[iLoop];
			File destFile = new File(destDir, sourceFile.getName());
			destFile.createNewFile();
			copyFile(sourceFile, destFile);
		}
		return true;
	}

	public static String createTempDir(String prefix) {
		String path = System.getProperty("java.io.tmpdir");
		File temp = new File(path);
		File newDir = null;
		newDir = new File(temp, prefix + System.currentTimeMillis());
		newDir.mkdir();

		path = newDir.getAbsolutePath();
		return path;
	}

	public static boolean moveDir(File sourceDir, File destDir) {
		boolean flag = true;
		if (!destDir.exists()) {
			destDir.mkdir();
		}

		File[] children = sourceDir.listFiles();
		for (int i = 0; children != null && i < children.length; i++) {
			File sourceChild = children[i];
			String name = sourceChild.getName();
			File destChild = new File(destDir, name);
			if (sourceChild.isDirectory()) {
				flag = flag && moveDir(sourceChild, destChild);
			} else {
				flag = flag && moveFile(sourceChild, destChild);
			}
		}
		return flag;
	}

	public static boolean moveFile(File source, File dest) {
		boolean flag;
		if (dest.exists()) {
			log.debug("Destination file exists : {}", dest.getAbsolutePath());
			flag = false;
		} else {
			flag = source.renameTo(dest);
			if (!flag) {
				log.debug("Cannot move file : {}", source.getAbsolutePath());
			}
		}
		return flag;
	}

	/**
	 * write object to file
	 * 
	 * @param obj
	 * @param fileName
	 * @throws IOException
	 */
	public static void writeObject(Object obj, String fileName) throws IOException {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
			out.writeObject(obj);
		} finally {
			close(out);
		}
	}

	/**
	 * read object from file
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object readObject(String fileName) throws FileNotFoundException, IOException,
			ClassNotFoundException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
			return in.readObject();
		} finally {
			close(in);
		}
	}

	/**
	 * return basePath + File.separator + appendPath
	 */
	public static String makePath(String basePath, String appendPath) {
		if (basePath == null || basePath.length() == 0) return appendPath;
		if (appendPath == null || appendPath.length() == 0) return basePath;
		String completePath;
		if (basePath.endsWith(File.separator) || appendPath.startsWith(File.separator))
			completePath = basePath + appendPath;
		else
			completePath = basePath + File.separator + appendPath;
		return completePath;
	}

	/**
	 * Creates a zip of a directory or file. If a directory, then the last directory in the path is
	 * assumed to be the directory to be zipped and is used as a virtual root. All internal files and
	 * directories are mapped relative to this virtual root.
	 * 
	 * @param objectToZip Complete path of a file or directory to zip.
	 * @param targetFilename The output zip file that is to be created. The complete path must be
	 *          mentioned alongwith any necessary extensions such as .zip
	 */
	public static void createZip(String objectsToZip[], File targetFile) throws IOException {
		File flSource = new File(objectsToZip[0]);

		FileOutputStream fout = new FileOutputStream(targetFile);
		ZipOutputStream zout = new ZipOutputStream(fout);

		try {
			if (flSource.isDirectory()) {
				addDirectoryRecursively(flSource, zout, "");
			} else {
				addFile(flSource, zout, "");
			}
		} finally {
			zout.finish();
			zout.close();
			fout.close();
		}
	}

	/**
	 * Adds a directory and its contents (i.e. other directories and files) to the zip stream
	 * 
	 * @param directory The directory to be added.
	 * @param zipStream The zip stream to which the contents are added.
	 * @param path Path of the folder that contains the new one being created.
	 * @throws IOException
	 */
	private static void addDirectoryRecursively(File directory, ZipOutputStream zipStream, String path)
			throws IOException {
		/*
		 * String dirPath = directory.getPath(); ZipEntry dirEntry = new ZipEntry(dirPath);
		 * zipStream.putNextEntry(dirEntry); zipStream.closeEntry();
		 */

		StringBuffer bufPath = new StringBuffer(32);
		bufPath.append(path).append(directory.getName()).append('/');
		String curRelativePath = bufPath.toString();
		zipStream.putNextEntry(new ZipEntry(curRelativePath));

		File allFiles[] = directory.listFiles();
		for (int ctrFiles = 0; ctrFiles < allFiles.length; ctrFiles++) {
			File curFile = allFiles[ctrFiles];
			if (curFile.isDirectory()) {
				addDirectoryRecursively(curFile, zipStream, curRelativePath);
			} else {
				addFile(curFile, zipStream, curRelativePath);
			}
		}
	}

	/**
	 * Adds the contents of a file to the zip stream.
	 * 
	 * @param file The file whose contents are to be added.
	 * @param zipStream The zip stream to which the contents are added.
	 * @param path The path of the folder that contains this file.
	 * @throws IOException
	 */
	private static void addFile(File file, ZipOutputStream zipStream, String path) throws IOException {
		FileInputStream fin = new FileInputStream(file);

		int bufSize = 1024;
		byte ipBuf[] = new byte[bufSize];
		int lenRead = 0;

		String filename = path + file.getName();
		zipStream.putNextEntry(new ZipEntry(filename));

		while ((lenRead = fin.read(ipBuf)) > 0) {
			zipStream.write(ipBuf, 0, lenRead);
		}

		zipStream.closeEntry();
		fin.close();
	}

	/**
	 * @param zipFile complete path to the zip file
	 * @param targetDirectory Complete path of the target directory into which the zip file is
	 *          extracted.
	 */
	public static void extractZip(String zipFile, String targetDirectory) throws IOException {
		ZipFile zipFl = new ZipFile(zipFile);
		Enumeration<? extends ZipEntry> enumZipEntries = zipFl.entries();
		while (enumZipEntries.hasMoreElements()) {
			ZipEntry zipEntry = enumZipEntries.nextElement();
			String entryName = zipEntry.getName();
			System.out.println(entryName);

			StringBuffer bufPath = new StringBuffer(32);
			if (targetDirectory.endsWith(File.separator)) {
				targetDirectory = targetDirectory.substring(0, targetDirectory.length() - 1);
			}
			if (entryName.startsWith(File.separator)) {
				entryName = entryName.substring(1);
			}
			bufPath.append(targetDirectory);
			bufPath.append(File.separator);
			bufPath.append(entryName);
			String entryFullPath = bufPath.toString();
			File entryFile = new File(entryFullPath);
			// System.out.println("Full Entry Path: " +
			// entryFile.getAbsolutePath());
			if (zipEntry.isDirectory()) {
				entryFile.mkdirs();
			} else {
				entryFile.getParentFile().mkdirs();
				BufferedOutputStream bufOut = new BufferedOutputStream(new FileOutputStream(entryFile));
				InputStream is = zipFl.getInputStream(zipEntry);
				byte[] buf = new byte[1024];
				int numRead = 0;
				while ((numRead = is.read(buf)) != -1) {
					bufOut.write(buf, 0, numRead);
				}
				bufOut.close();
			}
		}
		zipFl.close();
	}

	/**
	 * Extract specify file with file name "fileName" to target directory dirToUnzip if the file under
	 * a folder in the zip, use such format
	 * 
	 * <pre>
	 * extractZip(zippedFile, dirToUnzip, &quot;folderpath/filename&quot;)
	 * </pre>
	 * 
	 * @param zippedFile
	 * @param dirToUnzip
	 * @param fileName
	 */
	public static void extractZip(File zippedFile, String dirToUnzip, String fileName)
			throws IOException {
		ZipFile zipfile = new ZipFile(zippedFile);
		ZipEntry entry = zipfile.getEntry(fileName);
		if (entry != null) {
			File file = new File(makePath(dirToUnzip, entry.getName()));
			if (!entry.isDirectory()) {
				new File(dirToUnzip).mkdirs();
				FileOutputStream fos = new FileOutputStream(file);
				copy(zipfile.getInputStream(entry), fos);
			}
		} else {
			log.error("In extractZip(): No such specify file in zip file");
		}
		zipfile.close();
	}

	public static String readFileToString(File file, Charset encoding) throws IOException {
		InputStream in = null;
		try {
			in = openInputStream(file);
			return toString(in, toCharset(encoding));
		} finally {
			close(in);
		}
	}

	public static String toString(InputStream input, Charset encoding) throws IOException {
		Writer sw = new Writer() {
			private final StringBuilder builder = new StringBuilder();

			@Override
			public void write(char[] cbuf, int off, int len) throws IOException {
				if (cbuf != null) {
					builder.append(cbuf, off, len);
				}
			}

			@Override
			public void flush() throws IOException {
			}

			@Override
			public void close() throws IOException {
			}
		};
		copy(input, sw, encoding);
		return sw.toString();
	}

	public static Charset toCharset(Charset charset) {
		return charset == null ? Charset.defaultCharset() : charset;
	}

	public static void copy(InputStream input, Writer output, Charset encoding) throws IOException {
		InputStreamReader in = new InputStreamReader(input, toCharset(encoding));
		copy(in, output);
	}

	public static int copy(Reader input, Writer output) throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	public static long copyLarge(Reader input, Writer output) throws IOException {
		return copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
	}

	public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
		long count = 0;
		int n = 0;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static FileInputStream openInputStream(File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (file.canRead() == false) {
				throw new IOException("File '" + file + "' cannot be read");
			}
		} else {
			throw new FileNotFoundException("File '" + file + "' does not exist");
		}
		return new FileInputStream(file);
	}

	public static void copyFile(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);
		// Transfer bytes from in to out
		copy(in, out);
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * Large streams (over 2GB) will return a bytes copied value of <code>-1</code> after the copy has
	 * completed since the correct number of bytes cannot be returned as an int. For large streams use
	 * the <code>copyLarge(InputStream, OutputStream)</code> method.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 * @throws ArithmeticException if the byte count is too large
	 * @since Commons IO 1.1
	 */
	public static int copy(InputStream input, OutputStream output) throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	/**
	 * Copy bytes from a large (over 2GB) <code>InputStream</code> to an <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 * @since Commons IO 1.3
	 */
	public static long copyLarge(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * return free space of a directory(Drive), if the path specified not exists, -1 will be return
	 */
	public static int getFreeSpaceInMegaBytes(String path) throws IOException {
		long nSpaceInByte = getFreeSpaceInBytes(path);
		log.debug(String.valueOf(nSpaceInByte));
		if (nSpaceInByte >= 0) {
			int nSpaceInMegaByte = (int) (nSpaceInByte / (1024 * 1024));
			return nSpaceInMegaByte;
		} else {
			return (int) nSpaceInByte;
		}
	}

	public static long getFreeSpaceInBytes(String path) throws IOException {
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows")) {
			return getFreeSpaceOnWindows(path);
		}

		if (osName.startsWith("Linux")) {
			return getFreeSpaceOnLinux(path);
		}

		if (osName.startsWith("SunOS")) {
			return getFreeSpaceOnSolaris(path);
		}

		throw new UnsupportedOperationException(
				"The method getFreeSpace(String path) has not been implemented for this operating system : "
						+ osName);
	}

	private static long getFreeSpaceOnWindows(String path) throws IOException {
		long bytesFree = -1;

		File script = new File(System.getProperty("java.io.tmpdir"), "script.bat");
		PrintWriter writer = new PrintWriter(new FileWriter(script, false));
		writer.println("dir \"" + path + "\"");
		writer.close();

		// get the output from running the .bat file
		Process p = Runtime.getRuntime().exec(script.getAbsolutePath());
		InputStream reader = new BufferedInputStream(p.getInputStream());
		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = reader.read();
			if (c == -1) break;
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		reader.close();

		// parse the output text for the bytes free info
		StringTokenizer tokenizer = new StringTokenizer(outputText, "\n");
		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken().trim();
			// see if line contains the bytes free information
			if (line.endsWith("bytes free")) {
				tokenizer = new StringTokenizer(line, " ");
				tokenizer.nextToken();
				tokenizer.nextToken();
				bytesFree = Long.parseLong(tokenizer.nextToken().replaceAll(",", ""));
			}
		}
		return bytesFree;
	}

	private static long getFreeSpaceOnSolaris(String path) throws IOException {
		long bytesFree = -1;

		Process p = Runtime.getRuntime().exec("df -k " + "/" + path);
		InputStream reader = new BufferedInputStream(p.getInputStream());
		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = reader.read();
			if (c == -1) break;
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		reader.close();

		// parse the output text for the bytes free info
		StringTokenizer tokenizer = new StringTokenizer(outputText, "\n");
		@SuppressWarnings("unused")
		String firstLine = tokenizer.nextToken();

		if (tokenizer.hasMoreTokens()) {
			String line2 = tokenizer.nextToken();
			StringTokenizer tokenizer2 = new StringTokenizer(line2, " ");
			if (tokenizer2.countTokens() >= 4) {
				tokenizer2.nextToken();
				tokenizer2.nextToken();
				tokenizer2.nextToken();
				try {
					bytesFree = Long.parseLong(tokenizer2.nextToken());
					bytesFree = bytesFree * 1024;
				} catch (Exception ex) {
					throw new IOException("Can not read the free space of " + path + ".");
				}
				return bytesFree;
			}

		}
		throw new IOException("Can not read the free space of " + path + ".");
	}

	private static long getFreeSpaceOnLinux(String path) throws IOException {
		long bytesFree = -1;

		Process p = Runtime.getRuntime().exec("df " + "/" + path);
		InputStream reader = new BufferedInputStream(p.getInputStream());
		StringBuffer buffer = new StringBuffer();
		for (;;) {
			int c = reader.read();
			if (c == -1) break;
			buffer.append((char) c);
		}
		String outputText = buffer.toString();
		reader.close();

		// parse the output text for the bytes free info
		StringTokenizer tokenizer = new StringTokenizer(outputText, "\n");
		tokenizer.nextToken();
		if (tokenizer.hasMoreTokens()) {
			String line2 = tokenizer.nextToken();
			StringTokenizer tokenizer2 = new StringTokenizer(line2, " ");
			if (tokenizer2.countTokens() >= 4) {
				tokenizer2.nextToken();
				tokenizer2.nextToken();
				tokenizer2.nextToken();
				bytesFree = Long.parseLong(tokenizer2.nextToken());
				return bytesFree;
			}
		}
		throw new IOException("Can not read the free space of " + path + " path");
	}

	/**
	 * Return the space used by a folder or a file
	 * 
	 * @param filePathName
	 * @return the size of space used by the file or folder in bytes
	 * @throws Exception
	 */
	public static long getSpaceUsage(String filePathName) throws IOException {
		File file = new File(filePathName);
		if (!file.exists()) {
			throw new IOException("File or Folder " + filePathName
					+ " doesn't exists or not accessiable!");
		}

		if (file.isFile()) {
			return file.length();
		}

		FolderBean folderBean = new FolderBean();
		long spaceUsage = getFolderSpaceUsage(folderBean, file, 0, 0);
		folderBean.setSpaceUsage(spaceUsage);

		return spaceUsage;
	}

	private static long getFolderSpaceUsage(FolderBean folderBean, File folder, long startTimeMillis,
			long timeToLiveMillis) throws IOException {
		long folderSpaceUsage = 0;

		// If the operation time exceeds the livetime, abort operation and throw
		// Exception
		if (timeToLiveMillis > 0) {
			long currentTimeMillis = System.currentTimeMillis();
			if (currentTimeMillis - timeToLiveMillis > startTimeMillis) {
				throw new IOException("The operation takes too much time and is aborted!");
			}
		}

		File[] files = folder.listFiles();
		if (files == null) return folderSpaceUsage;
		for (int i = 0; i < files.length; i++) {
			File subFile = files[i];
			if (subFile.isFile()) {
				folderSpaceUsage += subFile.length();
				log.debug("Checking folderSpaceUsage: " + folderSpaceUsage);
				folderBean.setFileNumber(folderBean.getFileNumber() + 1);

				if (timeToLiveMillis > 0) {
					long currentTimeMillis = System.currentTimeMillis();
					log.debug("Computing upload size: " + (currentTimeMillis - startTimeMillis) + "ms spent");
					if (currentTimeMillis - timeToLiveMillis > startTimeMillis) {
						throw new IOException("The operation takes too much time and is aborted!");
					}
				}
			} else if (subFile.isDirectory()) {
				folderSpaceUsage += getFolderSpaceUsage(folderBean, subFile, startTimeMillis,
						timeToLiveMillis);
				log.debug("Checking folderSpaceUsage: " + folderSpaceUsage);
				folderBean.setFolderNumber(folderBean.getFolderNumber() + 1);
			}
			subFile = null;
		}

		log.debug("Checking Total folderSpaceUsage: " + folderSpaceUsage);
		return folderSpaceUsage;
	}

	/**
	 * @param filePathName
	 * @return the folder information including number of file, subfolder and space usage
	 * @throws Exception
	 */
	public static FolderBean getFolderInfo(String filePathName) throws Exception {
		File file = new File(filePathName);
		if (!file.exists() || !file.isDirectory()) {
			throw new Exception("Folder " + filePathName + " doesn't exists or not accessiable!");
		}

		FolderBean folderBean = new FolderBean();
		long spaceUsage = getFolderSpaceUsage(folderBean, file, 0, 0);
		folderBean.setSpaceUsage(spaceUsage);

		return folderBean;
	}

	/**
	 * @param filePathName
	 * @return the folder information including number of file, subfolder and space usage
	 * @throws Exception
	 */
	public static FolderBean getFolderInfo(String filePathName, long _timeToLiveInSeconds)
			throws Exception, Exception {
		File file = new File(filePathName);
		if (!file.exists() || !file.isDirectory()) {
			throw new Exception("Folder " + filePathName + " doesn't exists or not accessiable!");
		}

		long startTimeMillis = System.currentTimeMillis();
		FolderBean folderBean = new FolderBean();
		long spaceUsage = getFolderSpaceUsage(folderBean, file, startTimeMillis,
				_timeToLiveInSeconds * 1000);
		folderBean.setSpaceUsage(spaceUsage);

		return folderBean;
	}

	/**
	 * close it quietly
	 */
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ignore) {
			}
		}
	}
}
