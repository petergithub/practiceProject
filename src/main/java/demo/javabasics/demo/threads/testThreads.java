package demo.javabasics.demo.threads;

import java.util.Vector;

/**
 * not sure about the design about the situation. looks like Class3 get a field
 * reference of Class2 see <a
 * href='http://cantellow.iteye.com/blog/981687'>无状态类在并发环境中</a>
 * <p>
 * 用eclipse在if (!vector.contains(element))打上断点，
 * 然后让main线程和thread0线程交替执行，最后输出结果为: [cantellow, cantellow]
 * 虽然正常run程序的话，只输出[cantellow]，但是不排除上面的可能，特别是在大型应用程序中。
 * 所以，无状态的类在并发情况中不能说是绝对安全的，一般静态变量对象是很容易被共享出来的
 * ，即使这个静态变量本身是线程安全的（vector），也不能保证它被多个栈引用时能确保正确的行为。
 * 对于有状态的类我们很谨慎，因为有状态就意味着对象中的方法能够任意访问它
 * ，对于无状态的类来说，我们也需要谨慎对待，因为无状态类的方法很可能获取堆上其他已存在对象的引用
 * ，而不是重新生成对象，当这个对象是唯一的时候（静态变量很容易做到这一点），如果我们不对此方法进行同步或者协调，那么很可能，就会出现并发问题。
 * 同样的道理，你也不能说不可变类在并发环境中是安全的。
 */
public class testThreads {
	public static void main(String[] args) {
		Class3 c = new Class3();
		Thread t = new Thread(new Runnable() {
			public void run() {
				Class3 c = new Class3();
				c.doSomeThing();
			}
		});
		t.start();
		c.doSomeThing();
		System.out.println(Class2.vector);
	}
}

class Class3 {
	public void doSomeThing() {
		Vector<String> vector;
		Class2 c = new Class2();
		vector = c.getVector();
		String element = "cantellow";
		if (!vector.contains(element)) {// 在此处打上断点
			vector.add(element);
		}
	}
}

class Class2 {
	protected static Vector<String> vector = new Vector<String>();

	public Vector<String> getVector() {
		return vector;
	}

}
