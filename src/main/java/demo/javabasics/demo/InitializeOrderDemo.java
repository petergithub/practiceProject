package demo.javabasics.demo;

//java初始化顺序和原理简记
//
//Java初始化顺序，这个题目估计是个笔试就都会考到。以至于倒背如流者数不胜数。
//从始化顺序的原理，来解释，来理解，或许更好~
//
//http://java-admin.iteye.com/blog/167684 博文对初始化顺序实践的很不错: 
//
//JAVA 类首次装入时，会对静态成员变量或方法进行一次初始化,但方法不被调用是不会执行的，静态成员变量和静态初始化块级别相同，
//非静态成员变量和非静态初始化块级别相同。
//先初始化父类的静态代码--->初始化子类的静态代码-->
//(创建实例时,如果不创建实例,则后面的不执行)初始化父类的非静态代码--->初始化父类构造函数--->初始化子类非静态代码--->初始化子类构造函数
//2 、类只有在使用New调用创建的时候才会被JAVA 类装载器装入
//3、创建类实例时，首先按照父子继承关系进行初始化
//4、类实例创建时候，首先初始化块部分先执行，然后是构造方法；然后从本类继承的子类的初始化块执行，最后是子类的构造方法
//5、类消除时候，首先消除子类部分，再消除父类部分
//
//顺序简记: 
//1．  父类静态成员和静态初始化块 ，按在代码中出现的顺序依次执行
//2．  子类静态成员和静态初始化块 ，按在代码中出现的顺序依次执行
//3．  父类实例成员和实例初始化块 ，按在代码中出现的顺序依次执行
//4．  父类构造方法
//5．  子类实例成员和实例初始化块 ，按在代码中出现的顺序依次执行
//6．  子类构造方法
//
//试着从原理上来分析: 
//1. 类的成员有method、field、innerClass等，分别又有静态和非静态两种
//2. 静态成员是给类用的，非静态成员是给对象用的（对象如果使用静态成员编译会提示）
//3. 类只要被用到，就会加载；必须先加载后使用
//4. 使用类有两种，一种是只使用类的静态成员而不构造对象，这样的话就不需要初始化非静态的成员了；第二种是构造对象，构造对象必须先加载类，此时非静态成员在加载时也就初始化了，之后初始化非静态成员
//5. 为什么先初始化静态成员，后初始化非静态成员
//    静态成员是一次性加载的，而且只加载一次；非静态成员可以使用静态成员，这样必须保证在非静态成员可能用到静态成员之前初始化完毕
//6. 为什么先初始化父类，后初始化子类
//    子类可能用到父类的方法，必须保证子类使用之前是得父类可能用到的成员初始化完毕。
//（1）使用子类静态成员时，不能使用子类自己的非静态成员，不能使用父类的非静态成员，只可能使用到父类的静态成员；这也解释了为什么加载子类（第一次使用子类）时，父类的非静态成员没有初始化
//    （2）使用子类的非静态成员时，可能用到子类自己的静态成员、父类的静态成员、父类的非静态成员
//7. 为什么先初始化成员，后初始化构造函数
//    在构造函数中，可以对成员进行赋值，也可以不进行操作，但是可以使用非静态成员。因此必须保证构造函数可能用到的成员先初始化，否则就会报错。
//8. 为什么父类构造方法执行要在子类非静态成员之前
//子类非静态成员可能用到父类的非静态成员，而父类的非静态成员有可能是要在构造函数中才能正确初始化的，默认的初始化值可能不是理想的。构造函数的目的更多的是为了根据实际需求重新初始化成想要的值以待用。
//
//初始化是个比较复杂的工程，必须保证正确执行，否则就会发生混乱。初始化最基本的目的是: 保证所需要的，可能用到的东西先初始化完毕待用。初始化额外追求: lazy-init效果，不需要的暂不初始化。
/**
 * @author Shang Pu
 * @version Date: Apr 15, 2012 6:24:45 PM
 */
public class InitializeOrderDemo extends InitializeOrderParent {
	// 7. 初始化子类的非静态代码
	public int age = getNumber(1001);
	// 8. 初始化子类的非静态代码
	private int radius = getNumber(10);
	// 3. 初始化子 静态成员变量 static int sage
	static int sage = getNumber(250);
	// 4. 初始化子 静态初始化块 static
	static {
		System.out.println("4th step: init subclass static block");
	}
	{
		System.out.println("init subclass nonstatic block");
	}

	// 9. 初始化子类构造函数
	InitializeOrderDemo(int radius) {
		this.radius = radius;
		System.out.println(age);
		System.out.println("9th step: init subclass constructon method initializeOrder()");
	}

	public void draw() {
		System.out.println("initializeOrder.draw " + radius);
	}

	public static void main(String[] args) {
		new InitializeOrderDemo(1000);
	}
}

abstract class InitializeOrderParent {
	// 5. 初始化父类的非静态代码
	public int age = getNumber(100);
	// 静态成员变量和静态初始化块级别相同 所以按照在代码中的顺序依次执行
	// 1. 初始化父 静态成员变量 static int sage
	static int sage = getNumber(50);
	// 2. 初始化父 静态初始化块 static
	static {
		System.out.println("2nd step: init base static block");
	}
	{
		System.out.println("init base nonstatic block");
	}

	// 6. 初始化父类构造函数
	InitializeOrderParent() {
		System.out.println(age);
		System.out.println("base start");
		draw();// 会调用子类覆盖后的方法
		System.out.println("base end");
	}

	static int getNumber(int base) {
		System.out.println("base.getNumber int" + base);
		return base;
	}

	public void draw() {
		System.out.println("base.draw");
	}
}
