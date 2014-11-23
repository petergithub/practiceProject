package demo.javabasics.demo.annotation;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lighter
 * refer to http://annotation.group.iteye.com/group/wiki/385-实战篇: 设计自己的Annotation
 * @version Date: May 12, 2012 3:51:22 PM
 */
public class TestAnnotation {
	/**
	 * 说明:具体关天Annotation的API的用法请参见javaDoc文档
	 */
	public static void main(String[] args) throws Exception {
		String CLASS_NAME = "helloworld.javabasics.demo.annotation.JavaEyer";
		Class<?> test = Class.forName(CLASS_NAME);
		boolean flag = test.isAnnotationPresent(Description.class);
		if (flag) {
			Description des = (Description) test
					.getAnnotation(Description.class);
			System.out.println("描述:" + des.value());
			System.out.println("-----------------");
		}

		// 把JavaEyer这一类有利用到@Name的全部方法保存到Set中去
		Method[] method = test.getMethods();
		Set<Method> set = new HashSet<Method>();
		for (int i = 0; i < method.length; i++) {
			boolean otherFlag = method[i].isAnnotationPresent(Name.class);
			if (otherFlag) set.add(method[i]);
		}
		for (Method m : set) {
			Name name = m.getAnnotation(Name.class);
			System.out.println(name.originate());
			System.out.println("创建的社区:" + name.community());
		}
	}
}
