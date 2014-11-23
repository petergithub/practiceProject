package demo.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * @author Shang Pu
 */
public class TestAlgorithm {

	/**
	 * C:\\Shang\\Dropbox\\Base\\DctmTools\\doc\\
	 * C:\Shang\Dropbox\Base\DctmTools\doc\
	 */
	String path = getClass().getResource("/").getPath();
	protected static final Logger logger = Logger
			.getLogger(TestAlgorithm.class);

	public void testSplitStringByChar() {
		String s = "test中dd文dsaf中男大3443n中国";
		splitStringByChar(s, 7);
	}

	/**
	 * check the int if it is a power of 2
	 * <p>
	 * 如果一个数字是2的阶次方数, 那么它的二进制数的 首位一般是1, 后面接若干个0.如果8是1000, 64是1000000 如果将这个数减1后,
	 * 再与该数做与（&）运算, 刚应该全为0 （8与7, 一个二进制数是1000, 一个二进制数是111, 它们做
	 * 和运算后全为0）。所以((d-1)&(d))==0
	 */

	public void testPowerOf2() {
		int d = 4;
		if (((d - 1) & (d)) == 0 && (d != 0))
			System.out.println(d + "是2的阶次");
		else
			System.out.println(d + "不是2的阶次");
	}

	/**
	 * 截取中文: 一个截取字符串的函数, 输入: 一个字符串和字节数, 输出: 按字节截取的字符串。 但是要保证汉字不被截成半个。
	 * <p>
	 * string.substring()是按char计算的, 而string.getbytes()是按byte计算的
	 */
	public static void splitStringByChar(String str, int bytes) {
		int loopCount;
		loopCount = (str.length() % bytes == 0) ? (str.length() / bytes) : (str
				.length() / bytes + 1);
		System.out.println("Willl Split into " + loopCount);
		for (int i = 1; i <= loopCount; i++) {
			if (i == loopCount) {
				System.out
						.println(str.substring((i - 1) * bytes, str.length()));
			} else {
				System.out.println(str.substring((i - 1) * bytes, (i * bytes)));
			}
		}
	}

	/**
	 * Exchange int Value Without a third variable
	 */
	public void testExchangeInt(int x, int y) {
		x = x + y;
		y = x - y;
		x = x - y;
	}

	@org.junit.Test
	public void testGetMaxAndMost() {
		int[] iArray = { 1, 3, 4, 7, 2, 1, 1, 5, 2 };
		// int[] iArray = { 5, 5, 8, 5, 3, 5, 3, 3, 3, 1, 1, 1 };
		String result = getMaxAndMost2(iArray);
		logger.info(result);
	}

