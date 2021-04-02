package com.al.po.cache.lru;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRU extends LinkedHashMap {
    int capacity;

    public LRU(int capacity) {
        super(16, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > capacity;
    }

    public static void main(String[] args) {
        LRU lru = new LRU(5);
        lru.put("1", 1);
        lru.put("2", 2);
        lru.put("3", 3);
        lru.put("4", 4);
        lru.get("1");
        lru.put("5", 5);
        lru.put("6", 6);

        System.out.println(lru.size() + " | " + lru);
    }
}
