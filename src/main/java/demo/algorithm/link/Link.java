package demo.algorithm.link;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 链表是一种重要的数据结构，在程序设计中占有很重要的地位。C语言和C＋＋语言中是用指针来实现链表结构的，由于Java语言不提供指针，
 * 所以有人认为在Java语言中不能实现链表
 * ，其实不然，Java语言比C和C＋＋更容易实现链表结构。Java语言中的对象引用实际上是一个指针（本文中的指针均为概念上的意义
 * ，而非语言提供的数据类型），所以我们可以编写这样的类来实现链表中的结点。
 * 
 * <pre>
 * class Node {
 * 	Object data;
 * 	Node next;// 指向下一个结点
 * }
 * </pre>
 * 
 * 将数据域定义成Object类是因为Object类是广义超类，任何类对象都可以给其赋值，增加了代码的通用性。为了使链表可以被访问还需要定义一个表头，
 * 表头必须包含指向第一个结点的指针和指向当前结点的指针。为了便于在链表尾部增加结点，还可以增加一指向链表尾部的指针，另外还可以用一个域来表示链表的大小，
 * 当调用者想得到链表的大小时
 * ，不必遍历整个链表。
 * 链表的数据结构
 * 我们可以用类List来实现链表结构，用变量head、tail、
 * length、pointer来实现表头。存储当前结点的指针时有一定的技巧，pointer并非存储指向当前结点的指针
 * ，而是存储指向它的前趋结点的指针,当其值为null时表示当前结点是第一个结点
 * 。那么为什么要这样做呢？这是因为当删除当前结点后仍需保证剩下的结点构成链表，如果Pointer指向当前结点
 * ，则会给操作带来很大困难。那么如何得到当前结点呢，
 * 我们定义了一个方法cursor(),返回值是指向当前结点的指针。类List还定义了一些方法来实现对链表的基本操作
 * ，通过运用这些基本操作我们可以对链表进行各种操作。例如reset()方法使第一个结点成为当前结点。 insert(Object
 * d)方法在当前结点前插入一个结点
 * ，并使其成为当前结点。remove()方法删除当前结点同时返回其内容，并使其后继结点成为当前结点，如果删除的是最后一个结点，则第一个结点变为当前结点。
 * 
 * @author Shang Pu
 * @version Date: Apr 24, 2012 1:29:30 PM
 */
public class Link {
	private static final Logger log = LoggerFactory.getLogger(Link.class);
	
	/** 构成链表的结点定义 */
	class Node {
		private Object data;
		Node next;

		private Node(Object d) {
			data = d;
			next = null;
		}

		public Node() {
		}

		Node(Node node) {
			data = node.data;
			next = node.next;
		}

		public String toString() {
			if (next != null) {
				return data.toString() + " -> " + next.toString();
			} else {
				return data.toString();
			}
		}
	}

	/** 用变量来实现表头 */
	private Node head = null;
	private Node tail = null;
	/**
	 * pointer并非存储指向当前结点的指针，而是存储指向它的前趋结点的指针,当其值为null时表示当前结点是第一个结点
	 */
	private Node pointer = null;
	private int length = 0;

	public Link() {
	}

	public Link(int size) {
		for (int i = 0; i < size; i++) {
			this.insert(i);
		}
	}

	/** 清空整个链表 */
	public void clear() {
		head = null;
		tail = null;
		pointer = null;
		length = 0;
	}

	/** 链表复位，使第一个结点成为当前结点 */
	public void reset() {
		pointer = null;
	}

	/** 判断链表是否为空 */
	public boolean isEmpty() {
		return (length == 0);
	}

	/** 判断当前结点是否为最后一个结点 */
	public boolean isEnd() {
		if (length == 0)
			throw new NullPointerException();
		else if (length == 1)
			return true;
		else
			return (cursor() == tail);
	}

	/** 返回当前结点的下一个结点的值，并使其成为当前结点 */
	public Object nextNode() {
		if (length == 1)
			throw new java.util.NoSuchElementException();
		else if (length == 0)
			throw new NullPointerException();
		else {
			Node temp = cursor();
			pointer = temp;
			if (temp != tail)
				return (temp.next.data);
			else
				throw new java.util.NoSuchElementException();
		}
	}

	/** 返回当前结点的值 */
	public Object currentNode() {
		Node temp = cursor();
		return temp.data;
	}

