package Cache;
 
/**
* @author Crunchify.com
*/
public class Cache {
 
    public static void main(String[] args) throws InterruptedException {
 
        Cache cache = new Cache();
 
        System.out.println("\n\n==========Test1: TestAddRemoveObjects ==========");
        cache.addRemoveObjects();
        System.out.println("\n\n==========Test2: TestExpiredCacheObjects ==========");
        cache.expiredCacheObjects();
        System.out.println("\n\n==========Test3: TestObjectsCleanupTime ==========");
        cache.objectsCleanupTime();
    }
 
    private void addRemoveObjects() {
 
        // Test with timeToLiveInSeconds = 200 seconds
        // timerIntervalInSeconds = 500 seconds
        // maxItems = 6
        MemoryCache<String, String> cache = new MemoryCache<String, String>(200, 500, 6);
 
        cache.put("eBay", "eBay");
        cache.put("Paypal", "Paypal");
        cache.put("Google", "Google");
        cache.put("Microsoft", "Microsoft");
        cache.put("IBM", "IBM");
        cache.put("Facebook", "Facebook");
 
        System.out.println("6 Cache Object Added.. cache.size(): " + cache.size());
        cache.remove("IBM");
        System.out.println("One object removed.. cache.size(): " + cache.size());
 
        cache.put("Twitter", "Twitter");
        cache.put("SAP", "SAP");
        System.out.println("Two objects Added but reached maxItems.. cache.size(): " + cache.size());
    }
 
    private void expiredCacheObjects() throws InterruptedException {
 
        // Test with timeToLiveInSeconds = 1 second
        // timerIntervalInSeconds = 1 second
        // maxItems = 10
        MemoryCache<String, String> cache = new MemoryCache<String, String>(1, 1, 10);
 
        cache.put("eBay", "eBay");
        cache.put("Paypal", "Paypal");
        // Adding 3 seconds sleep.. Both above objects will be removed from
        // Cache because of timeToLiveInSeconds value
        Thread.sleep(3000);
 
        System.out.println("Two objects are added but reached timeToLive. cache.size(): " + cache.size());
    }
 
    private void objectsCleanupTime() throws InterruptedException {
        int size = 500000;
 
        // Test with timeToLiveInSeconds = 100 seconds
        // timerIntervalInSeconds = 100 seconds
        // maxItems = 500000
 
        MemoryCache<String, String> cache = new MemoryCache<String, String>(100, 100, 500000);
 
        for (int i = 0; i < size; i++) {
            String value = Integer.toString(i);
            cache.put(value, value);
        }
 
        Thread.sleep(200);
 
        long start = System.currentTimeMillis();
        cache.cleanup();
        double finish = (double) (System.currentTimeMillis() - start) / 1000.0;
 
        System.out.println("Cleanup times for " + size + " objects are " + finish + " s");
    }
}