package demo.javabasics.profile.benchmark;

/**
 * BenchmarkLoopTest.java
 * Copyright (c) 2010 by Dr. Herong Yang, herongyang.com
 */
class BenchmarkLoopTest {

	// The empty loop benchmark test method
	public static long emptyLoop(int steps, BenchmarkRunner runner) {
		long result = 0;
		long i = 0;
		runner.startTimer();
		for (i = 0; i < steps; i++) {
		}
		runner.stopTimer();
		result = i;
		return result;
	}
}
