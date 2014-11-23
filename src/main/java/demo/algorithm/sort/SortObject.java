package demo.algorithm.sort;

/**
 * 通用抽象类
 * 
 * @author Shang Pu
 * @version Date: Apr 17, 2012 11:54:38 AM
 */
public abstract class SortObject<E> {

	/**
	 * @param array
	 * @param from 起始位置
	 * @param len 从起始位置开始 需要比较的个数
	 */
	public abstract void sort(E[] array, int from, int len);

	public final void sort(E[] array) {
		sort(array, 0, array.length);
	}

	protected final void swap(E[] array, int from, int to) {
		E tmp = array[from];
		array[from] = array[to];
		array[to] = tmp;
	}

	public void printResult(Comparable<?>[] array) {
		int length = array.length;
		for (int i = 0; i < length; i++) {
			// logger.info(i);
			if (i == length - 1) {
				System.out.print(array[i]);
			} else {
				System.out.print(array[i] + ", ");
			}
		}
		System.out.println();
	}

}
