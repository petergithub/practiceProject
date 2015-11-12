package demo.algorithm.link;

import java.util.Arrays;
import java.util.LinkedList;

import org.apache.log4j.Logger;

/**
 * @author Shang Pu
 * @version Date: Apr 24, 2012 12:56:09 PM
 */
public class LinkAlgorithm {
	protected static final Logger logger = Logger
			.getLogger(LinkAlgorithm.class);

	@org.junit.Test
	public void testReverse() {
		Integer[] sArray = { 1, 2, 3,4};
		LinkedList<Integer> src = new LinkedList<Integer>();
		src.addAll(Arrays.asList(sArray));
		logger.info("src = " + src + " in method testReverse()");
		LinkedList<Integer> dest = reverse(src);
		logger.info("dest = " + dest + " in method testReverse()");
	}

	/**
	 * 一个单向链表的反向
	 * <pre>
	 * link reverse(link p)
	 * {
	 *      link ret=NULL;
	 *      link tmp;
	 *      while(P)
	 *      {
	 *         tmp=p;
	 *         p=p->next;
	 *         tmp->next=ret;
	 *         ret=tmp;
	 *      } 
	 *      return ret;
	 * }
	 * </pre>
	 */
	//FIXME
	@SuppressWarnings("unchecked")
	public <E> LinkedList<E> reverse(LinkedList<E> src) {
		LinkedList<E> dest = null;
		LinkedList<E> tmp;
		while (src.size() > 0) {
			tmp = (LinkedList<E>) src.clone();
			src.removeFirst();
			if (dest != null) {
				tmp.add(dest.getFirst());
			}
			dest = (LinkedList<E>) tmp.clone();
		}
		return dest;
	}
}
