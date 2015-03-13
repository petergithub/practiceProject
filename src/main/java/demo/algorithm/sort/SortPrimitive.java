package demo.algorithm.sort;

import org.apache.log4j.Logger;
import org.pu.test.base.TestBase;

/**
 * http://zh.wikipedia.org/wiki/排序算法
 * 选择排序（Selection sort）、插入排序（Insertion sort）与气泡排序（Bubble
 * sort）这三个排序方式是初学排序所必须知道的三个基本排序方式
 * ，它们由于速度不快而不实用（平均与最快的时间复杂度都是O(n2)），然而它们排序的方式确是值得观察与探讨的。
 */
public class SortPrimitive extends TestBase {
	protected static final Logger logger = Logger
			.getLogger(SortPrimitive.class);

	@org.junit.Test
	public void testSort() {
		int[] array = { 92, 77, 67, 8, 6, 84, 55, 85, 43, 67 };
		logger.info("array before");
		printArray(array);
		 insertionSort(array);
		bubbleSort(array);
		logger.info("array after Sort()");
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
	public static void selectionSort(int[] number) {
		for (int i = 0; i < number.length - 1; i++) {
			int selectPos = i;
			for (int j = i + 1; j < number.length; j++)
				if (number[j] < number[selectPos]) selectPos = j;

			if (i != selectPos) swap(number, i, selectPos);
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
	public static void insertionSort(int[] number) {
		for (int i = 1; i < number.length; i++) {
			int valueToInsert = number[i];
			int holePos = i;
			for (; (holePos > 0) && (number[holePos - 1] > valueToInsert); holePos--) {
				number[holePos] = number[holePos - 1];
			}
			number[holePos] = valueToInsert;
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
	public static void bubbleSort(int[] number) {
		boolean flag = true;
		for (int i = 0; i < number.length - 1 && flag; i++) {
			flag = false;
			for (int j = 0; j < number.length - 1 - i; j++) {
				if (number[j] > number[j + 1]) {
					swap(number, j + 1, j);
					flag = true;
				}
			}
		}
	}

	private static void swap(int[] number, int i, int j) {
		int temp = number[i];
		number[i] = number[j];
		number[j] = temp;
	}
}
