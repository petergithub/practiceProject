package demo.algorithm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author Shang Pu
 * @version Date: Apr 17, 2012 2:14:41 PM
 */
// permutation: the act of changing the arrangement of a given number of
// elements
public class Permute {
	protected static final Logger logger = Logger.getLogger(Permute.class);

	/**
	 * 题目: 利用1、2、3、4这4个数字，打印出所有不同的序列 如12234，21234等，要求打印出来的不能有重复。
	 * <p>
	 * 解析: 此题难点在于如何消减重复的2.对于任意一个串利用递归
	 * 进行排列时，循环串中的每个字符到第一个字符进行递归。如果串中字符出现重复的话，则重复的字符中可以利用算法一次
	 * ,即只要与前面相同的字符循环到第一个字符时不调用递归就可以避免生重复。
	 */

	static int count = 0;

	public void testListAll() {
		// String[] array = new String[]{"1", "2", "2", "3", "4"};
		String[] array = new String[] { "15", "2", "2", "2" };
		permuteList("", Arrays.asList(array));
		// permuteListNotDuplicate("", Arrays.asList(array));
	}

	public void permuteListNotDuplicate(String begin, List<String> end) {
		if (end.isEmpty()) {
			logger.info(begin + "  ---  " + ++count);
		}
		Set<String> recordSet = new HashSet<String>();
		for (int i = 0; i < end.size(); i++) {
			List<String> endNew = new LinkedList<String>(end);
			if (!recordSet.contains(endNew.get(i))) {
				recordSet.add(endNew.get(i));
				permuteListNotDuplicate(begin + endNew.remove(i), endNew);
			}
		}
	}

	public static void permuteList(String begin, List<String> end) {
		if (end.isEmpty()) {
			logger.info(begin + "  ---  " + ++count);
		}
		for (int i = 0; i < end.size(); i++) {
			List<String> endingNew = new LinkedList<String>(end);
			permuteList(begin + endingNew.remove(i), endingNew);
		}
	}

	@org.junit.Test
	public void testPermuteString() {
		permuteString("", "1234");
	}

	public static void permuteString(String begin, String end) {
		if (end.length() == 0) {
			logger.info(begin + "  ---  " + ++count);
		}
		for (int i = 0; i < end.length(); i++) {
			String endNew = end.substring(0, i) + end.substring(i + 1);
			permuteString(begin + end.charAt(i), endNew);
		}
	}
}
