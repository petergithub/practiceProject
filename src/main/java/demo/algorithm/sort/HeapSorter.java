package demo.algorithm.sort;

/**
 * 堆排序:堆是一种完全二叉树，一般使用数组来实现。 堆主要有两种核心操作， 1）从指定节点向上调整(shiftUp)
 * 2）从指定节点向下调整(shiftDown) 建堆，以及删除堆定节点使用shiftDwon,而在插入节点时一般结合两种操作一起使用。
 * 堆排序借助最大值堆来实现，第i次从堆顶移除最大值放到数组的倒数第i个位置， 然后shiftDown到倒数第i+1个位置,一共执行N次调整，即完成排序。
 * 显然，堆排序也是一种选择性的排序，每次选择第i大的元素。
 * 
 * @author Daniel Cheng
 */
public class HeapSorter<E extends Comparable<E>> extends SortObject<E> {

	public void sort(E[] array, int from, int len) {
		build_heap(array, from, len);
		for (int i = 0; i < len; i++) {
			// 第i次从堆顶移除最大值放到数组的倒数第i个位置，
			swap(array, from, from + len - 1 - i);
			// 一直shiftDown（从0开始）到倒数第i+1个位置,一共执行N次调整
			shift_down(array, from, len - 1 - i, 0);
		}
	}

	private final void build_heap(E[] array, int from, int len) {
		// 我们从（len- 1）/ 2开始，因为分支节点+1=叶子节点，而所有的叶子节点已经是一个堆
		int pos = (len - 1) / 2;
		for (int i = pos; i >= 0; i--) {
			shift_down(array, from, len, i);
		}

	}

	private final void shift_down(E[] array, int from, int len, int pos) {
		E tmp = array[from + pos];
		int index = pos * 2 + 1;// 用左孩子结点
		while (index < len)// 直到没有孩子结点
		{
			if (index + 1 < len
					&& array[from + index].compareTo(array[from + index + 1]) < 0)// 右孩子结点是较大的
			{
				index += 1;// 切换到右孩子结点
			}
			if (tmp.compareTo(array[from + index]) < 0) {
				array[from + pos] = array[from + index];
				pos = index;
				index = pos * 2 + 1;

			} else {
				break;
			}
		}
		array[from + pos] = tmp;
	}

}
