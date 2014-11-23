package demo.algorithm.sort;

/**
 * @author Shang Pu
 * @version Date: Apr 17, 2012 11:54:51 AM
 */
public class InsertSorter<E extends Comparable<E>> extends SortObject<E> {

	public void sort(E[] array, int from, int len) {
		E tmp = null;
		for (int i = from + 1; i < from + len; i++) {
			tmp = array[i];
			int j = i;
			for (; j > from; j--) {
				if (tmp.compareTo(array[j - 1]) < 0) {
					array[j] = array[j - 1];
				} else
					break;
			}
			array[j] = tmp;
		}
	}
}
