package demo.algorithm.anagramsbystack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

/**
 * 
 <pre>
 * Background
 * 背景
 * 
 * How can anagrams result from sequences of stack operations? 
 * There are two sequences of stack operators which can convert 
 * "TROT" to "TORT": 怎样才能通过一系列的堆栈操作，将字符串按要求的
 * 顺序重新排列？比如有两种进出栈序列可以将字符串“TROT”转换为“TORT”，如下: 
 * 
 * [
 * i i i i o o o o
 * i o i i o o i o
 * ]
 * 
 * where i stands for Push and o stands for Pop. Your program should,
 * given pairs of words produce sequences of stack operations 
 * which convert the first word to the second.
 * 其中i表示压入，o表示弹出。给定一对单词，你要写一个程序通过一组进出栈的操作，
 * 使前一个单词的字母顺序变成后一个单词。
 *  
 * Input
 * 输入
 * The input will consist of several lines of input. The first line of 
 * each pair of input lines is to be considered as a source word 
 * (which does not include the end-of-line character). 
 * The second line (again, not including the end-of-line character) 
 * of each pair is a target word.
 * 输入由多行组成，每对单词占2行。第1行为原始单词（不包括行尾的换行符），
 * 第2行为目标单词（同样不包括行尾的换行符）。
 *  
 * Output
 * 输出
 * For each input pair, your program should produce a sorted list of 
 * valid sequences of i and o which produce the target word from the 
 * source word. Each list should be delimited by
 * 对于输入的每一对字符串，你的程序应该按顺序生成所有的有效的进出栈操作列表，
 * 其中的每组操作都能使原字符串转变为目标字符串。每一组操作要由方括号括起来: 
 * [
 * ]
 * and the sequences should be printed in "dictionary order".
 * Within each sequence, each i and o is followed by a single space and
 * each sequence is terminated by a new line.
 * 序列中每组操作应该按字典顺序输出。在一组操作中，i和o用空格隔开，每一组独占一行。
 *  
 * Process
 * 处理
 * A stack is a data storage and retrieval structure permitting two operations:
 * 堆栈是一种数据结构，它只允许两种操作: 
 * Push - to insert an item and
 *       压入——向栈中插入一项
 * Pop - to retrieve the most recently pushed item
 *       弹出——取出最后插入的一项
 * We will use the symbol i (in) for push and o (out) for pop operations for an initially empty stack of characters. Given an input word, some sequences of push and pop operations are valid in that every character of the word is both pushed and popped, and furthermore, no attempt is ever made to pop the empty stack. For example, if the word FOO is input, then the sequence:
 * 你要使用“i”来表示进栈，“o”表示出栈，栈的初始状态为空。给定一个输入的单词，只要对每一个字母都进行了进栈和出栈操作，这一组操作才可能是有效的。进一步来说，不能对空栈执行弹出操作。比如对于输入的单词FOO，下面的操作序列: 
 * 
 * i i o i o o is valid, but
 *       i i o i o o是有效的，但
 * i i o is not (it's too short),
 *       i i o是无效的，（没有完成操作）
 * neither is i i o o o i (there's an illegal pop of an empty stack)
 *       i i o o o i同样无效（执行了非法的从空栈弹出的操作）
 * 
 * Valid sequences yield rearrangements of the letters in an input word. For example, the input word FOO and the sequence i i o i o o produce the anagram OOF. So also would the sequence i i i o o o. You are to write a program to input pairs of words and output all the valid sequences of i and o which will produce the second member of each pair from the first.
 * 可能有多组有效操作都能够为输入的单词生成指定的字母排列。比如对于输入的单词“FOO”，操作i i o i o o就可以生成重排的单词“OOF”。操作i i i o o o也可以生成同样的重排。你要写一个程序，找出所有有效的操作序列，以使输入的一对单词的前者重排为后者。
 *  
 * Sample Input
 * 输入示例
 * 
 * madam
 * adamm
 * bahama
 * bahama
 * long
 * short
 * eric
 * rice
 * 
 * Sample Output
 * 输出示例
 * 
 * [
 * i i i i o o o i o o
 * i i i i o o o o i o
 * i i o i o i o i o o
 * i i o i o i o o i o
 * ]
 * [
 * i o i i i o o i i o o o
 * i o i i i o o o i o i o
 * i o i o i o i i i o o o
 * i o i o i o i o i o i o
 * ]
 * [
 * ]
 * [
 * i i o i o i o o
 * ]
 * </pre>
 * 
 * @author Shang Pu
 * @version Date: Apr 20, 2012 10:03:06 PM
 */
