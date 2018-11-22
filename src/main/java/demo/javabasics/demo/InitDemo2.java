package demo.javabasics.demo;

/**
 * 程序流程为: 在主函数中，首先执行new MyBase()，在这个过程中， 子类会首先调用父类的构造函数；在父类的构造函数Base()中执行
 * add()方法。这里需要注意，这个add方法由于是在新建MyBase对象 时调用的，将会首先查找MyBase类中是否有此方法。所以执行的是 子类中的
 * i+=v*2;此时i的值为2.在打印两个2之后，父类的构造 函数执行完毕，执行子类的构造函数，即MyBase()，这里的add(2)
 * 当然也是子类的。i的值变为了6.最后执行go函数。i 即为22
 **/
public class InitDemo2 {
	public static void main(String[] args) {
		go(new MyBase());
	}

	static void go(Base b) {
		b.add(8);
	}

}
class Base {
	int i;// i被默认初始化为0

	Base() {
		add(1);
		System.out.println("in the Base's construction: " + i);
	}

	void add(int v) {
		System.out.println("in the Base's add i= " + i);
		i += v;
		System.out.println("in the Base's add: " + i);
	}

	void print() {
		System.out.println(i);
	}
}

class MyBase extends Base {
	MyBase() {
		add(2);
	}

	void add(int v) {
		System.out.println("in the MyBase's i= " + i);
		i += v * 2;
		System.out.println("in the MyBase's add: " + i);
	}
}


