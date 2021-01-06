package demo.java8.stream;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shang Pu
 * @version Date: Aug 27, 2017 12:13:35 PM
 */
public class PracticeJdk8Stream {
    private static final Logger log = LoggerFactory.getLogger(PracticeJdk8Stream.class);

    @Test
    public void breakTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        Stream<Integer> stream = numbers.stream();
        stream.forEach(
                i -> {
                    log.info("i: {}", i);
                    for (int j = 0; j < 3; j++) {
                        log.info("j {}", j);
                        for (int k = 0; k <2; k++) {
                            log.info("k {}", k);
                            if (k == 0) {
                                break;
                            }
                        }
                    }
                }
        );
    }

    @Test
    public void distinct() {
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        Stream<Integer> stream = numbers.stream();
        stream.filter(i -> i % 2 == 0).distinct().forEach(i -> log.info("i: {}", i));

        numbers.stream().filter(i -> i % 2 == 0).distinct().forEach(System.out::println);
        numbers.stream().filter(i -> i % 2 == 0).distinct().forEach(i -> System.out.println(i));
    }

    /**
     * small to high
     * <p>
     * high to small
     */
    @Test
    public void sorted() {
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        // small to high
        numbers.stream().sorted(Integer::compare).forEach(System.out::println);

        // high to small
        log.info("numbers is sorted from high to small");
        numbers.stream().sorted((x, y) -> x <= y ? 1 : -1).forEach(System.out::println);
    }

    @Test
    public void map() {
        List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");
        List<Integer> wordLengths = words.stream().map(String::length).collect(toList());
        log.info("wordLengths: {}", wordLengths);
    }

    /**
     * input: [1,2,3] and [3,4] <br>
     * output: [1,3],[1,4],[2,3],[2,4],[3,3],[3,4]
     */
    @Test
    public void flatMap1() {
        List<Integer> num1 = Arrays.asList(1, 2, 3);
        List<Integer> num2 = Arrays.asList(3, 4);

        List<Integer[]> pairs = num1.stream()
                .flatMap(i -> num2.stream().map(j -> new Integer[]{i, j})).collect(toList());
        pairs.stream().forEach(i -> System.out.println("[" + i[0] + "," + i[1] + "]"));

        // num1.stream()
        // .flatMap(i -> num2.stream().map(j -> new
        // HashMap<Integer,Integer>().put(i, j))).forEach(System.out::println);

        HashMap<Integer, Integer> map = new HashMap<>();

        num1.stream().flatMap(i -> num2.stream().map(j -> new Integer[]{i, j})).map(i -> {
            System.out.println(i);
            map.put(i[0], i[1]);
            System.out.println("[" + i[0] + "," + i[1] + "]");
            return map;
        });
        log.info("map: {}", map);
    }

    /**
     * input: [1,2,3] and [3,4] <br>
     * output(i+j)%3==0: [2,4],[3,3]
     */
    @Test
    public void flatMap2() {
        List<Integer> num1 = Arrays.asList(1, 2, 3);
        List<Integer> num2 = Arrays.asList(3, 4);

        List<Integer[]> pairs = num1.stream().flatMap(
                i -> num2.stream().filter(j -> (i + j) % 3 == 0).map(j -> new Integer[]{i, j}))
                .collect(toList());
        pairs.stream().forEach(i -> System.out.println("[" + i[0] + "," + i[1] + "]"));
    }
}
