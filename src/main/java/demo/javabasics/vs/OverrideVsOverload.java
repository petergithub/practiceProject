package demo.javabasics.vs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Broken - incorrect use of overloading!
 * <p>
 * You might expect this program to print "Set," followed by "List" and "Unknown
 * Collection," but it doesn't; it prints out "Unknown Collection" three times.
 * Why does this happen? Because the classify method is overloaded, and the
 * choice of which overloading to invoke is made at compile time. The behavior
 * of this program is counterintuitive because selection among overloaded
 * methods is static, while selection among overridden methods is dynamic
 * <p>
 * refer to EffectiveJava-Joshua Bloch.pdf Item 26: Use overloading judiciously
 * 
 * @version Date: May 7, 2012 1:11:00 PM
 */
@SuppressWarnings("rawtypes")
public class OverrideVsOverload {
	public static String classify(Set s) {
		return "Set";
	}

	public static String classify(List l) {
		return "List";
	}

	public static String classify(Collection c) {
		return "Unknown Collection";
	}

	public static void main(String[] args) {
		//overload: will print Unknown Collection three times
		Collection[] testsOverload = new Collection[] { new HashSet(), // A Set
				new ArrayList(), // A List
				new HashMap().values() // Neither Set nor List
		};
		for (int i = 0; i < testsOverload.length; i++)
			System.out.println(classify(testsOverload[i]));

		//override: will print ABC
		A[] testsOverride = new A[] { new A(), new B(), new C() };
		for (int i = 0; i < testsOverride.length; i++)
			System.out.print(testsOverride[i].name());
	}
}

class A {
	String name() {
		return "A";
	}
}

class B extends A {
	String name() {
		return "B";
	}
}

class C extends A {
	String name() {
		return "C";
	}
}