public class AnagramsByStack {

	String src;
	String dest;
	char[] ioArray;
	Stack<Character> stack = new Stack<Character>();

	public void testTry() {
		ioArray = new char[500];
		// src = "lal";
		// dest = "all";
		src = "trot";
		dest = "tort";
		// src = "madam";
		// dest = "adamm";
		System.out.println('[');
		generateIoSimple(0, 0);
		System.out.println(']');
	}

	/**
	 * 代码的执行过程就是边入栈，边匹配，匹配了就出栈，不匹配就继续入栈， 直到找到所有的可能的入栈、出栈序列为止。为了能够找到所有可能的序列，
	 * 还需要在递归搜索过程中，从递归函数返回时恢复原来的环境，以便能够继续搜索。
	 * 
	 * <pre>
	 * 这一题就是要把一个输入串仅仅通过栈的入栈和出栈操作转化为另一个串。初看此题，
	 * 我感觉非常难以下手，不知道该如何是好。 仔细分析题目中的两个串之间有什么规律
	 * ，时间花了很多的，但是一点效果都没有。请教了一位ACM很牛的同学。他马上就说了
	 * 一句话，直击题目的核心。就是你不用电脑，只用纸和笔，你会怎么算呢？是的
	 * ，如果我只用纸和笔，我会怎么做呢？我想对于madam和adamm这两个串来说，
	 * 当然就是先入m，然后在入a，然后弹出a ，然后重复此过程直到找到一种出栈、
	 * 入栈序列为止。他说，你把你的这个思考过程转化成代码，让电脑能够执行不就行了吗？
	 * 下面是我的代码。
	 * 
	 * <pre>
	 * 
	 * <pre>
	 * #include <iostream>
	 * #include <string>
	 * #include <stack>
	 * #include <fstream>
	 * using namespace std;
	 * 
	 * char *pSrc = NULL, *pDest = NULL;
	 * stack<char> s1;
	 * char res[1000];
	 * int cnt = 0;
	 * 
	 * void Try()
	 * {
	 *     if (*pDest == '/0') {
	 *         for (int i = 0; i != cnt; ++i) {
	 *             cout << res[i];
	 *             if (i != cnt - 1)
	 *                 cout << ' ';
	 *         }
	 *         cout << endl;
	 *     }
	 *     if (!s1.empty()) {
	 *         char c = s1.top();
	 *         if (c == *pDest) {
	 *             s1.pop();
	 *             res[cnt++] = 'o';
	 *             ++pDest;
	 *             Try();
	 *             s1.push(c);
	 *             --cnt;
	 *             --pDest;
	 *         }
	 *     }
	 *     if (*pSrc != '/0') {
	 *         s1.push(*pSrc);
	 *         res[cnt++] = 'i';
	 *         ++pSrc;
	 *         Try();
	 *         s1.pop();
	 *         --cnt;
	 *         --pSrc;
	 *     }
	 * }
	 * 
	 * int main()
	 * {
	 *     ifstream cin("data");
	 *     string strSrc, strDsti;
	 *     while (cin >> strSrc >> strDsti) {
	 *         pSrc = const_cast<char*>(strSrc.c_str());
	 *         pDest = const_cast<char*>(strDsti.c_str());
	 *         cout << '[' << endl;
	 *         Try();
	 *         cout << ']' << endl;
	 *     }
	 *     return 0;
	 * }
	 * </pre>
	 * 
	 * refer to http://blog.csdn.net/archimedes_zht/article/details/2306036
	 */
	private void generateIoSimple(int iCount, int oCount) {
		int ioCount = iCount + oCount;
		if (oCount == dest.length()) {
			for (int i = 0; i < ioCount; ++i) {
				System.out.print(ioArray[i]);
			}
			System.out.println();
		}
		if (!stack.empty()) {
			char c = stack.peek();
			if (c == dest.charAt(oCount)) {
				ioArray[ioCount] = 'o';
				stack.pop();
				oCount++;
				generateIoSimple(iCount, oCount);
				oCount--;
				stack.push(c);
			}
		}
		if (iCount < src.length()) {
			ioArray[ioCount] = 'i';
			stack.push(src.charAt(iCount));
			iCount++;
			generateIoSimple(iCount, oCount);
			iCount--;
			stack.pop();
		}
	}

