package fib;

import java.util.Hashtable;

public class FibServer {
	
	private Hashtable<Integer, Integer> cache = new Hashtable<Integer, Integer>();
	private int port;
	
	public FibServer(int port) {
		this.port = port;
	}
	
	synchronized private void addToCache(Integer key, Integer value) {
		cache.put(key, value);
	}
	
	synchronized private Integer getInCache(Integer key) {
		return cache.get(key);
	}
	
	synchronized private boolean isInCache(Integer key) {
		if (cache.containsKey(key)) {
			return true;
		}
		return false;
	}
}
