package demo.algorithm;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Consistent Hashing Blog
 * @author https://community.oracle.com/blogs/tomwhite/2007/11/27/consistent-hashing
 * @version Date：Jul 2, 2016 4:53:58 PM
 * @param <T>
 */
public class ConsistentHash<T> {

	private final HashFunction hashFunction;
	private final int numberOfReplicas;
	private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

	public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, Collection<T> nodes) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;

		for (T node : nodes) {
			add(node);
		}
	}

	public void add(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hash(node.toString() + i), node);
		}
	}

	public void remove(T node) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hash(node.toString() + i));
		}
	}

	public T get(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = hashFunction.hash(key);
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}
	
	class HashFunction {

		public Integer hash(String str) {
			int hash = 7;
			for (int i = 0; i < str.length(); i++) {
			    hash = hash*31 + str.charAt(i);
			}
			return hash;
		}

		public int hash(Object key) {
			return 0;
		}
		
	}

}