	/**
	 * <pre>
	 * 	#include<iostream>
	 * 	#include<stack>
	 * 	#include<cstring>
	 * 	using namespace std;
	 * 
	 * 	int a_len, b_len;
	 * 	char a[100], b[100];
	 * 	bool s[200];
	 * 	stack<char> S;
	 * 
	 * 	void DFS(int i, int j, int k)
	 * 	{
	 * 	    if(j >= b_len){
	 * 	        for(int x = 0; x < k; ++x)
	 * 	            if(s[x]) printf("i ");
	 * 	            else printf("o ");
	 * 	        printf("\n");
	 * 	        return;
	 * 	    }
	 * 	    if(i < a_len){
	 * 	        S.push(a[i]);
	 * 	        s[k] = 1;
	 * 	        DFS(i + 1, j, k + 1);
	 * 	        S.pop();
	 * 	    }
	 * 	    if(!S.empty() && b[j] == S.top()){
	 * 	        char c = S.top();
	 * 	        S.pop();
	 * 	        s[k] = 0;
	 * 	        DFS(i, j + 1, k + 1);
	 * 	        S.push(c);
	 * 	    }
	 * 	}
	 * 
	 * 	int main()
	 * 	{
	 * 	    while(scanf("%s %s", a, b) != EOF){
	 * 	        a_len = strlen(a);
	 * 	        b_len = strlen(b);
	 * 	        while(!S.empty()) S.pop();
	 * 	        printf("[\n");
	 * 	        DFS(0, 0, 0);
	 * 	        printf("]\n");
	 * 	    }
	 * 	    return 0;
	 * 	}
	 * 	refer to http://hi.baidu.com/racebug/blog/item/6e46974e1446cc3fafc3ab80.html
	 * </pre>
	 */
	@SuppressWarnings("unused")
	private void DFS(int iCount, int oCount) {
		int ioCount = iCount + oCount;
		if (oCount >= dest.length()) {
			for (int index = 0; index < ioCount; ++index)
				System.out.print(ioArray[index]);
			System.out.println();
			return;
		}
		if (iCount < src.length()) {
			stack.push(src.charAt(iCount));
			ioArray[ioCount] = 'i';
			DFS(iCount + 1, oCount);
			stack.pop();
		}
		if (!stack.empty() && dest.charAt(oCount) == stack.peek()) {
			char c = stack.pop();
			ioArray[ioCount] = 'o';
			DFS(iCount, oCount + 1);
			stack.push(c);
		}
	}

	public void testGeneIO() {
		src = "la";
		dest = "al";
		// src = "trot";
		// dest = "tort";
		// src = "madam";
		// dest = "adamm";
		ioArray = new char[500];
		if (src.length() != dest.length()) {
			System.out.println('[');
			System.out.println(']');
		}
		System.out.println('[');
		geneIO(0, 0);
		System.out.println(']');
	}

