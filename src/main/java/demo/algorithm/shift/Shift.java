package demo.algorithm.shift;

/**
 * @author Shang Pu
 * @version Date: Apr 16, 2012 5:54:55 PM
 */
/**
 * 数组循环右移K位
 * 设计一个算法，把一个含有N个元素的数组循环右移K位，要求时间复杂度为O（N），且只允许使用两个附加变量。
 * 不合题意的解法如下: 
 * 我们先试验简单的办法，可以每次将数组中的元素右移一位，循环K次。abcd1234→4abcd123→34abcd12→234abcd1→1234abcd。
 * 伪代码如下: 
 * 
 * <pre>
 * RightShift(int* arr, int N, int K)
 * {
 *     while(K--)
 *     {
 *         int t = arr[N - 1];
 *         for(int i = N - 1; i > 0; i --)
 *             arr[i] = arr[i - 1];
 *         arr[0] = t;
 *     }
 * }
 * </pre>
 * 
 * 虽然这个算法可以实现数组的循环右移，但是算法复杂度为O（K * N），不符合题目的要求，需要继续往下探索。
 * 分析与解法
 * 假如数组为abcd1234，循环右移4位的话，我们希望到达的状态是1234abcd。不妨设K是一个非负的整数，当K为负整数的时候，右移K位，相当于左移（-
 * K）位。左移和右移在本质上是一样的。
 * 【解法一】
 * 大家开始可能会有这样的潜在假设，K<N。事实上，很多时候也的确是这样的。但严格地说，我们不能用这样的“惯性思维”来思考问题。尤其在编程的时候，
 * 全面地考虑问题是很重要的，K可能是一个远大于N的整数，在这个时候，上面的解法是需要改进的。
 * 仔细观察循环右移的特点，不难发现: 每个元素右移N位后都会回到自己的位置上。因此，如果K >
 * N，右移K-N之后的数组序列跟右移K位的结果是一样的。进而可得出一条通用的规律: 右移K位之后的情形，跟右移K’= K % N位之后的情形一样。
 * 
 * <pre>
 * RightShift(int* arr, int N, int K)
 * {
 *     K %= N;
 *     while(K--)
 *     {
 *         int t = arr[N - 1];
 *         for(int i = N - 1; i > 0; i --)
 *             arr[i] = arr[i - 1];
 *         arr[0] = t;
 *     }
 * }
 * </pre>
 * 
 * 可见，增加考虑循环右移的特点之后，算法复杂度降为O（N２），这跟K无关，与题目的要求又接近了一步。但时间复杂度还不够低，接下来让我们继续挖掘循环右移前后，
 * 数组之间的关联。
 * 【解法二】
 * 假设原数组序列为abcd1234，要求变换成的数组序列为1234abcd，即循环右移了4位。比较之后，不难看出，其中有两段的顺序是不变的: 1234
 * 和abcd，可把这两段看成两个整体。右移K位的过程就是把数组的两部分交换一下。变换的过程通过以下步骤完成: 
 * 1. 逆序排列abcd: abcd1234 → dcba1234；
 * 2. 逆序排列1234: dcba1234 → dcba4321；
 * 3. 全部逆序: dcba4321 → 1234abcd。
 * 
 * <pre>
 * Reverse(int* arr, int b, int e)
 * {
 *     for(; b < e; b++, e--)
 *     {
 *         int temp = arr[e];
 *         arr[e] = arr[b];
 *         arr[b] = temp;
 *     }
 * }
 * RightShift(int* arr, int N, int k)
 * {
 *     K %= N;
 *     Reverse(arr, 0, N – K - 1);
 *     Reverse(arr, N - K, N - 1);
 *     Reverse(arr, 0, N - 1);
 * }
 * </pre>
 * 
 * 这样，我们就可以在线性时间内实现右移操作了。
 */
public class Shift {

	int gcd(int m, int n) {
		int r;
		do {
			r = m % n;
			m = n;
			n = r;
		} while (r > 0);
		return n;
	}

	int[] shiftArray(int A[], int n, int k) {
		// 因为左移的代码比右移的代码好实现的多，而右移k位
		// 等价于左移-k位，-k = n - k。以下代码是通过左移-k位来实现右移k位
		k = n - (k % n);
		for (int i = 0, cnt = gcd(n, k); i < cnt; i++) {
			int t = A[i], p = i, j = (k + i) % n;
			while (j != i) {
				A[p] = A[j];
				p = j;
				j = (k + p) % n;
			}
			A[p] = t;
		}
		return A;
	}

	void printArray(int A[], int n) {
		for (int i = 0; i < n; i++) {
			System.out.printf("%-3d", A[i]);
			if ((i + 1) % 10 == 0) System.out.printf("/n");
		}
	}

	@org.junit.Test
	public void testShift() {
		int A[] = { 1, 2, 3, 4, 5, 6, 7 };
		A = shiftArray(A, 7, 1);
		printArray(A, 7);
	}
}
