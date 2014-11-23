package demo.designpattern.behavioral.observer;

import java.util.Observable;

public class JekyllAndHyde extends Observable {
	public void drinkPotion() {
		name = "Mr.Hyde";
		setChanged();
		notifyObservers();
	}

	public String getName() {
		return name;
	}

	private String name = "Mr. Jekyll";
}
