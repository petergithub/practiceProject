package demo.javabasics.profile.benchmark;

/**
 * BenchmarkRunner.java
 * Copyright (c) 2010 by Dr. Herong Yang, herongyang.com
 * <p>
 * Micro benchmark is a benchmark designed to measure the performance of a very
 * small and specific piece of code.
 * <li># A benchmark program can be designed in 2 parts: a benchmark runner and
 * a test method.</li>
 * <li># "-XX:+PrintCompilation" option allows you to watch the JIT compilation
 * log.</li>
 * <li># "-XX:+PrintGC" option allows you to watch the GC log.</li>
 * <li># "-Xms" and "-Xmx" options allow you to allocate a large memory to avoid
 * GC.</li>
 * <li># "-Xint" option allows you to run test in interpreted-only mode -
 * disabling the JIT compiler.</li>
 * <p>
 * One good example of Java Micro Benchmark programs is the Java Microbenchmark
 * Applet maintained by Jonathan Hardwick at
 * http://www.cs.cmu.edu/~jch/java/microbench.html. It is designed to measure
 * the performance of Java basic operations like:
 * 
 * <pre>
 * Loop overhead: while (Go) n++
 * Local variable assignment: i = n
 * Instance var. assign.: this.i = n
 * Array element assign.: a[0] = n
 * Byte increment: b++
 * Short increment: s++
 * Int increment: i++
 * Long increment: l++
 * Float increment: f++
 * Double increment: d++
 * Object creation: new Object()
 * Array creation: new int[10]
 * Method call: null_func()
 * Synchronous call: sync_func()
 * Math function: abs()
 * Inline code: (x < 0) ? -x : x
 * </pre>
 * 
 * If you want to write your own micro benchmark programs, you need to consider:
 * <li>Compiler optimization - Compiler may modify your code for optimization.
 * For example, expression "Hello " + "Herong!" will probably be optimized as
 * "Hello Herong!". You need to write your code carefully to avoid compiler
 * optimization.</li>
 * <li>Java Virtual Machine (JVM) warm up - When JVM is started, it runs your
 * application thread together with other threads to warm up the system. If you
 * run your benchmark test immediately as after JVM started, warm up threads may
 * affect your test code performance. You need to give JVM a warm up period
 * before starting your benchmark test code.</li>
 * <li>Garbage Collection (GC) - GC thread may affect your test code
 * performance. You need to avoid garbage collection or turn off garbage
 * collection if possible.</li>
 * <li>Just In Time (JIT) compilation - Some JVM like HotSpot identifies code
 * hot spots and compile them into native code to speedup execution. If you want
 * to test Java class code interpretation speed, you should turn off JIT
 * compilation. If you want to test native code speed, you should JIT
 * compilation of your test code first.</li>
 * <li>Class loading and initialization - Be aware class loading and
 * initialization effects on your test code. You need to load all classes before
 * starting your benchmark test code.</li>
 * <li>Class loading and initialization - Be aware class loading and
 * initialization effects on your test code. You need to load all classes before
 * starting your benchmark test code.</li>
 * <li>Environment noise - Applications other than the JVM running on your
 * computer may affect your test code performance. You need to turn off other
 * applications, like virus scanner, Yahoo messenger, etc., as much as possible
 * on your computer before running your benchmark test code.
 */
class BenchmarkRunner {
	static java.io.PrintStream out = System.out;
	static java.io.InputStream in = System.in;
	long startTime = 0;
	long endTime = 0;
	long[] timeRecords = null;

