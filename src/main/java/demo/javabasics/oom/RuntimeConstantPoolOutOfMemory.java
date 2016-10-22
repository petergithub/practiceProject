package demo.javabasics.oom;

import org.junit.Test;

import junit.framework.Assert;

/**
 * 深入理解Java虚拟机: JVM高级特性与最佳实践
 * @version Date: Oct 7, 2016 8:25:07 PM
 */
public class RuntimeConstantPoolOutOfMemory {
	
	/**
	 * 深入理解Java虚拟机: JVM高级特性与最佳实践 P57 
	 * JDK 6: return false false
	 * JDK 7, 8: return true false
	 */
	@Test
	public void constantPool() {
		String str1 = new StringBuilder("计算机").append("软件").toString();
//		Assert.assertFalse(str1.intern() == str1); // JDK 6: false
		Assert.assertTrue(str1.intern() == str1); // JDK 7, 8: true
		
		String str2 = new StringBuilder("ja").append("va").toString();
		Assert.assertFalse(str2.intern() == str2); // JDK 6, 7, 8: return false
	}
}
