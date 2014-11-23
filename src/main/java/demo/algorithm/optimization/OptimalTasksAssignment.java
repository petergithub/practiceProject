package demo.algorithm.optimization;

/**
 * 21.工作分配问题。 设有n件工作分配给n个人。将工作i分配给第j个人所需的费用为cij。
 * 试设计一个算法，为每一个人都分配一件不同的工作， 并使总费用达到最小。
 * input: 第一行有1个正整数n(1<=n<=20)。接下来的n行每行n个数，表示工作费用。
 * output: 输出最小的总费用。
 * ACM中的工作分配问题是一个典型的回溯问题，利用回溯思想能很准确地得到问题的解
 * <p>
 * 解题思路:  由于每个人都必须分配到工作，在这里可以建一个二维数组c[i][j]，用以表示
 * i号工人完成j号工作所需的费用。给定一个循环，从第1个工人开始循环分配工作，直到所有
 * 工人都分配到。为第i个工人分配工作时，再循环检查每个工作是否已被分配，没有则分配给i号工人， 否则
 * 检查下一个工作。可以用一个一维数组x[j]来表示第j号工作是否被分配，未分配则x[j]=0，
 * 否则x[j]=1。利用回溯思想，在工人循环结束后回到上一工人，取消此次分配的工作，而去
 * 分配下一工作直到可以分配为止。这样，一直回溯到第1个工人后，就能得到所有的可行解。
 * 在检查工作分配时，其实就是判断取得可行解时的二维数组的一下标都不相同，二下标同样不相同。
 * <p>
 * Assignment problem
 * <p>
 * There are a number of agents and a number of tasks. Any agent can be assigned
 * to perform any task, incurring some cost that may vary depending on the
 * agent-task assignment. It is required to perform all tasks by assigning
 * exactly one agent to each task in such a way that the total cost of the
 * assignment is minimized.
 * <p>
 * 匈牙利算法是众多用于解决线性任务分配问题的算法之一，它可以在多项式时间内解决问题。
 * 分配问题是运输问题的特例，运输问题是最少成本流量问题的特例，而它们都是线性规划的特例
 * 。因此，单纯形法可以作为解决这些问题的通法。然而，针对每种特殊情形设计的专门算法可以提高解决问题的效率
 * 。如果问题的成本函数包含二次不等式，则称之为二次分配问题。
 * <p>
 * 任务分配問題一般可以在多項式時間內轉化成最大流量問題（Maximum Flow Problem）。
 * <p>
 * 
 * <pre>
 * 样例分析: 
 *     给定3件工作，i号工人完成j号工作的费用如下: 
 * 10 2 3
 * 2 3 4
 * 3 4 5
 *     假定一个变量count表示工作费用总和，初始为0，变量i表示第i号工人，初始为1。
 * n表示总的工作量，这里是取3。c[i][j]表示i号工人完成j号工作的费用，x[j]表示j号
 * 工作是否被分配。算法如下:  C代码 
 * void work(int i,int count){
 *   if(i>n)
 *     return;
 *   for(int j=1;j<=n;j++)
 *     if(x[j] == 0){
 *       x[j] = 1;
 *       work(i+1,count+c[i][j]);
 *       x[j] = 0;
 *     }
 * }
 * 那么在这里，用回溯法的思想就是，首先分配的工作是: 
 * 10:c[1][1]  3:c[2][2]  5:c[3][3]  count=18;
 *     此时，所有工人分配结束，然后回溯到第2个工人重新分配: 
 * 10:c[1][1]  4:c[2][3]  4:c[3][2]  count=18;
 *     第2个工人已经回溯到n，再回溯到第1个工人重新分配: 
 * 2:c[1][2]  2:c[2][1]  5:c[3][3]  count=9;
 *     回溯到第2个工人，重新分配: 
 * 2:c[1][2]  4:c[2][3]  3:c[3][1]  count=9;
 *     再次回溯到第1个工人，重新分配: 
 * 3:c[1][3]  2:c[2][1]  4:c[3][2]  count=9;
 *     回溯到第2个工人，重新分配: 
 * 3:c[1][3]  3:c[2][2]  3:c[3][1]  count=9;
 * 
 * 这样，就得到了所有的可行解。而我们是要得到最少的费用，即可行解中和最小的一个，
 * 故需要再定义一个全局变量cost表示最终的总费用，初始cost为 c[i][i]之和，
 * 即对角线费用相加。在所有工人分配完工作时，比较count与cost的大小，如果count小于cost,
 * 证明在回溯时找到了一个最优解，此时就把count赋给cost。
 *     到这里，整个算法差不多也快结束了，已经能得到最终结果了。但考虑到算法的复杂度，
 *     这里还有一个剪枝优化的工作可以做。就是在每次计算局部费用变量count的值时，
 *     如果判断count已经大于cost，就没必要再往下分配了，因为这时得到的解必然不是最优解。
 * </pre>
 * 
 * refer to http://maozj.iteye.com/blog/685628 and
 * http://www.iteye.com/topic/846130 http://zh.wikipedia.org/wiki/任务分配问题
 * 
 * @author 毛正吉
 */