	/** 在当前结点前插入一个结点，并使其成为当前结点 */
	public void insert(Object d) {
		Node node = new Node(d);
		if (length == 0) {
			tail = node;
			head = node;
		} else {
			Node temp = cursor();
			node.next = temp;
			if (pointer == null)
				head = node;
			else
				pointer.next = node;
		}
		length++;
	}

	/** 返回链表的大小 */
	public int size() {
		return (length);
	}

	/** 将当前结点移出链表，下一个结点成为当前结点，如果移出的结点是最后一个结点，则第一个结点成为当前结点 */
	public Object remove() {
		Object temp;
		if (length == 0)
			throw new java.util.NoSuchElementException();
		else if (length == 1) {
			temp = head.data;
			clear();
		} else {
			Node cur = cursor();
			temp = cur.data;
			if (cur == head)
				head = cur.next;
			else if (cur == tail) {
				pointer.next = null;
				tail = pointer;
				reset();
			} else
				pointer.next = cur.next;
			length--;
		}
		return temp;
	}

	/** 返回当前结点的指针 */
	private Node cursor() {
		if (head == null)
			throw new NullPointerException();
		else if (pointer == null)
			return head;
		else
			return pointer.next;
	}

	/**
	 * reverse the link
	 */
	public void reverse() {
		if (head == null || head.next == null) return;
		head = reverseByOneIteration(head);
	}

	/**
	 * reverse the link and return the link with the first Node
	 * 
	 * <pre>
	 * a <- b <- ... <- l    m -> n -> ...
	 * a <- b <- ... <- l <- m    n -> ...
	 * 
	 * Node* iteratively_reverse_list(Node *head) {
	 *   if(NULL == head || NULL == head->next)
	 *     return head;
	 *   Node *p, *q, *r;
	 *   p = NULL;
	 *   q = head;
	 *   while(q) {
	 *     r = q->next;
	 *     q->next = p;
	 *     p = q;
	 *     q = r;
	 *   }
	 *   head->next = NULL;
	 *   return p;
	 * }
	 * </pre>
	 */
	public Node reverseByOneIteration(Node head) {
		if (head == null || head.next == null) return head;
		
		Node current = head;
		Node pre = null;
		Node next = null;
		while (current != null) {
			next = current.next;
			current.next = pre;
			pre = current;
			current = next;
		}
		return pre;
	}
	
	public Node reverseByOneIteration2(Node head) {
		if (log.isDebugEnabled())
			log.debug("Enter reverse(" + head + ")");
		// return this node as head
		Node reverseHead = null;
		while (head != null) {
			Node temp = new Node(head);
			temp.next = reverseHead;
			reverseHead = temp;
			// move forward the next node
			head = head.next;
		}
		log.debug("Exit reverse()");
		return reverseHead;
	}

	/**
	 * reverse the link and return the link with the first Node
	 * 
	 * <pre>
	 * a <- b <- ... <- l    m -> n -> ...
	 * a <- b <- ... <- l <- m    n -> ...
	 * </pre>
	 */
	public Node reverseByRecursion(Node head) {
		if (log.isDebugEnabled())
			log.debug("Enter reverseByRecursion(" + head + ")");
		// return this node as head
		if (head == null || head.next == null) return head;
		Node rHead = reverseByRecursion(head.next);
		head.next.next = head;
		head.next = null;
		log.debug("Exit reverseByRecursion()" + rHead);
		return rHead;
	}
	
	/**
	 * 实现单链表部分转置，将相邻两个节点交换，如：a->b->c->d->e，变为：b->a->d->c->e
	 * @param head
	 * @return
	 */
	public Node reversePartiallyByRecursion(Node head) {
		if (log.isDebugEnabled())
			log.debug("Enter reverseByRecursion(" + head + ")");
		// return this node as head
		if (head == null || head.next == null) return head;
		Node ret = head.next;
		Node rHead = reversePartiallyByRecursion(head.next.next);
		head.next.next = head;
		head.next = rHead;
		log.debug("Exit reverseByRecursion()" + rHead);
		return ret;
	}

	/**
	 * @return 9 -> 8 -> 7 -> 6 -> 5 -> 4 -> 3 -> 2 -> 1 -> 0
	 */
	public String toString() {
		return head.toString();
	}

	public Node getHead() {
		return head;
	}

	public static void main(String[] args) {
		Link link = new Link(10);
		System.out.println(link.currentNode());
		while (!link.isEnd())
			System.out.println(link.nextNode());
		link.reset();
		while (!link.isEnd()) {
			link.remove();
		}
		link.remove();
		link.reset();
		if (link.isEmpty()) System.out.println("There is no Node in List \n");
	}
}
