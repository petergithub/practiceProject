package demo.algorithm.link;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.algorithm.link.Link.Node;

/**
 * @author Shang Pu
 * @version Date: Apr 24, 2012 1:29:30 PM
 */
public class LinkTest {
	private static final Logger log = LoggerFactory.getLogger(LinkTest.class);

	public void testReverseWithNode() {
		Link link = new Link(10);

		log.info("link.getHead() = " + link.getHead());
		log.info("link = " + link.toString() + " before Reverse()");
		Node reverseHead = link.reverseByOneIteration(link.getHead());
		log.info("toString(reverseHead) = " + reverseHead);
		log.info("link.getHead() = " + link.getHead());
		link.reset();
		log.info("link.getHead() = " + link.getHead().next);
	}
	
	public void testReverseByRecursion() {
		Link link = new Link(3);
//		Node reverseHead = link.reverseByOneIteration(link.getHead());
		Node reverseHead = link.reverseByRecursion(link.getHead());
		log.info("toString(reverseHead) = " + reverseHead);
//		reverseByRecursion(link.getHead());
	}

	@Test
	public void testReversePartiallyByRecursion() {
		Link link = new Link(6);
	//		Node reverseHead = link.reverseByOneIteration(link.getHead());
		Node reverseHead = link.reversePartiallyByRecursion(link.getHead());
		log.info("toString(reverseHead) = " + reverseHead);
	//		reverseByRecursion(link.getHead());
	}

	public void testReverse() {
		Link link = new Link(10);
		log.info("link = " + link.toString() + " before Reverse()");
		link.reverse();
		log.info("link = " + link.toString() + " after Reverse()");
	}
}
