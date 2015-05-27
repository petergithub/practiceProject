package demo.algorithm;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @version Date: Mar 19, 2015 11:33:31 AM
 * @author Shang Pu
 */
public class BinarySearch {

	private int[] arrayRotate = { 3, 4, 5, 1, 2 };

	/**
	 * <pre>
	 * 把一个数组最开始的若干个元素搬到数组的末尾，称之为数组的旋转，
	 * 输入一个递增排序的数组的一个旋转，在旋转数组中查找给定元素的下标。
	 * 例如数组{3,4,5,1,2}为{1,2,3,4,5}的一个旋转，元素1的下标为3，如果未找到则返回-1;
	 * </pre>
	 */
	@Test
	public void testBinarySearchRotate() {
		Assert.assertEquals(0, binarySearchRotate(arrayRotate, 3));
		Assert.assertEquals(1, binarySearchRotate(arrayRotate, 4));
		Assert.assertEquals(2, binarySearchRotate(arrayRotate, 5));
		Assert.assertEquals(3, binarySearchRotate(arrayRotate, 1));
		Assert.assertEquals(4, binarySearchRotate(arrayRotate, 2));
	}

	/**
	 * 给定一个有序的数组，查找某个数是否在数组中，请编程实现。
	 * 
	 * <pre>
	 *  编写二分查找的程序时
	 *     如果令 `left <= right，则right = middle - 1;
	 *     如果令left < right，则 right = middle;`
	 * 换言之，算法所操作的区间,是左闭右开区间,还是左闭右闭区间,这个区间,需要在循环初始化。
	 * 且在循环体是否终止的判断中,以及每次修改left, right区间值这三个地方保持一致,否则就可能出错。
	 * O(lnN)
	 * </pre>
	 * 
	 * @param array
	 * @param target
	 * @return
	 */
	public int binarySearch(int array[], int target) {
		int left = 0;
		int right = array.length - 1;
		// 如果这里是int right = n 的话，那么下面有两处地方需要修改，以保证一一对应：
		// 1、下面循环的条件则是while(left < right)
		// 2、循环内当 array[middle] > value 的时候，right = mid

		while (left <= right) // 循环条件，适时而变
		{
			int middle = left + ((right - left) >> 1); // 防止溢出，移位也更高效。同时，每次循环都需要更新。

			if (target < array[middle]) {
				right = middle - 1; // right赋值，适时而变
			} else if (array[middle] < target) {
				left = middle + 1;
			} else
				return middle;
			// 可能会有读者认为刚开始时就要判断相等，但毕竟数组中不相等的情况更多
			// 如果每次循环都判断一下是否相等，将耗费时间
		}
		return -1;
	}

	/**
	 * 给定一没有重复元素的旋转数组（它对应的原数组是有序的），求给定元素在旋转数组内的下标（不存在的返回-1）。
	 * 
	 * <pre>
	 * 看出中间位置两段起码有一个是有序的（不是左边，就是右边）， 那么就可以在有序的范围内使用二分查找；
	 * 如果不再有序范围内，就到另一半去找
	 * </pre>
	 * 
	 * @param array
	 * @param target
	 * @return
	 */
	public int binarySearchRotate(int[] array, int target) {
		int begin = 0;
		int end = array.length - 1;
		while (begin <= end) {
			int mid = begin + (end - begin) / 2;
			if (array[mid] == target) {
				return mid;
			}
			if (array[begin] <= array[mid]) {
				if (array[begin] <= target && target < array[mid]) {
					end = mid - 1;
				} else {
					begin = mid + 1;
				}
			} else {
				if (array[mid] <= target && target < array[end]) {
					end = mid - 1;
				} else {
					begin = mid + 1;
				}
			}
		}
		return -1;
	}

	/**
	 * 稍微扩展下，可以有重复的元素，其他的要求不变。
	 * 
	 * <pre>
	 * 大致思路与原来相同，这是需要比较A[beg] 与　A[mid]的关系
	 *     A[beg]  <　A[mid] ————左边有序
	 *     A[beg]  >　A[mid] ————右边有序
	 *     A[beg]  =　A[mid] ————++beg
	 * </pre>
	 * 
	 * @param array
	 * @param target
	 * @return
	 */
	public boolean search(int[] array, int target) {
		int beg = 0;
		int end = array.length - 1;
		while (beg <= end) {
			int mid = beg + (end - beg) / 2;
			if (array[mid] == target) return true;
			if (array[beg] < array[mid]) {
				if (array[beg] <= target && target < array[mid])
					end = mid - 1;
				else
					beg = mid + 1;
			} else if (array[beg] > array[mid]) {
				if (array[mid] < target && target <= array[end])
					beg = mid + 1;
				else
					end = mid - 1;
			} else
				++beg;
		}
		return false;
	}
}
