package demo.javabasics.demo;

/**
 * @author Shang Pu
 * @version Date: Apr 18, 2012 8:18:00 AM
 */
public class InnerClassDemo {
	class One {
		private class Two {
			public void main() {
				System.out.println("two");
			}
			{main();}
		}
	}
	One.Two two = new One().new Two();
}