	/**
	 * @param iCount - the time of push i
	 * @param oCount - the time of pop o
	 * 
	 *            <pre>
	 * #include<iostream>
	 * #include<queue>
	 * #include<stack>
	 * #include<vector>
	 * #include<string>
	 * #include<cstring>
	 * 
	 * using namespace std;
	 * string START,END;
	 * int CountI,CountO;
	 * char IO[500];
	 * int ok(void)
	 * {
	 *     stack<char> S;
	 *     string result;
	 *     int i,in = 0;
	 *     for( i = 0; i < 2*END.length(); i++ )
	 *     {
	 *         if( IO[i] == 'i' )
	 *             S.push(START[in++]);
	 *         else
	 *             result.append( 1,S.top() ),S.pop();
	 *     }
	 *     if( END == result ) return 1;
	 *     else return 0;
	 * }
	 * void GeneIO( int x ,int CountI,int CountO )
	 * {
	 *     int i;
	 *     if( x == 2*END.length() )
	 *     {
	 *         if( CountI - CountO ) return;
	 *         if( ok() )
	 *         {
	 *             for( i = 0; i < 2*END.length(); i++ )
	 *                 cout << IO[i] << ' ';
	 *             cout << endl;
	 *         }
	 *         return;
	 *     }
	 *     if( CountI > CountO )
	 *     {
	 *         IO[x] = 'i';
	 *         GeneIO( x + 1 ,CountI + 1,CountO );
	 *         IO[x] = 'o';
	 *         GeneIO( x + 1 ,CountI,CountO + 1 );
	 *     }
	 *     else if( CountI == CountO )
	 *     {
	 *         IO[x] = 'i';
	 *         GeneIO( x + 1 ,CountI + 1,CountO );
	 *     }
	 * }
	 * 
	 * int main(void)
	 * {
	 * 
	 *     while( cin >> START >> END )
	 *     {
	 *         if( START.length() != END.length() )
	 *         {
	 *             cout << '[' << endl << ']' << endl;
	 *             continue;
	 *         }
	 *         cout << '[' << endl;
	 *         GeneIO(0,0,0);
	 *         cout << ']' << endl;
	 *     }
	 *     return 0;
	 * }
	 * </pre>
	 */
	private void geneIO(int iCount, int oCount) {
		int ioCount = iCount + oCount;
		if (ioCount == 2 * dest.length()) {
			if (iCount - oCount > 0) return;
			if (getOrders(src, dest, ioArray)) {
				for (int i = 0; i < 2 * dest.length(); i++)
					System.out.print(ioArray[i]);
				System.out.println();
			}
			return;
		}
		if (iCount > oCount) {
			ioArray[ioCount] = 'i';
			geneIO(iCount + 1, oCount);
			ioArray[ioCount] = 'o';
			geneIO(iCount, oCount + 1);
		} else if (iCount == oCount) {
			ioArray[ioCount] = 'i';
			geneIO(iCount + 1, oCount);
		}
	}

	private boolean getOrders(String src, String dest, char[] ioArray) {
		Stack<Character> stack = new Stack<Character>();
		String result = "";
		int timesOfIo, in = 0;
		for (timesOfIo = 0; timesOfIo < 2 * dest.length(); timesOfIo++) {
			if (ioArray[timesOfIo] == 'i') {
				stack.push(src.charAt(in++));
			} else {
				result += stack.pop();
			}
		}
		return dest.equals(result);
	}

	@org.junit.Test
	public void testGenerateAnagrams() {
		String[] srcArrayStrings = { "t", "r", "o", "t" };
		String[] destArrayStrings = { "t", "o", "r", "t" };
		LinkedList<String> src = new LinkedList<String>();
		src.addAll(Arrays.asList(srcArrayStrings));
		LinkedList<String> dest = new LinkedList<String>();
		dest.addAll(Arrays.asList(destArrayStrings));
		LinkedList<String> order = new LinkedList<String>();
		Stack<String> stack = new Stack<String>();
		generateAnagrams(src, dest, order, stack);
	}

