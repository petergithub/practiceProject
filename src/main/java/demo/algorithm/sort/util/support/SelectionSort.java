package demo.algorithm.sort.util.support;
import demo.algorithm.sort.util.SortUtil;
/**
 * 选择排序
 * @author treeroot
 * @since 2006-2-2
 * @version 1.0
 */
public class SelectionSort implements SortUtil.Sort {
    public void sort(int[] data) {
        for (int i = 0; i < data.length; i++) {
            int lowIndex = i;
            for (int j = data.length - 1; j > i; j--) {
                if (data[j] < data[lowIndex]) {
                    lowIndex = j;
                }
            }
            SortUtil.swap(data,i,lowIndex);
        }
    }
}