package demo.algorithm.sort;

import org.junit.Test;

import demo.algorithm.sort.HeapSorter;
import demo.algorithm.sort.MergeSorter;
import demo.algorithm.sort.ShellSorter;
import demo.algorithm.sort.SortObject;

/**
 * @author Daniel Cheng
 */
public class SorterTest {
	Integer[] array = { 5, 1, 13, 2, 17, 9, 7, 4, 0 };
	String[] strArray = { "a", "c", "d", "b" };

	@Test
	public void testHeapSorter() {
		SortObject<Integer> heapSorter = new HeapSorter<Integer>();
		heapSorter.sort(array);
		heapSorter.printResult(array);
	}

	@Test
	public void testShellSorter() {
		SortObject<Integer> shellSorter = new ShellSorter<Integer>();
		shellSorter.sort(array);
		shellSorter.printResult(array);
	}

	@Test
	public void testMergeSorter() {
		SortObject<String> mergeSorter = new MergeSorter<String>();
		mergeSorter.sort(strArray);
		mergeSorter.printResult(strArray);
	}
}
