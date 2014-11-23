package demo.javabasics.demo;

public class JavapDemo {
	public static void main(String[] args) {
		int i = 0;
		i++;
		// i = i++;
		System.out.println(i);
	}
}

// int i = 0;
// i++;

// 0: iconst_0
// 1: istore_1
// 2: iinc 1, 1

// int i = 0;
// i = i++;

// 0: iconst_0
// 1: istore_1
// 2: iload_1
// 3: iinc 1, 1
// 6: istore_1

// javap -c Inc
// Compiled from "Inc.java"
// public class com.test.Inc extends java.lang.Object{
// public com.test.Inc();
// Code:
// 0: aload_0
// 1: invokespecial #8; //Method java/lang/Object."<init>":()V
// 4: return
//
// public static void main(java.lang.String[]);
// Code:
// 0: new #1; //class com/test/Inc
// 3: dup
// 4: invokespecial #16; //Method "<init>":()V
// 7: astore_1
// 8: iconst_0
// 9: istore_2
// 10: iload_2
// 11: iinc 2, 1
// 14: istore_2
// 15: getstatic #17; //Field java/lang/System.out:Ljava/io/PrintStream;
// 18: iload_2
// 19: invokevirtual #23; //Method java/io/PrintStream.println:(I)V
// 22: return
//
// void fermin(int);
// Code:
// 0: iinc 1, 1
// 3: return
//
// }
