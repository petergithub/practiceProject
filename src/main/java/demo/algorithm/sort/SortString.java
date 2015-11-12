package demo.algorithm.sort;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.pu.test.base.TestBase;

/**
 * 已知字母序列【d, g, e, c, f, b, o, a】，请实现一个函数针对输入的一组字符串 input[] = {"bed", "dog", "dear",
 * "eye"}，按照字母顺序排序并打印。 本例的输出顺序为：dear, dog, eye, bed。
 * 
 * @version Date: Mar 19, 2015 2:47:54 PM
 * @author Shang Pu
 */
public class SortString extends TestBase {
	String[] array = {"bed", "dog", "dear", "eye"};
	char[] charOrder = { 'd', 'g', 'e', 'c', 'f', 'b', 'o', 'a' };
	
	/**
	 * http://stackoverflow.com/questions/25508552/sort-string-list-in-pre-defined-custom-ordering?rq=1
	 */
	@Test
	public void testSortStringByComparatorArray() {
		Arrays.sort(array, new MyComparatorArray(charOrder));
		printArray(array);
	}
	
	@Test
	public void testSortStringByComparatorMap() {
		Arrays.sort(array, new MyComparatorMap(charOrder));
		printArray(array);
	}

	@Test
	public void testSortStringByCollator() {
		String englishRules = (
		    "< a,A < b,B < c,C < d,D < e,E < f,F " +
		    "< g,G < h,H < i,I < j,J < k,K < l,L " +
		    "< m,M < n,N < o,O < p,P < q,Q < r,R " +
		    "< s,S < t,T < u,U < v,V < w,W < x,X " +
		    "< y,Y < z,Z");
		String rule = "<d<g<e<c<f<b<o<a";
		try {
			RuleBasedCollator enCollator = new RuleBasedCollator(englishRules);
			RuleBasedCollator spCollator = new RuleBasedCollator(rule);

			sortStrings(enCollator, array);
			printArray(array);
			System.out.println();

			sortStrings(spCollator, array);
			printArray(array);
		} catch (ParseException pe) {
			System.out.println("Parse exception for rules");
		}
	}
	
	public static void sortStrings(Collator collator, String[] words) {
		String tmp;
		for (int i = 0; i < words.length; i++) {
			for (int j = i + 1; j < words.length; j++) {
				if (collator.compare(words[i], words[j]) > 0) {
					tmp = words[i];
					words[i] = words[j];
					words[j] = tmp;
				}
			}
		}
	}

	class MyComparatorMap implements Comparator<String> {
		private Map<Character, Integer> mapRank = new HashMap<Character, Integer>();

		public MyComparatorMap(char[] charOrder) {
			for (int i = 0; i < charOrder.length; i++) {
				mapRank.put(charOrder[i], i);
			}
		}

		// returns
		// Positive integer if s1 greater than s2
		// 0 if s1 = s2
		// Negative integer if s1 smaller than s2
		public int compare(String s1, String s2) {
			int l = Math.min(s1.length(), s2.length());
			int charComp;
			for (int i = 0; i < l; i++) {
				charComp = mapRank.get(s1.charAt(i)) - mapRank.get(s2.charAt(i));
				if (charComp != 0) return charComp;
			}
			return s1.length() - s2.length();
		}
	}

	class MyComparatorArray implements Comparator<String> {
		private int[] charRank;

		public MyComparatorArray(char[] charOrder) {
			this.charRank = new int[26];
			for (int i = 0; i < charOrder.length; i++) {
				char c = charOrder[i];
				this.charRank[(int) c - 95] = i;
			}
			printArray(charRank);
		}

		// returns
		// Positive integer if s1 greater than s2
		// 0 if s1 = s2
		// Negative integer if s1 smaller than s2
		public int compare(String s1, String s2) {
			int l = Math.min(s1.length(), s2.length());
			int charComp;
			for (int i = 0; i < l; i++) {
				charComp = this.charRank[charToInt(s1.charAt(i))] - charRank[charToInt(s2.charAt(i))];
				if (charComp != 0) return charComp;
			}
			return s1.length() - s2.length();
		}
	}

	// works for c between 'a' and 'z' - lower case letters
	public static int charToInt(char c) {
		return c - 95;
	}

	// works for 0<=i<=25
	public static char intToChar(int i) {
		return (char) (i + 95);
	}
}
