package demo.algorithm.optimization;

import java.util.Arrays;

/**
 * 贪心策略: 最短服务时间优先
 * <p>
 * 18.最优服务次序问题。 设有n个顾客同时等待一项服务。顾客i需要的服务时间为ti，
 * 1<=i<=n。应如何安排n个顾客的服务次序才能使平均等待时间达到最小? 平均等待时间是n个 顾客等待服务时间的总和除以n。
 * input: 正整数n，表示n个顾客。 接下来一行输入n个正整数，表示n个顾客需要的服务时间。 output: 最小的平均等待时间
 * 
 * @since jdk1.6
 * @author 毛正吉
 * @version 1.0
 * @date 2010.06.08
 */
public class OptimalService {

	public static void main(String[] args) {
		int count = 10;
		int[] intArray = { 56, 12, 1, 99, 1000, 234, 33, 55, 99, 812 };
		double sum = bestService(intArray, count);
		System.out.println(sum);
	}

	/**
	 * 计算最小的平均等待时间
	 * 
	 * @param intArray all the time needed
	 * @param count total count of customer
	 * @return
	 */
	public static double bestService(int[] intArray, int count) {
		double sum = 0.0;
		Arrays.sort(intArray); // 贪心策略: 最短服务时间优先

		for (int i = 1; i < count; i++) {
			intArray[i] += intArray[i - 1];
		}

		for (int i = 0; i < count; i++) {
			sum += intArray[i];
		}
		sum /= count; // 最小的平均等待时间
		return sum;
	}
}
