package Cache;
 
import java.util.ArrayList;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.LRUMap;
 
/**
* @author Crunchify.com
*/
public class MemoryCache<K, T> {
 
    private long timeToLive;
    private LRUMap cacheMap;
 
    protected class CrunchifyCacheObject {
        public long lastAccessed = System.currentTimeMillis();
        public T value;
 
        protected CrunchifyCacheObject(T value) {
            this.value = value;
        }
    }
 
    public MemoryCache(long timeToLive, final long timerInterval, int maxItems) {
        this.timeToLive = timeToLive * 1000;
 
        this.cacheMap = new LRUMap(maxItems);
 
        if (this.timeToLive > 0 && timerInterval > 0) {
 
            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(timerInterval * 1000);
                        } catch (InterruptedException ex) {
                        }
                        cleanup();
                    }
                }
            });
 
            t.setDaemon(true);
            t.start();
        }
    }
 
    public void put(K key, T value) {
        synchronized (this.cacheMap) {
            cacheMap.put(key, new CrunchifyCacheObject(value));
        }
    }
 
    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (this.cacheMap) {
            CrunchifyCacheObject c = (CrunchifyCacheObject) this.cacheMap.get(key);
 
            if (c == null)
                return null;
            else {
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }
 
    public void remove(K key) {
        synchronized (this.cacheMap) {
            this.cacheMap.remove(key);
        }
    }
 
    public int size() {
        synchronized (this.cacheMap) {
            return this.cacheMap.size();
        }
    }
 
    @SuppressWarnings("unchecked")
    public void cleanup() {
 
        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey = null;
 
        synchronized (this.cacheMap) {
            MapIterator itr = this.cacheMap.mapIterator();
 
            deleteKey = new ArrayList<K>((this.cacheMap.size() / 2) + 1);
            K key = null;
            CrunchifyCacheObject c = null;
 
            while (itr.hasNext()) {
                key = (K) itr.next();
                c = (CrunchifyCacheObject) itr.getValue();
 
                if (c != null && (now > (this.timeToLive + c.lastAccessed))) {
                    deleteKey.add(key);
                }
            }
        }
 
        for (K key : deleteKey) {
            synchronized (this.cacheMap) {
                this.cacheMap.remove(key);
            }
 
            Thread.yield();
        }
    }
}