package demo.algorithm;

import org.apache.log4j.Logger;

/**
 * 在王晓东编著的《算法设计与实验题解》中看到的这个问题，问题描述如下: 
 * 一本书的页码从自然数1开始顺序编码直到自然数n。书的页码按照通常的习惯编排，
 * 每个页码都不含多余的前导数字0。例如第6页用6表示而不是06或006。
 * 数字统计问题要求对给定书的总页码，计算出书的全部页码中分别用到多少次数字0,1,2,3,.....9。
 * 
 * @author Shang Pu
 * @version Date: Apr 18, 2012 5:38:43 PM
 */
public class StatisticNumber {
	protected static final Logger logger = Logger
			.getLogger(StatisticNumber.class);
	int number = 21;

	public void testStatNumber1() {
		int count[] = new int[10];
		for (int i = 0; i < 10; i++) {
			count[i] = 0;
		}
		statNumber1(count, number);

		for (int i = 0; i < 10; i++) {
			logger.info("count[" + i + "] = " + count[i] + " in method main()");
		}
	}

	@org.junit.Test
	public void testStatNumber2() {
		int count[] = new int[10];
		for (int i = 0; i < 10; i++) {
			count[i] = 0;
		}
		statNumber2(count, number);

		for (int i = 0; i < 10; i++) {
			logger.info("count[" + i + "] = " + count[i] + " in method main()");
		}
	}

	public void testStatNumber3() {
		statNumber4(number);
	}

	/**
	 * 这个题目有个最容易想到的n*log10(n)的算法。这是自己写的复杂度为O(n*log10(n))的代码: 
	 */
	public void statNumber1(int[] count, int num) {
		int i, t;
		for (i = 1; i <= num; i++) {
			t = i;
			while (t > 0) {
				count[t % 10]++;
				t /= 10;
			}
		}
	}

	/**
	 * 计算0出现个数，先补0，就可以同其他数字同样计算，然后去掉补 0 个数
	 */
	void statNumber2(int[] count, int num) {
		int remainder, k;
		int s = 0;
		int pown = 1; //10^n
		for (k = 0; num > 0; k++, num /= 10, pown *= 10) {
			remainder = num % 10;
			// 先补0
			// 统计从个位算起前k位 0 ~ 9 个数
			for (int i = 0; i < 10; i++)
				count[i] += remainder * k * pown / 10;

			// 统计第k+1位出现 0 ~ (c-1) 个数
			for (int i = 0; i < remainder; i++)
				count[i] += pown;

			// 统计第k+1位出现 c 个数
			count[remainder] += 1 + s;

			// 去掉第k+1位补 0 个数
			count[0] -= pown;
			s += remainder * pown;
		}
	}

	/**
	 * 仔细考虑m个n位十进制数的特点，在一个n位十进制数的由低到高的第i个数位上，总是连续出现10^i个0，然后是10^i个1……一直到10^i个9，9
	 * 之后又是连续的10^i个0，这样循环出现。找到这个规律，就可以在常数时间内算出第i个数位上每个数字出现的次数。而在第i个数位上，
	 * 最前面的10^i个0是前导0，应该把它们减掉。这样，可以只分析给定的输入整数n的每个数位，从面可以得到一个log10(n)的算法，代码如下: 
	 * http://blog.csdn.net/jcwKyl/article/details/3009244
	 */
	void statNumber3(int n) {
		int m, i, j, t, x;
		int len = String.valueOf(n).length();
		char charArray[] = new char[16];
		int count[] = new int[10];
		count[0] = 0;
		int pow10[] = new int[12];
		pow10[0] = 1;
		for (i = 1; i < 12; i++) {
			pow10[i] = pow10[i - 1] * 10;
		}
// System.out.printf("%c", charArray.toString());
		m = n + 1;
		for (i = 0; i <= len; i++) {
			x = charArray[i] - '0';
			t = (m - 1) / pow10[len - i];

			count[x] += m - t * pow10[len - i];

			t /= 10;
			j = 0;
			while (j <= x - 1) {
				count[j] += (t + 1) * pow10[len - i];
				j++;
			}
			while (j < 10) {
				count[j] += t * pow10[len - i];
				j++;
			}
			count[0] -= pow10[len - i]; /* 第i个数位上前10^i个0是无意义的 */
		}
		for (j = 0; j < 10; j++) {
			System.out.printf("%d/n", count[j]);
		}
	}

	void statNumber4(int n) {
		int m, i, j, t, x;
		int len = String.valueOf(n).length();
		int charArray[] = new int[16];
		int count[] = new int[10];
		count[0] = 0;
		int pow10[] = new int[12];
		pow10[0] = 1;
		for (i = 1; i < 12; i++) {
			pow10[i] = pow10[i - 1] * 10;
		}

		for (i = 1; i < 12; i++) {
			pow10[i] = pow10[i - 1] * 10;
		}
// System.out.printf(charArray, "%d", n);
		m = n + 1;
		for (i = 0; i <= len; i++) {
			x = charArray[i];
			t = (m - 1) / pow10[len - i];

			count[x] += m - t * pow10[len - i];

			t /= 10;
			j = 0;
			while (j <= x - 1) {
				count[j] += (t + 1) * pow10[len - i];
				j++;
			}
			while (j < 10) {
				count[j] += t * pow10[len - i];
				j++;
			}
			count[0] -= pow10[len - i]; /* 第i个数位上前10^i个0是无意义的 */
		}
		for (j = 0; j < 10; j++) {
			System.out.printf("%d/n", count[j]);
		}
	}
}
