package demo.python;

import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.pu.utils.StringUtils;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.*;

@Slf4j
public class PracticePythonTest {
    /**
     * PYTHON_ABSOLUTE_PATH
     */
    String python3 = "python3";

    private String pythonScriptPath = "/Users/pu/sp/Dropbox/base/practice/practiceProject/src/main/python/pytest/";
    private String pythonScript = pythonScriptPath + "model_2.py";
    private String parameters = " 10 32";
    String command = "python3 " + pythonScript + parameters;

    @Test
    public void runtime() throws IOException {
        String prg = "import sys\nprint int(sys.argv[1])+int(sys.argv[2])\n";
        BufferedWriter out = new BufferedWriter(new FileWriter("test1.py"));
        out.write(prg);
        out.close();

        int number1 = 10;
        int number2 = 32;
        Process p = Runtime.getRuntime().exec("python test1.py " + number1 + " " + number2);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String readLine = in.readLine();
        Assert.assertEquals("42", readLine);

        int ret = new Integer(readLine).intValue();
        Assert.assertEquals(42, ret);
    }

    @Test
    public void runtimeWithExistFile() throws IOException {
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String ret = in.readLine();
        Assert.assertEquals("42", ret);
    }

    @Test
    public void runtimeMultipleThreadUtil() throws IOException, InterruptedException {
        String result = ExcuteShell.excuteCmd_multiThread(command);
        Assert.assertEquals("42", result);
    }

    @Test
    public void processBuilder() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(python3, pythonScript, "10", "32");
        pb.redirectErrorStream(true).inheritIO();

        Process p = pb.start();
        p.waitFor();

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String ret = "";
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            if (StringUtils.isNotEmpty(line)) {
                ret = line;
            }
        }
        in.close();

        int exitCode = p.exitValue();
        Assert.assertEquals(0, exitCode);

        Assert.assertEquals("42", ret);
    }

    /**
     * Jython在执行普通py脚本时速度很慢，而且在含有第三方库（requests, jieba…）时bug很多,不易处理。 原因在于，python执行时的sys.path和Jython的sys.path路径不一致，以及Jython的处理不是很好。
     * https://cloud.tencent.com/developer/article/1194418
     */
    @Test
    public void jython() {
        PythonInterpreter python = new PythonInterpreter();

        python.exec("a=[5,2,3,9,4,0]; ");
        python.exec("print(sorted(a));");  //此处python语句是3.x版本的语法
        python.exec("print sorted(a);");   //此处是python语句是2.x版本的语法

        int number1 = 10;
        int number2 = 32;
        python.set("number1", new PyInteger(number1));
        python.set("number2", new PyInteger(number2));
        python.exec("number3 = number1+number2");
        PyObject number3 = python.get("number3");
        Assert.assertEquals(42, number3.__tojava__(Integer.class));

        PySystemState sys = Py.getSystemState();
        //依赖 model_1.py 所以需要导入
        sys.path.add(pythonScriptPath); // import dependency module
        python.execfile(pythonScript);
        PyFunction func = (PyFunction) python.get("add", PyFunction.class);
        PyObject pyobj = func.__call__(new PyInteger(number1), new PyInteger(number2));
        Assert.assertEquals(42, pyobj.__tojava__(Integer.class));
    }

    @Test
    public void runtimeMultipleThread() throws IOException, InterruptedException {
        final Process process = Runtime.getRuntime().exec(command);
        printMessage(process.getInputStream());
        printMessage(process.getErrorStream());
        int waitForFlag = process.waitFor();
        System.out.println(waitForFlag);
    }

    private static void printMessage(final InputStream input) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Reader reader = new InputStreamReader(input);
                BufferedReader bf = new BufferedReader(reader);
                String line = null;
                try {
                    while ((line = bf.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

}
