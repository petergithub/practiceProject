package demo.java8.stream.lambdasinaction;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StreamBasic {

    public static void main(String...args){
        // Java 7
        getLowCaloricDishesNamesInJava7(Dish.menu).forEach(System.out::println);

        // Java 8
        getLowCaloricDishesNamesInJava8(Dish.menu).forEach(System.out::println);
        
        getTopThreeCaloricDishesNamesInJava8(Dish.menu).forEach(System.out::println);
        
        getHighThreeCaloricDishesNamesInJava8AppallingDebugStyle(Dish.menu).forEach(System.out::println);
    }

    public static List<String> getLowCaloricDishesNamesInJava8(List<Dish> dishes){
        System.out.println("\ngetLowCaloricDishesNamesInJava8");
        return dishes.stream()
                .filter(d -> d.getCalories() < 400)
                .sorted(comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(toList());
    }
    
    public static List<String> getTopThreeCaloricDishesNamesInJava8(List<Dish> dishes){
        System.out.println("\ngetHighThreeCaloricDishesNamesInJava8");
        
    	return dishes.stream()
    			.filter(d -> d.getCalories() > 300)
    			.sorted(comparing(Dish::getCalories))
    			.map(Dish::getName)
    			.limit(3)
    			.collect(toList());
    }
    
    public static List<String> getHighThreeCaloricDishesNamesInJava8AppallingDebugStyle(List<Dish> dishes){
        System.out.println("\rgetHighThreeCaloricDishesNamesInJava8AppallingDebugStyle");
        
    	return dishes.stream()
    			.filter(d -> {
    						System.out.println("Name:" + d.getName() + " in filter");
    						return d.getCalories() > 300;
    					})
    			.map(d -> {
					System.out.println("Name:" + d.getName() + " in map");
					return d.getName();
				})
    			.limit(3)
    			.collect(toList());
    }

    public static List<String> getLowCaloricDishesNamesInJava7(List<Dish> dishes){
        List<Dish> lowCaloricDishes = new ArrayList<>();
        for(Dish d: dishes){
            if(d.getCalories() < 400){
                lowCaloricDishes.add(d);
            }
        }
        List<String> lowCaloricDishesName = new ArrayList<>();
        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            public int compare(Dish d1, Dish d2){
                return Integer.compare(d1.getCalories(), d2.getCalories());
            }
        });
        for(Dish d: lowCaloricDishes){
            lowCaloricDishesName.add(d.getName());
        }
        return lowCaloricDishesName;
    }
}
