package doing;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompilerTest {
	private final static Logger log = LoggerFactory.getLogger(CompilerTest.class);

	public static void main(String[] args) throws Exception {
		String source = "public class Main { public static void main(String[] args) {System.out.println(\"Hello World!\");} }";
		CompilationTask task = getCompilationTask(source, "Main");
		boolean result = task.call();
		if (result) {
			log.info("Compile successfully");
		}
	}

	@org.junit.Test
	public void testCalculate() {
		String expr = "3+4";
		try {
			double result = calculate(expr);
			log.info("result of expr = " + result);
		} catch (Exception e) {
			log.error("Exception in CompilerTest.testCalculate()", e);
		}
	}

	private static double calculate(String expr) throws Exception {
		String className = "CalculatorMain";
		String methodName = "calculate";
		String source = "public class " + className + " { public static double " + methodName
				+ "() { return " + expr + "; } }";
		CompilationTask task = getCompilationTask(source, className);
		boolean result = task.call();
		if (result) {
			// ClassLoader loader = CompilerTest.class.getClassLoader();
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			Class<?> clazz = loader.loadClass(className);
			Method method = clazz.getMethod(methodName, new Class<?>[] {});
			Object value = method.invoke(null, new Object[] {});
			return (Double) value;
		} else {
			throw new Exception("Wrong expression");
		}
	}

	public static CompilationTask getCompilationTask(String str, String className)
			throws URISyntaxException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		StringSourceJavaObject src = new StringSourceJavaObject(className, str);
		Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(src);
		CompilationTask task = compiler.getTask(null, fileManager, null, null, null, fileObjects);
		return task;
	}
}

class StringSourceJavaObject extends SimpleJavaFileObject {
	private String content = null;

	public StringSourceJavaObject(String name, String content) throws URISyntaxException {
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.content = content;
	}

	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		return content;
	}
}
