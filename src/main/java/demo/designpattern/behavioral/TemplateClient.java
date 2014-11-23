package demo.designpattern.behavioral;

/**
 * 模板方法定义了一系列算法步骤，子类可以去实现/覆盖其中某些步骤，但
 * 不能改变这些步骤的执行顺序
 */
public class TemplateClient {

}

abstract class HappyNewYear {
	public void celebrateSpringFestival() {
		subscribeTicket();
		travel();
		celebrate();
	}

	protected final void subscribeTicket() {
		// …
	}

	protected abstract void travel();

	protected final void celebrate() {
		// …
	}
}

class PassengerByAir extends HappyNewYear {
	@Override
	protected void travel() {
		System.out.println("Travelling by Air...");
	}

	public void main(String[] args) {
		HappyNewYear passengerByAir = new PassengerByAir();
		HappyNewYear passengerByCoach = new PassengerByCoach();
		HappyNewYear passengerByTrain = new PassengerByTrain();
		System.out.println("Let's Go Home For A Grand Family Reunion...\n");
		System.out.println("Tom is going home:");
		passengerByAir.celebrateSpringFestival();
		System.out.println("\nRoss is going home:");
		passengerByCoach.celebrateSpringFestival();
		System.out.println("\nCatherine is going home:");
		passengerByTrain.celebrateSpringFestival();
	}
}

class PassengerByCoach extends HappyNewYear {
	@Override
	protected void travel() {
		System.out.println("Travelling by Coach...");
	}
}

class PassengerByTrain extends HappyNewYear {
	@Override
	protected void travel() {
		System.out.println("Travelling by Train...");
	}
}
