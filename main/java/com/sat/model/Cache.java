package com.sat.model;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class Cache<K, V> {
    private final int maxSize;
    private final long defaultTTL;
    private final Map<K, Node<K, V>> map;
    private final DoublyLinkedList<K, V> lruList;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();
    private final AtomicLong evictions = new AtomicLong();
    private final AtomicLong expired = new AtomicLong();

    public Cache(int maxSize, long defaultTTLSeconds) {
        this.maxSize = maxSize;
        this.defaultTTL = TimeUnit.SECONDS.toMillis(defaultTTLSeconds);
        this.map = new HashMap<>();
        this.lruList = new DoublyLinkedList<>();
        startCleanup();
    }

    public void put(K key, V value) {
        put(key, value, defaultTTL);
    }

    public void put(K key, V value, long ttlMillis) {
        long expiry = System.currentTimeMillis() + ttlMillis;
        lock.writeLock().lock();
        try {
            if (map.containsKey(key)) {
                Node<K, V> node = map.get(key);
                node.value = value;
                node.expiryTime = expiry;
                lruList.moveToFront(node);
            } else {
                if (map.size() >= maxSize) {
                    Node<K, V> lru = lruList.removeLast();
                    if (lru != null) {
                        map.remove(lru.key);
                        evictions.incrementAndGet();
                    }
                }
                Node<K, V> newNode = new Node<>(key, value, expiry);
                lruList.addFirst(newNode);
                map.put(key, newNode);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public V get(K key) {
        lock.writeLock().lock();
        try {
            Node<K, V> node = map.get(key);
            if (node == null) {
                misses.incrementAndGet();
                return null;
            }
            if (System.currentTimeMillis() > node.expiryTime) {
                lruList.remove(node);
                map.remove(key);
                expired.incrementAndGet();
                misses.incrementAndGet();
                return null;
            }
            lruList.moveToFront(node);
            hits.incrementAndGet();
            return node.value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void delete(K key) {
        lock.writeLock().lock();
        try {
            Node<K, V> node = map.remove(key);
            if (node != null) lruList.remove(node);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            map.clear();
            lruList.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Map<String, Object> getStats() {
        lock.readLock().lock();
        try {
            long total = hits.get() + misses.get();
            double hitRate = total == 0 ? 0 : (double) hits.get() / total;
            Map<String, Object> stats = new LinkedHashMap<>();
            stats.put("hits", hits.get());
            stats.put("misses", misses.get());
            stats.put("hit_rate", String.format("%.2f", hitRate));
            stats.put("total_requests", total);
            stats.put("current_size", map.size());
            stats.put("evictions", evictions.get());
            stats.put("expired_removals", expired.get());
            return stats;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void shutdown() {
        cleaner.shutdown();
    }

    private void startCleanup() {
        cleaner.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            lock.writeLock().lock();
            try {
                Iterator<Map.Entry<K, Node<K, V>>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    var entry = it.next();
                    if (entry.getValue().expiryTime < now) {
                        lruList.remove(entry.getValue());
                        it.remove();
                        expired.incrementAndGet();
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }, 5, 5, TimeUnit.SECONDS);
    }
}