	/**
	 * run it:
	 * java helloworld.javabasics.benchmark.BenchmarkLoopTest emptyLoop 3 3 3
	 * <p>
	 * Note that:
	 * <li>A simple output and input dialog step is used to warm up the JVM. You
	 * can wait as long as you needed to make sure that the JVM is fully warmed
	 * up.</li>
	 * <li>The test class and test method are explicitly loaded and prepared.</li>
	 * <li>In order to warm up the JIT compiler, the test method is called in a
	 * loop. The number of iterations of this loop is controlled by the
	 * "warmups" parameter, which should be large enough to fully wake up the
	 * JIT compiler - triggering it to compile, and sometimes recompile, the
	 * test method into native codes.</li>
	 * <li>After JIT compilation, the test method is called in the benchmark
	 * test loop. The number of iterations of this loop is controlled by the
	 * "runs" parameter, which allows you to perform the benchmark test multiple
	 * times and take the average execution time.</li>
	 * <li>The "steps" parameter is passed to the test method, just in case the
	 * test code needs to be repeated in multiple times.</li>
	 * <li>Two callback methods, startTimer() and stopTimer(), are provided for
	 * the test method to call back to provide execution time measurements.</li>
	 * <li>Other methods are provided to record execution time of each test run
	 * and report test result.</li>
	 */
	public static void main(String[] args) {
		if (args.length < 5) {
			out.println("Missing arguments. Usage: ");
			out.println("BenchmarkRunner class method warmups runs steps");
			return;
		}
		try {
			String className = args[0];
			String methodName = args[1];
			int numberOfWarmups = Integer.parseInt(args[2]);
			int numberOfRuns = Integer.parseInt(args[3]);
			int numberOfSteps = Integer.parseInt(args[4]);

			// Warming up the JVM
			out.println("Are you ready?");
			in.read(new byte[1]);

			// Loading the benchmark class and method
			Class<?> testClass = Class.forName(className);
			java.lang.reflect.Method testMethod = testClass.getMethod(
					methodName, int.class, BenchmarkRunner.class);
			BenchmarkRunner testRunner = new BenchmarkRunner(numberOfRuns);
			Object testObject = testClass.newInstance();

			// JIT warmup
			out.println();
			out.println("Waking up the JIT compiler...");
			for (int i = 0; i < numberOfWarmups; i++) {
				Object testResult = testMethod.invoke(testObject,
						numberOfSteps, testRunner);
				out.println("Run: " + i + ", Time: " + testRunner.returnTime()
						+ ", Test returns: " + testResult);
			}

			// Benchmark runs
			out.println();
			out.println("Starting benchmark test runs...");
			for (int i = 0; i < numberOfRuns; i++) {
				Object testResult = testMethod.invoke(testObject,
						numberOfSteps, testRunner);
				testRunner.recordTime(i);
				out.println("Run: " + i + ", Time: " + testRunner.returnTime()
						+ ", Test returns: " + testResult);
			}

			// Benchmark report
			out.println();
			out.println("Benchmark test time report...");
			testRunner.report(numberOfRuns, numberOfSteps);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Constructor
	public BenchmarkRunner(int runs) {
		timeRecords = new long[runs];
	}

	// Starting the timer - to be called by test method
	public void startTimer() {
		startTime = System.nanoTime();
	}

	// Stopping the timer - to be called by test method
	public void stopTimer() {
		endTime = System.nanoTime();
	}

	// Returning time from the timer
	public long returnTime() {
		return endTime - startTime;
	}

	// Recording time from the timer
	public void recordTime(int i) {
		timeRecords[i] = endTime - startTime;
	}

	// Reportting benchmark result
	public void report(int runs, int steps) {
		long total = 0;
		long minimum = Long.MAX_VALUE;
		long maximum = 0;
		for (int i = 0; i < runs; i++) {
			long t = timeRecords[i];
			total += t;
			if (t > maximum) maximum = t;
			if (t < minimum) minimum = t;
		}
		long average = total / runs;
		out.println("Runs: " + runs + ", Ave: " + average / steps + ", Min: "
				+ minimum / steps + ", Max: " + maximum / steps
				+ " - Per step in nanoseconds");
		out.println("Runs: " + runs + ", Ave: " + average + ", Min: " + minimum
				+ ", Max: " + maximum + " - All steps in nanoseconds");
		out.println("Runs: " + runs + ", Ave: " + average / 1000000 + ", Min: "
				+ minimum / 1000000 + ", Max: " + maximum / 1000000
				+ " - All steps in milliseconds");
		out.println("Runs: " + runs + ", Ave: " + average / 1000000000
				+ ", Min: " + minimum / 1000000000 + ", Max: " + maximum
				/ 1000000000 + " - All steps in seconds");
	}

	// Constructor needed as a sample benchmark test class
	public BenchmarkRunner() {
	}

	// A sample benchmark test method
	public static long sampleTest(int steps, BenchmarkRunner runner) {
		long total = 0;
		runner.startTimer();
		for (int i = 0; i < steps; i++) {
			total += i;
		}
		runner.stopTimer();
		return total;
	}
}
