package doing;

/**
 * @version Date: Jun 4, 2014 2:51:40 PM
 * @author Shang Pu
 */
public class Something {
	public static void main(String[] args) {
		Other o = new Other();
		new Something().addOne(o);
		System.out.println(o.i);
	}
	public void addOne(final Other o){
		o.i++;
		System.out.println(o.i);
	}
}

class Other {
	public int i;
}
