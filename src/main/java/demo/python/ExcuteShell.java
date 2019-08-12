package demo.python;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * used to execute shell
 * https://cloud.tencent.com/developer/article/1194418
 * https://www.kancloud.cn/tuna_dai_/day01/443344
 */
@Slf4j
public class ExcuteShell {

    public static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();

    public static int corePoolSize = 3;
    public static int maximumPoolSize = 5;
    public static long keepAliveTime = 10000L;
    public static ExecutorService singleThreadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
            keepAliveTime, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 执行外部程序,并获取标准输出
     */
    public static String excuteCmd_multiThread(String command) {
        BufferedReader bReader = null;
        InputStreamReader sReader = null;
        try {
            Process p = Runtime.getRuntime().exec(command);

            /* 为"错误输出流"单独开一个线程读取之,否则会造成标准输出流的阻塞 */
            singleThreadPool.execute(new InputStreamRunnable(p.getErrorStream(), "ErrorStream"));

            /* "标准输出流"就在当前方法中读取 */
            BufferedInputStream bis = new BufferedInputStream(p.getInputStream());

            sReader = new InputStreamReader(bis, StandardCharsets.UTF_8);
            bReader = new BufferedReader(sReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bReader.readLine()) != null) {
                sb.append(line);
                // If multiple line append with LINE_SEPARATE
                // sb.append("\n");
            }

            bReader.close();
            p.destroy();
            return sb.toString();
        } catch (Exception e) {
            log.error("Exception in excuteCmd_multiThread", e);
            return "result: error";
        } finally {
        }
    }
}

class InputStreamRunnable implements Runnable {
    BufferedReader bReader = null;
    String type = null;

    public InputStreamRunnable(InputStream is, String _type) {
        try {
            bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is), "UTF-8"));
            type = _type;
        } catch (Exception ex) {
        }
    }

    @Override
    public void run() {
        String line;
        int lineNum = 0;

        try {
            while ((line = bReader.readLine()) != null) {
                lineNum++;
                // Thread.sleep(200);
            }
            bReader.close();
        } catch (Exception ex) {
        }
    }
}
