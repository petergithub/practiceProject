package demo.javabasics.demo;

import java.util.EnumSet;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

public class EnumDemo {
    private static final Logger log = LoggerFactory.getLogger(EnumDemo.class);

	public enum FoodEnum {
		HAMBURGER, FRIES, HOTDOG, ARTICHOKE;
		public static FoodEnum favoriteFood() {
			return ARTICHOKE;
		}
	}

	public enum FoodPriceEnum {
		HAMBURGER(7), FRIES(2), HOTDOG(3), ARTICHOKE(4);
		FoodPriceEnum(int price) {
			this.price = price;
		}

		private final int price;

		public int getPrice() {
			return price;
		}
	}

    public enum FoodCustomizeNameEnum {
        HAMBURGER("Hamburger", 7), FRIES("Fries", 2), HOTDOG("Hotdog", 3), ARTICHOKE("Artichoke", 4);
        
        FoodCustomizeNameEnum(String alias, int price) {
            this.price = price;
        }

        private final int price;

        public int getPrice() {
            return price;
        }
    }

	
	@Test
	public void enumName() {
	    Assert.assertEquals(FoodEnum.FRIES.name(), "FRIES");
	    Assert.assertEquals(FoodPriceEnum.FRIES.name(), "FRIES");
	    Assert.assertEquals(FoodCustomizeNameEnum.FRIES.name(), "Fries");
	    Assert.assertEquals(FoodEnum.values().length, 4);
	}
	
	@Test
	public void testFood() {
		for (FoodEnum f : FoodEnum.values()) {
			System.out.println("Food: " + f);
			if (f == FoodEnum.FRIES) {
				System.out.println("I found the fries!");
			}
			if (f == FoodEnum.favoriteFood()) {
				System.out.println("I found my favorite food!");
			}
		}

		for (FoodEnum f : EnumSet.range(FoodEnum.FRIES, FoodEnum.ARTICHOKE)) {
			System.out.println("More food: " + f);
		}
	}

    @Test
	public void testFood2() {
		for (FoodPriceEnum f : FoodPriceEnum.values()) {
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
