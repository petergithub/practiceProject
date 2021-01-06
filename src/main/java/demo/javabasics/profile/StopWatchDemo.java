package demo.javabasics.profile;

import org.springframework.util.StopWatch;

public class StopWatchDemo {

    public static void main(String[] args) throws Exception {
        StopWatch clock = new StopWatch("计时器");
        clock.start("Task 1");
        // 假装任务一执行了3秒钟，后面类似
        Thread.sleep(1000 * 1);

        clock.stop();
        clock.start("Task 2");
        Thread.sleep(1000 * 2);

        clock.stop();
        clock.start("Task 3");
        Thread.sleep(1000 * 3);
        clock.stop();

        System.out.println("getTotalTimeSeconds " + clock.getTotalTimeSeconds());
        System.out.println("prettyPrint: " + clock.prettyPrint());
        System.out.println("short summary: " + clock.shortSummary());
        System.out.println("clock: " + clock);
    }
}


