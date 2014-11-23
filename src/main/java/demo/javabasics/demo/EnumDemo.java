package demo.javabasics.demo;

import java.util.EnumSet;

public class EnumDemo {

	public enum Food {
		HAMBURGER, FRIES, HOTDOG, ARTICHOKE;
		public static Food favoriteFood() {
			return ARTICHOKE;
		}
	}

	public enum Food2 {
		HAMBURGER(7), FRIES(2), HOTDOG(3), ARTICHOKE(4);
		Food2(int price) {
			this.price = price;
		}

		private final int price;

		public int getPrice() {
			return price;
		}
	}

	@org.junit.Test
	public void testFood() {
		for (Food f : Food.values()) {
			System.out.println("Food: " + f);
			if (f == Food.FRIES) {
				System.out.println("I found the fries!");
			}
			if (f == Food.favoriteFood()) {
				System.out.println("I found my favorite food!");
			}
		}

		for (Food f : EnumSet.range(Food.FRIES, Food.ARTICHOKE)) {
			System.out.println("More food: " + f);
		}
	}

	public void testFood2() {
		for (Food2 f : Food2.values()) {
			System.out.print("Food2: " + f + ", ");

			if (f.getPrice() >= 4) {
				System.out.print("Expensive, ");
			} else {
				System.out.print("Affordable, ");
			}

			switch (f) {
				case HAMBURGER:
					System.out.println("Tasty");
					continue;
				case ARTICHOKE:
					System.out.println("Delicious");
					continue;
				default:
					System.out.println("OK");
			}
		}
	}
}
