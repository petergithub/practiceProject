package org.pu.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.pu.utils.IoUtils;
import org.pu.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Jul 13, 2012 5:26:40 PM
 */
public class UtilsTest implements Serializable {
	private static final long serialVersionUID = -5883274009216112906L;
	private final Logger log = LoggerFactory.getLogger(UtilsTest.class);

	public void testGetPathFromClass() {
		Class<IoUtils> klass = IoUtils.class;
		log.info("getClassNameFrom() = {}", Utils.getClassName(klass));
		log.info("getPathFromClass() = {}", Utils.getPath(klass));
		log.info("getClassLoaderFrom() {}", Utils.getClassLoader(klass));
	}

	public void testDeepClone() throws IOException, ClassNotFoundException {
		List<UtilsTest> src = new ArrayList<UtilsTest>();
		UtilsTest o = new UtilsTest();
		src.add(o);
		List<?> copy = Utils.deepClone(src);
		log.info("copy = {}", copy);
	}

	public void testGetJarPath() {
		Utils.getJarPath(Assert.class);
	}

	@Test
	public void testgetAllJarPaths() {
		List<String> list = Utils.getAllJarPaths();
		log.info("list = {}", list);
	}

	@Test
	public void testgetAllJars() {
		List<String> list = Utils.getAllJars();
		log.info("list = {}", list);
	}

	public void testGetSuperClassGenricType() {
		Class<?> superType = Utils.getSuperClassGenricType(demo.javabasics.demo.SerializableDemo.class);
		Assert.assertEquals(Object.class, superType);
	}

	public void testGetVersion() {
		String version = Utils.getVersion(junit.framework.Assert.class, "4.10");
		log.info("version = {}", version);
		Assert.assertEquals("4.10", version);
	}

	public void testGetProperties() {
		String propertyFile = "log4j.properties";
		Properties config = IoUtils.getProperties(propertyFile);
		Assert.assertNotNull(config);
		log.info("logpath = {}", config.get("log.dir"));
	}
}
