package demo.algorithm.link;

import org.apache.log4j.Logger;

import demo.algorithm.link.Link;
import demo.algorithm.link.Link.Node;

/**
 * @author Shang Pu
 * @version Date: Apr 24, 2012 1:29:30 PM
 */
public class LinkTest {
	protected static final Logger logger = Logger.getLogger(Link.class);

	@org.junit.Test
	public void testReverseWithNode() {
		Link link = new Link(10);

		logger.info("link.getHead() = " + link.getHead());
		logger.info("link = " + link.toString() + " before Reverse()");
		Node reverseHead = link.reverse(link.getHead());
		logger.info("toString(reverseHead) = " + reverseHead);
		logger.info("link.getHead() = " + link.getHead());
		link.reset();
		logger.info("link.getHead() = " + link.getHead().next);
	}

	public void testReverse() {
		Link link = new Link(10);

		logger.info("link = " + link.toString() + " before Reverse()");
		link.reverse();
		logger.info("link = " + link.toString() + " after Reverse()");
	}
}
