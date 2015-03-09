package demo.algorithm.link;

import org.apache.log4j.Logger;
import org.junit.Test;

import demo.algorithm.link.Link.Node;

/**
 * @author Shang Pu
 * @version Date: Apr 24, 2012 1:29:30 PM
 */
public class LinkTest {
	protected static final Logger logger = Logger.getLogger(Link.class);

	public void testReverseWithNode() {
		Link link = new Link(10);

		logger.info("link.getHead() = " + link.getHead());
		logger.info("link = " + link.toString() + " before Reverse()");
		Node reverseHead = link.reverseByOneIteration(link.getHead());
		logger.info("toString(reverseHead) = " + reverseHead);
		logger.info("link.getHead() = " + link.getHead());
		link.reset();
		logger.info("link.getHead() = " + link.getHead().next);
	}
	
	public void testReverseByRecursion() {
		Link link = new Link(3);
//		Node reverseHead = link.reverseByOneIteration(link.getHead());
		Node reverseHead = link.reverseByRecursion(link.getHead());
		logger.info("toString(reverseHead) = " + reverseHead);
//		reverseByRecursion(link.getHead());
	}

	@Test
	public void testReversePartiallyByRecursion() {
		Link link = new Link(6);
	//		Node reverseHead = link.reverseByOneIteration(link.getHead());
		Node reverseHead = link.reversePartiallyByRecursion(link.getHead());
		logger.info("toString(reverseHead) = " + reverseHead);
	//		reverseByRecursion(link.getHead());
	}

	public void testReverse() {
		Link link = new Link(10);

		logger.info("link = " + link.toString() + " before Reverse()");
		link.reverse();
		logger.info("link = " + link.toString() + " after Reverse()");
	}
}
