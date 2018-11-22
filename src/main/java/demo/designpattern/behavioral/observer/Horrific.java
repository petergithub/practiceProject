package demo.designpattern.behavioral.observer;

import java.util.Observer;

public class Horrific {
	public static void main(String args[]) {
		JekyllAndHyde man = new JekyllAndHyde();

		Observer[] crowd = {
				new Person("Officer", "What's all this then?"),
				new Person("Eileen Backwards",
						"Oh, no, it's horrible - those teeth!"),
				new Person("Phil McCavity",
						"I'm your local dentist - here' s my card."),
				new Person("Slim Sagebrush",
						"What in tarnation's goin' on here?"),
				new Person("Freaky Weirdo",
						"Real cool, man. Where can I get that strff?") };

		// Add the observers
		for (Observer observer : crowd) {
			man.addObserver(observer); // 注册
		}
		man.drinkPotion();
	}
}