	/**
	 * 给你一组字符如{1, 3, 4, 7, 2, 1, 1, 5, 2}, 让你输出里面出现次数最多且数值最大的一个, 出现几次
	 */
	public String getMaxAndMost2(int[] iArray) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int times = 0;
		int max = -1;
		for (int i = 0; i < iArray.length; i++) {
			if (map.containsKey(iArray[i])) {
				// 如果已经有key--num[1],那么这个put,就会把value值+1覆盖原来的value值
				map.put(iArray[i], map.get(iArray[i]) + 1);
				Integer value = map.get(iArray[i]);
				// if 次数最多,或者次数相等数值最大
				if (value > times || (value == times && iArray[i] > max)) {
					times = value;
					max = iArray[i];
				}
			} else {
				map.put(iArray[i], 1);
			}
		}
		return "The num is: " + max + " And the times is: " + times;
	}

	public void testCountChar() {
		String string = "aabbbbbccddddd";
		countChar(string);
	}

	/**
	 * 统计最多的字符
	 */
	public static void countChar(String string) {
		char[] chars = string.toCharArray();
		ArrayList<String> lists = new ArrayList<String>();
		TreeSet<String> set = new TreeSet<String>();

		for (int i = 0; i < chars.length; i++) {
			lists.add(String.valueOf(chars[i]));
			set.add(String.valueOf(chars[i]));
		}
		System.out.println(set);
		Collections.sort(lists);
		System.out.println(lists);

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < lists.size(); i++) {
			sb.append(lists.get(i));
		}

		string = sb.toString();
		System.out.println(string);
		int max = 0;
		String maxString = "";
		ArrayList<String> maxlist = new ArrayList<String>();

		Iterator<String> its = set.iterator();
		while (its.hasNext()) {
			String os = its.next();
			int begin = string.indexOf(os);
			int end = string.lastIndexOf(os);
			int value = end - begin + 1;
			if (value > max) {
				max = value;
				maxString = os;
				maxlist.add(os);
			} else if (value == max) {
				maxlist.add(os);
			}
		}

		int index = 0;
		for (int i = 0; i < maxlist.size(); i++) {
			if (maxlist.get(i).equals(maxString)) {
				index = i;
				break;
			}
		}
		logger.info("index = " + index + " in method countChar()");

		System.out.println("出现最多的字符是: ");
		for (int i = 0; i < maxlist.size(); i++) {
			System.out.println(maxlist.get(i) + " ");
		}
		System.out.println();
		System.out.println("出现最多的次数是: " + max);
	}

	// 找出重复次数最多的字符, 并打印出数量和字符
	public void testGetMaxRepeatingChar() {
		String a = "aaabbdcfeeswdfttrrccaa";
		List<Object> ls = new LinkedList<Object>();
		for (int i = 0; i < a.length(); i++) {
			if (!isHave(ls, (a.charAt(i) + ""))) {
				ls.add(a.charAt(i) + "");
			}
		}
		// 保存相应的位置的字母的个数
		int[] ret = new int[ls.size()];
		// 最大数
		int max = 0;
		for (int i = 0; i < ls.size(); i++) {
			ret[i] = getNum(ls.get(i).toString(), a);
			if (ret[i] > max) {
				max = ret[i];
			}
		}
		// 检查个数最大的, 返回位置列表
		List<Object> ls2 = new LinkedList<Object>();
		for (int i = 0; i < ret.length; i++) {
			if (ret[i] == max) {
				ls2.add(i + "");
			}
		}

		// 遍历ls2, 打印出相应字母和个数
		logger.info("\n打印出个数最多的字母及个数: ");
		for (int i = 0; i < ls2.size(); i++) {
			int m = Integer.parseInt(ls2.get(i).toString());
			System.out
					.println("字母: " + ls.get(i).toString() + "   个数: " + ret[m]);
		}

		logger.info("\n打印出所有字母及个数: ");
		for (int i = 0; i < ret.length; i++) {
			System.out
					.println("字母: " + ls.get(i).toString() + "   个数: " + ret[i]);
		}
	}

	// 查询该列表 ls 是否有该字符
	private boolean isHave(List<Object> ls, String k) {
		boolean flag = false;
		for (int i = 0; i < ls.size(); i++) {
			if (ls.get(i).toString().equals(k)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	// 查询 a 中有几个 k
	private int getNum(String k, String a) {
		int j = 0;
		for (int i = 0; i < a.length(); i++) {
			if ((a.charAt(i) + "").equals(k)) {
				j++;
			}
		}
		return j;
	}

	public void testMax() {
		String str = "adadfdfseffserfefsefseetsdg";
		replace(str);
		statistics(str);
	}

	private void statistics(String str) {
		char[] charArray = str.toCharArray();
		Arrays.sort(charArray);
		char tempchar = charArray[0];
		int templength = 1;
		char maxchar = '\0';
		int maxlength = 0;
		for (int i = 1; i < charArray.length; i++) {
			if (tempchar == charArray[i]) {
				templength++;
			} else {
				if (templength > maxlength) {
					maxchar = tempchar;
					maxlength = templength;
				}
				tempchar = charArray[i];
				templength = 1;
			}
		}
		if (templength > maxlength) {// 对数组的最后相同的字符串进行判断
			maxlength = templength;
			maxchar = charArray[charArray.length - 1];
		}

		logger.info(String.valueOf(charArray));
		logger.info("the max length char is: " + maxchar
				+ "   and the length is: " + maxlength);
	}

	private void replace(String sample) {
		String[] chars = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
				"w", "x", "y", "z" };
		String mChar = "";
		int mCount = 0;
		for (int i = 0; i < chars.length; i++) {
			String[] temp = sample.split(chars[i]);
			if (temp.length > mCount) {
				mChar = chars[i];
				mCount = temp.length - 1;
			}
		}
		logger.info("The   String   you   input   was:" + sample);
		logger.info("The   most   favorite   char   is:" + mChar
				+ ",It   got   " + mCount + "   showtimes!");
	}

	public void test6() {
		int[] x = { 2, 3, -8, 7, 9 };
		int max = x[0];
		for (int i = 1; i < x.length; i++) {
			if (x[i] > max) max = x[i];
		}
		logger.info(max);

	}

	// int i = 0;
	// i++;

	// 0: iconst_0
	// 1: istore_1
	// 2: iinc 1, 1

	// int i = 0;
	// i = i++;

	// 0: iconst_0
	// 1: istore_1
	// 2: iload_1
	// 3: iinc 1, 1
	// 6: istore_1
	public void testSelfAdd() {
		int i = 0;
		i = i++;
		logger.info("i = " + i + " in method testSelfAdd()");
	}
}
