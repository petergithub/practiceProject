package demo.designpattern.structural;

import java.util.LinkedList;
import java.util.List;

public class StategyClient {
	public static void main(String[] args) {
		HappyPeople passengerByAirAndCoach = new PassengerByAirAndCoach();
		System.out.println("Let's Go Home For A Grand Family Reunion...\n");
		System.out.println("Jennifer is going home:");
		passengerByAirAndCoach.celebrateSpringFestival();
	}
}

class ByAir implements Travellable {
	@Override
	public void travel() {
		// travel logic...
		System.out.println("Travelling by Air...");
	}
}

class ByCoach implements Travellable {
	@Override
	public void travel() {
		// travel logic...
		System.out.println("Travelling by Coach...");
	}
}

class ByTrain implements Travellable {
	@Override
	public void travel() {
		// travel logic...
		System.out.println("Travelling by Train...");
	}
}

class PassengerByAirAndCoach extends HappyPeople {
	private Travellable first;
	private Travellable second;

	public PassengerByAirAndCoach() {
		first = new ByAir();
		second = new ByCoach();
	}

	@Override
	public void travel() {
		first.travel();
		second.travel();
	}
}

class HappyPeople {
	private List<Travellable> transportationList;

	public HappyPeople() {
		transportationList = new LinkedList<Travellable>();
	}

	public void celebrateSpringFestival() {
		subscribeTicket();
		travel();
		celebrate();
	}

	protected final void subscribeTicket() {
		// …
	}

	public void travel() {
		for (Travellable travel : transportationList) {
			travel.travel();
		}
	}

	protected final void celebrate() {
		// …
	}
}

interface Travellable {
	void travel();
}