public class OptimalTasksAssignment {
	/**
	 * n个工作(tasks)
	 */
	private int countOfTasks;
	/**
	 * 设有n件工作分配给n个人(agents)。将工作i分配给第j个人所需的费用为costOfAgentTask[i,j]
	 */
	private int[][] costOfAgentTask;
	/**
	 * n个工作的排列解空间
	 */
	private int[] assign;
	/**
	 * 最优解空间
	 */
	private int[] bestAssign;
	/**
	 * 最优解
	 */
	private int minimizedTotalCost;

	public static void main(String[] args) {
		int countOfTasks = 3;
		int[][] costOfAgentTask = { { 0, 0, 0, 0 }, { 0, 10, 2, 3 },
				{ 0, 2, 3, 4 }, { 0, 3, 4, 5 } };
		// 一个测试案例
		OptimalTasksAssignment taskAssign = new OptimalTasksAssignment(
				countOfTasks, costOfAgentTask);
		taskAssign.assignTask(0);
		int[] bestAssign = taskAssign.getBestAssign();
		int minimizedTotalCost = taskAssign.getMinimizedTotalCost();

		// 输出
		for (int i = 1; i <= countOfTasks; i++) {
			System.out.print(bestAssign[i] + " ");
		}
		System.out.println("\n" + minimizedTotalCost);
	}

	/**
	 * 构造方法
	 */
	public OptimalTasksAssignment(int countOfTasks, int[][] costOfAgentTask) {
		this.countOfTasks = countOfTasks;
		this.costOfAgentTask = costOfAgentTask;
		assign = new int[countOfTasks + 1];
		bestAssign = new int[countOfTasks + 1];
		minimizedTotalCost = 36237;

		for (int i = 1; i <= countOfTasks; i++) {
			assign[i] = i;
		}
	}

	/**
	 * 回溯搜索
	 */
	public void assignTask(int count) {
		if (count > countOfTasks) {
			// 如果对所有的task进行了分配，就计算花费
			computeCost();
		} else {
			// 对所有的task进行分配
			for (int j = count; j <= countOfTasks; j++) {
				swap(assign, count, j);
				assignTask(count + 1);
				swap(assign, count, j);
			}
		}
	}

	/**
	 * 计算最优
	 */
	private void computeCost() {
		int sum = 0;
		for (int i = 1; i <= countOfTasks; i++) {
			sum += costOfAgentTask[i][assign[i]];
		}

		if (sum < minimizedTotalCost) {
			minimizedTotalCost = sum;
			for (int i = 1; i <= countOfTasks; i++) {
				bestAssign[i] = assign[i];
			}
		}
	}

	/**
	 * 获得最优工作次序
	 * 
	 * @return
	 */
	public int[] getBestAssign() {
		return bestAssign;
	}

	/**
	 * 获得最小费用
	 * 
	 * @return
	 */
	public int getMinimizedTotalCost() {
		return minimizedTotalCost;
	}

	/**
	 * 交换
	 * 
	 * @param x
	 * @param i
	 * @param j
	 */
	private void swap(int[] x, int i, int j) {
		int temp = x[i];
		x[i] = x[j];
		x[j] = temp;
	}

}
