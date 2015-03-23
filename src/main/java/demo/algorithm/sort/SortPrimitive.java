package demo.algorithm.sort;

import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://zh.wikipedia.org/wiki/排序算法
 * 选择排序（Selection sort）、插入排序（Insertion sort）与气泡排序（Bubble
 * sort）这三个排序方式是初学排序所必须知道的三个基本排序方式
 * ，它们由于速度不快而不实用（平均与最快的时间复杂度都是O(n2)），然而它们排序的方式确是值得观察与探讨的。
 */
public class SortPrimitive extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(SortPrimitive.class);


	@Test
	public void testSort() {
		int[] array = { 92, 77, 67, 8, 6, 84, 55, 85, 43, 67 };
		log.info("array before");
		printArray(array);
//		insertionSort(array);
//		bubbleSort(array);
		quickSort(array);
		log.info("array after Sort()");
		printArray(array);
	}

	/**
	 * <pre>
	 * 选择排序
	 * 	将要排序的对象分作两部份，一个是已排序的，一个是未排序的，
	 * 从后端未排序部份选择一个最小值，并放入前端已排序部份的最后一个，例如: 
	 * 	排序前: 70 80 31 37 10 1 48 60 33 80
	 * 	
	 * 	1. [1] 80 31 37 10 70 48 60 33 80 选出最小值1
	 * 	2. [1 10] 31 37 80 70 48 60 33 80 选出最小值10
	 * 	3. [1 10 31] 37 80 70 48 60 33 80 选出最小值31
	 * 	4. [1 10 31 33] 80 70 48 60 37 80 ......
	 * 	5. [1 10 31 33 37] 70 48 60 80 80 ......
	 * 	6. [1 10 31 33 37 48] 70 60 80 80 ......
	 * 	7. [1 10 31 33 37 48 60] 70 80 80 ......
	 * 	8. [1 10 31 33 37 48 60 70] 80 80 ......
	 * 	9. [1 10 31 33 37 48 60 70 80] 80 ......
	 * </pre>
	 */
	public static void selectionSort(int[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			int selectPos = i;
			for (int j = i + 1; j < array.length; j++)
				if (array[j] < array[selectPos]) selectPos = j;

			if (i != selectPos) swap(array, i, selectPos);
		}
	}

	/**
	 * <pre>
	 * 插入排序
	 * 像是玩朴克一样，我们将牌分作两堆，每次从后面一堆的牌抽出最前端的牌，
	 * 然后插入前面一堆牌的最小位置.
	 * http://zh.wikipedia.org/wiki/插入排序
	 * 一般来说，插入排序都采用in-place在数组上实现。具体算法描述如下：
	 * 1.	从第一个元素开始，该元素可以认为已经被排序
	 * 2.	取出下一个元素，在已经排序的元素序列中从后向前扫描
	 * 3.	如果该元素（已排序）大于新元素，将该元素移到下一位置
	 * 4.	重复步骤3，直到找到已排序的元素小于或者等于新元素的位置
	 * 5.	将新元素插入到该位置后
	 * 6.	重复步骤2~5
	 * 如果比较操作的代价比交换操作大的话，可以采用二分查找法来减少比较操作的数目。
	 * 该算法可以认为是插入排序的一个变种，称为二分查找插入排序。
	 * 例如: 
	 * 	排序前: 92 77 67 8 6 84 55 85 43 67
	 * 	1. [77 92] 67 8 6 84 55 85 43 67 将77插入92前
	 * 	2. [67 77 92] 8 6 84 55 85 43 67 将67插入77前
	 * 	3. [8 67 77 92] 6 84 55 85 43 67 将8插入67前
	 * 	4. [6 8 67 77 92] 84 55 85 43 67 将6插入8前
	 * 	5. [6 8 67 77 84 92] 55 85 43 67 将84插入92前
	 * 	6. [6 8 55 67 77 84 92] 85 43 67 将55插入67前
	 * 	7. [6 8 55 67 77 84 85 92] 43 67 ......
	 * 	8. [6 8 43 55 67 77 84 85 92] 67 ......
	 * 	9. [6 8 43 55 67 67 77 84 85 92] ......
	 * </pre>
	 */
	public static void insertionSort(int[] array) {
		for (int i = 1; i < array.length; i++) {
			int valueToInsert = array[i];
			int holePos = i;
			for (; (holePos > 0) && (array[holePos - 1] > valueToInsert); holePos--) {
				array[holePos] = array[holePos - 1];
			}
			array[holePos] = valueToInsert;
		}
	}

	/**
	 * <pre>
	 * 气泡排序法
	 * 就是排序时，最大的元素会如同气泡一样移至右端，
	 * 其利用比较相邻元素的方法，将大的元素交换至右端，所以大的元素会不断的往右移动，
	 * 直到适当的位置为止。 基本的气泡排序法可以利用旗标的方式稍微减少一些比较的时间，
	 * 当寻访完阵列后都没有发生任何的交换动作，表示排序已经完成，
	 * 而无需再进行之后的回圈比较与交换动作，例如: 
	 * 	 排序前: 95 27 90 49 80 58 6 9 18 50
	 * 	
	 * 	 1. 27 90 49 80 58 6 9 18 50 [95] 95浮出
	 * 	 2. 27 49 80 58 6 9 18 50 [90 95] 90浮出
	 * 	 3. 27 49 58 6 9 18 50 [80 90 95] 80浮出
	 * 	 4. 27 49 6 9 18 50 [58 80 90 95] ......
	 * 	 5. 27 6 9 18 49 [50 58 80 90 95] ......
	 * 	 6. 6 9 18 27 [49 50 58 80 90 95] ......
	 * 	 7. 6 9 18 [27 49 50 58 80 90 95] 由于接下来不会再发生交换动作，排序提早结束
	 * 	
	 * 	 在上面的例子当中，还加入了一个观念，就是当进行至i与i+1时没有交换的动作，
	 * 表示接下来的i+2至n已经排序完毕，这也增进了气泡排序的效率。
	 * </pre>
	 */
	public static void bubbleSort(int[] array) {
		boolean flag = true;
		for (int i = 0; i < array.length - 1 && flag; i++) {
			flag = false;
			for (int j = 0; j < array.length - 1 - i; j++) {
				if (array[j] > array[j + 1]) {
					swap(array, j + 1, j);
					flag = true;
				}
			}
		}
	}
	
	/**
	 * <pre>
	 * 伪代码，此算法可以被表示为：
	 *  function quicksort(q)
	 *      var list less, pivotList, greater
	 *      if length(q) ≤ 1 {
	 *          return q
	 *      } else {
	 *          select a pivot value pivot from q
	 *          for each x in q except the pivot element
	 *              if x < pivot then add x to less
	 *              if x ≥ pivot then add x to greater
	 *          add pivot to pivotList
	 *          return concatenate(quicksort(less), pivotList, quicksort(greater))
	 *      }
	 * </pre>
	 * 
	 * @param array
	 */
	public static void quickSort(int[] array) {
		qSort(array, 0, array.length-1);
	}

	private static void qSort(int[] array, int left, int right) {
		log.debug("Enter qSort({},{})", left, right);
		if (left < right) {
			int index = partition2(array, left, right);
			qSort(array, left, index-1);
			qSort(array, index+1, right);
		}
	}

	/**
	 * http://blog.csdn.net/morewindows/article/details/6684558
	 */
	private static int partition2(int[] array, int left, int right) {
		int index = left;//以左边的数为基准数， 如果以中间的数作为基准数，
		// 可以将中间的数和第一个数进行交换 就转化为以左边为基准的情况了。如下面注释
		// index = left + new Random().nextInt(right-left+1);
		// swap(array, index, left);
		// index = left;
		int key = array[index];// array[index]就是第一个坑
		while (left < right) {
			while (left < right && key <= array[right]) {// 从右向左找小于key的数来填array[index]
				right--;
			}
			array[left] = array[right];// 将array[right]填到array[left]中，array[right]就形成了一个新的坑

			// 从左向右找大于或等于key的数来填array[right]
			while (left < right && array[left] <= key) {
				left++;
			}
			array[right] = array[left];// 将array[left]填到array[right]中，array[left]就形成了一个新的坑
		}
		 //退出时，left等于right。将key填到这个坑中。  
		array[left] = key;
		return left;
	}

	/**
	 * http://zh.wikipedia.org/wiki/快速排序#Java
	 */
	private static int partition(int[] array, int left, int right) {
		int index = left;
//		 index = left + new Random().nextInt(right-left+1);
		int pivot = array[index];
//		swap(array, index, right);
		for (int i = index = left; i < right; ++i) {
			if (array[i] <= pivot) {
				swap(array, index++, i);
			}
		}
		swap(array, index, right);
		return index;
	}

	private static void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}
