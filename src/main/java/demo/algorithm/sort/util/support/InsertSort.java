package demo.algorithm.sort.util.support;

import demo.algorithm.sort.util.SortUtil;

/**
 * 插入排序:
 * 
 * @author treeroot
 * @since 2006-2-2
 * @version 1.0
 */
public class InsertSort implements SortUtil.Sort {
	public void sort(int[] data) {
		for (int i = 1; i < data.length; i++) {
			for (int j = i; (j > 0) && (data[j] < data[j - 1]); j--) {
				SortUtil.swap(data, j, j - 1);
			}
		}
	}
}
