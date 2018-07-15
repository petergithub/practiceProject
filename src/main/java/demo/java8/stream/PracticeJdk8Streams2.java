package demo.java8.stream;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

/**
 * @author http://www.importnew.com/11908.html
 */
public class PracticeJdk8Streams2 extends TestBase {
    private static final Logger log = LoggerFactory.getLogger(PracticeJdk8Streams2.class);

    @Test
    public void testStream() {
        final Collection<Task> tasks = Arrays.asList(new Task(Status.OPEN, 5), new Task(Status.OPEN, 13),
                new Task(Status.CLOSED, 8));

        // 第一个问题是所有状态为OPEN的任务一共有多少分数？
        // 在Java 8以前，一般的解决方式用foreach循环，但是在Java 8里面我们可以使用stream：一串支持连续、并行聚集操作的元素。
        // 第一，task集合被转换化为其相应的stream表示。然后，filter操作过滤掉状态为CLOSED的task。
        // 下一步，mapToInt操作通过Task::getPoints这种方式调用每个task实例的getPoints方法把Task的stream转化为Integer的stream。
        // 最后，用sum函数把所有的分数加起来，得到最终的结果。
        // Calculate total points of all active tasks using sum()
        final long totalPointsOfOpenTasks = tasks.stream().filter(task -> task.getStatus() == Status.OPEN)
                .mapToInt(Task::getPoints).sum();
        System.out.println("Total points: " + totalPointsOfOpenTasks);
        Assert.assertEquals(totalPointsOfOpenTasks, 18);

        // 下面这个例子和第一个例子很相似，但这个例子的不同之处在于这个程序是并行运行的，其次使用reduce方法来算最终的结果。
        // Calculate total points of all tasks
        final double totalPoints = tasks.stream().parallel().map(task -> task.getPoints())// or map(Task::getPoints)
                .reduce(0, Integer::sum);

        System.out.println("Total points (all tasks): " + totalPoints);
        Assert.assertEquals(totalPoints, 26.0);

        // 按照某种准则来对集合中的元素进行分组
        // Group tasks by their status
        final Map<Status, List<Task>> map = tasks.stream().collect(Collectors.groupingBy(Task::getStatus));
        System.out.println(map);
        Assert.assertEquals(map.toString(), "{CLOSED=[[CLOSED, 8]], OPEN=[[OPEN, 5], [OPEN, 13]]}");
        
        // 计算整个集合中每个task分数（或权重）的平均值
        // Calculate the weight of each tasks (as percent of total points)
        final Collection< String > result = tasks
            .stream()                                        // Stream< String >
            .mapToInt( Task::getPoints )                     // IntStream
            .asLongStream()                                  // LongStream
            .mapToDouble( points -> points / totalPoints )   // DoubleStream
            .boxed()                                         // Stream< Double >
            .mapToLong( weigth -> ( long )( weigth * 100 ) ) // LongStream
            .mapToObj( percentage -> percentage + "%" )      // Stream< String> 
            .collect( Collectors.toList() );                 // List< String > 
                 
        System.out.println( result );
        Assert.assertEquals(result.toString(), "[19%, 50%, 30%]");
        
        // Stream API不仅仅处理Java集合框架。像从文本文件中逐行读取数据这样典型的I/O操作也很适合用Stream API来处理。
        String filename = "/tmp/.gitignore";
        final Path path = new File(filename).toPath();
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.onClose(() -> System.out.println("Done!")).forEach(System.out::println);
        } catch (IOException e) {
           log.error("Exception in PracticeJdk8Streams2.testStream()", e);
        }
    }

    private enum Status {
        OPEN, CLOSED
    };

    /**
     * Task类有一个分数的概念（或者说是伪复杂度），其次是还有一个值可以为OPEN或CLOSED的状态.
     */
    private static final class Task {
        private final Status  status;
        private final Integer points;

        Task(final Status status, final Integer points) {
            this.status = status;
            this.points = points;
        }

        public Integer getPoints() {
            return points;
        }

        public Status getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return String.format("[%s, %d]", status, points);
        }
    }
}
