package demo.algorithm.sort.util.support;

import demo.algorithm.sort.util.SortUtil;

/**
 * Shell排序
 * 
 * @author treeroot
 * @since 2006-2-2
 * @version 1.0
 */
public class ShellSort implements SortUtil.Sort {
	public void sort(int[] data) {
		for (int i = data.length / 2; i > 2; i /= 2) {
			for (int j = 0; j < i; j++) {
				insertSort(data, j, i);
			}
		}
		insertSort(data, 0, 1);
	}

	private void insertSort(int[] data, int start, int inc) {
		for (int i = start + inc; i < data.length; i += inc) {
			for (int j = i; (j >= inc) && (data[j] < data[j - inc]); j -= inc) {
				SortUtil.swap(data, j, j - inc);
			}
		}
	}
}
