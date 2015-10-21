package demo.javabasics.demo.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.pu.test.base.TestBase;
import org.pu.utils.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import doing.Practice;


/**
 * @author Shang Pu
 * @version Date: Apr 20, 2012 1:36:41 PM
 */
public class TestClassLoader extends TestBase {
	private final Logger logger = LoggerFactory
			.getLogger(TestClassLoader.class);

	public void testClassLoader() throws Exception {
		ClassLoader context = Thread.currentThread().getContextClassLoader();
		logger.info("context = {}", context);
		// Thread.currentThread().setContextClassLoader(null) ;
		ClassLoader current = Practice.class.getClassLoader();
		logger.info("current = {}", current);
	}

	public void testClassIdentity() {
		String classDataRootPath = "C:\\workspace\\Classloader\\classData";
		FileSystemClassLoader loader1 = new FileSystemClassLoader(
				classDataRootPath);
		FileSystemClassLoader loader2 = new FileSystemClassLoader(
				classDataRootPath);
		String className = "com.test.Sample";
		try {
			Class<?> class1 = loader1.loadClass(className);
			Object obj1 = class1.newInstance();
			Class<?> class2 = loader2.loadClass(className);
			Object obj2 = class2.newInstance();
			Method setSampleMethod = class1.getMethod("setSample",
					java.lang.Object.class);
			setSampleMethod.invoke(obj1, obj2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class FileSystemClassLoader extends ClassLoader {

	private String rootDir;

	public FileSystemClassLoader(String rootDir) {
		this.rootDir = rootDir;
	}

	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] classData = getClassData(name);
		if (classData == null) {
			throw new ClassNotFoundException();
		} else {
			return defineClass(name, classData, 0, classData.length);
		}
	}

	private byte[] getClassData(String className) {
		String path = classNameToPath(className);
		InputStream ins = null;
		try {
			ins = new FileInputStream(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int bufferSize = 4096;
			byte[] buffer = new byte[bufferSize];
			int bytesNumRead = 0;
			while ((bytesNumRead = ins.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesNumRead);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IoUtils.close(ins);
		}
		return null;
	}

	private String classNameToPath(String className) {
		return rootDir + File.separatorChar
				+ className.replace('.', File.separatorChar) + ".class";
	}
}
