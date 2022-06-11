package com.example.SlaveNode.Cache;



public class CacheController {

    private static final int MAX_CAPACITY = 1000;
    private static  LRUCache<String, Object> globalCache = new LRUCache<>(MAX_CAPACITY);


    private CacheController(){}


    public static void add(String key, Object value){
        globalCache.put(key, value);
    }


    public static void clearCache(){
        globalCache = new LRUCache<>(MAX_CAPACITY);
    }

    public static Object getCacheObject(String key){
        return globalCache.get(key);
    }
}
