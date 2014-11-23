package demo.javabasics.demo;

import java.util.HashSet;
import java.util.Iterator;

/**
 * 如果没有重写hashcode()和equals()方法，hashset会添加相等的元素new Student(1,"zhangsan")
 * remove the comment when you run it the second time. you will get different result from HasSet.
 */
public class HashSetDemo {
	public static void main(String[] args) {
		HashSet<Student> hs = new HashSet<Student>();
		hs.add(new Student(1, "zhangsan"));
		hs.add(new Student(2, "lisi"));
		hs.add(new Student(3, "wangwu"));
		hs.add(new Student(1, "zhangsan"));

		Iterator<Student> it = hs.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}

class Student {
	int num;
	String name;

	Student(int num, String name) {
		this.num = num;
		this.name = name;
	}

	// 利用了String中重写的HashCode()方法，String中equals()相等 ,则hashcode也相等
//	public int hashCode() {
//		return num * name.hashCode();
//	}
//
//	public boolean equals(Object o) {
//		Student s = (Student) o;
//		return num == s.num && name.equals(s.name);
//	}

	public String toString() {
		return num + ":" + name;
	}
}
