package demo.designpattern.behavioral.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * Person implements Observer, 所以这个类的对象可以观察一个Observable对象。
 * <p>
 * 当收到观察对象改变的通知时，将调用update()。这里只是输出这个是谁以及他们说了什么。
 */

public class Person implements Observer {
	public Person(String name, String says) {
		this.name = name;
		this.says = says;
	}

	// Called when observing an object that changes
	public void update(Observable thing, Object o) {
		System.out.println("It's " + ((JekyllAndHyde) thing).getName() + "\n"
				+ name + ": " + says);
	}

	private String name;
	private String says;
}