	/**
	 * it's a bad implement
	 * 
	 * <pre>
	 * #include <iostream>
	 * #include <string>
	 * #include <vector>
	 * using namespace std;
	 * //深度遍例所有可能的进出栈序列，进栈视为左子树，出栈视为右子树
	 * void GenAnagram(vector<char> &Src, vector<char> &Dest,
	 *               vector<char> &Order, vector<char> &Stack) {
	 *   //如果字符串不为空，则执行进栈操作，相当于遍例左子树
	 *   if (!Src.empty()) {
	 *       //在操作序列中加入i
	 *       Order.push_back('i');
	 *       //进栈并保存现场
	 *       Stack.push_back(Src.back());
	 *       Src.pop_back();
	 *       //以当前状态遍例下一个动作
	 *       GenAnagram(Src, Dest, Order, Stack);
	 *       //恢复现场
	 *       Src.push_back(Stack.back());
	 *       Stack.pop_back();
	 *       Order.pop_back();
	 *   }
	 *   //如果栈不为空则执行弹栈操作，当栈顶和目标串相应字符相等时才继续
	 *   if (!Stack.empty() && Stack.back() == Dest[Stack.size() + Src.size() - 1]) {
	 *       //在操作序列中加入o
	 *       Order.push_back('o');
	 *       //弹栈并保存现场
	 *       char cTemp = Stack.back();
	 *       Stack.pop_back();
	 *       //以当前状态遍例下一个动作
	 *       GenAnagram(Src, Dest, Order, Stack);
	 *       //恢复现场
	 *       Stack.push_back(cTemp);
	 *       Order.pop_back();
	 *   }
	 *   //如果原字符串已空，且栈也为同，则说明所有字符都已出栈
	 *   if (Src.empty() && Stack.empty()) {
	 *       //输出进出栈操作序列
	 *       vector<char>::iterator i = Order.begin();
	 *       for (; i != Order.end() - 1; ++i ) {
	 *           cout << *i << ' ';
	 *       }
	 *       cout << *i << endl;
	 *   }
	 * }
	 * //主函数
	 * int main(void) {
	 *   //循环处理输入的每一对字符串
	 *   for (string str1, str2; cin >> str1 >> str2; cout << ']' << endl) {
	 *       cout << '[' << endl;
	 *       //当两字符串长度相等且不为空时才运算
	 *       if (str1.length() == str2.length() && !str1.empty()) {
	 *           //建立原串、目标串、字符栈和结果数组
	 *           vector<char> Src(str1.rbegin(), str1.rend());
	 *           vector<char> Dest(str2.rbegin(), str2.rend());
	 *           vector<char> Order, Stack;
	 *           //执行深度遍例，输出所有结果
	 *           GenAnagram(Src, Dest, Order, Stack);
	 *       }
	 *   }
	 *   return 0;
	 * }
	 * </pre>
	 * 
	 * refer to http://www.cnblogs.com/devymex/archive/2010/08/04/1792276.html
	 * Analysis
	 * 分析
	 * 这道题必须遍例所有可能的进出栈序列才能求得全部解。如果将进出栈序列视为二叉树，
	 * 那么求解的过程就是一个典型的深度遍例。进栈为左子节点，出栈为右子节点
	 * 。当所有字符都已出栈，即遍例到了叶子节点，应打印输出前面的进出栈序列
	 * （也可以视为从根到叶子的路径）。
	 * 如果在弹栈时发现当前弹出的字符与目标串中的该字符位置不符，则无需再遍例
	 * 这一支的子节点，直接回溯即可。思路很简单，就看实现了。注意，不要在行尾输出空格。
	 */
	// 深度遍例所有可能的进出栈序列，进栈视为左子树，出栈视为右子树
	private void generateAnagrams(LinkedList<String> src,
			LinkedList<String> dest, LinkedList<String> order,
			Stack<String> stack) {
		// 如果字符串不为空，则执行进栈操作，相当于遍例左子树
		if (!src.isEmpty()) {
			// 在操作序列中加入i
			order.addFirst("i");
			// 进栈并保存现场
			String first = src.getFirst();
			stack.push(first);
			src.removeFirst();
			// 以当前状态遍例下一个动作
			generateAnagrams(src, dest, order, stack);
			// 恢复现场
			src.addFirst(first);
			stack.pop();
			// order.removeFirst();
		}
		// 如果栈不为空则执行弹栈操作，当栈顶和目标串相应字符相等时才继续
		if (!stack.isEmpty()
				&& stack.peek().equals(dest.get(stack.size() + src.size() - 1))) {
			// 在操作序列中加入o
			order.addFirst("o");
			// 弹栈并保存现场
			String cTemp = stack.pop();
			// 以当前状态遍例下一个动作
			generateAnagrams(src, dest, order, stack);
			// 恢复现场
			stack.push(cTemp);
			// order.removeFirst();
		}
		// 如果原字符串已空，且栈也为同，则说明所有字符都已出栈
		if (src.isEmpty() && stack.isEmpty()) {
			// 输出进出栈操作序列
			for (String s : order) {
				System.out.print(s);
			}
			System.out.println();
		}
	}
}
