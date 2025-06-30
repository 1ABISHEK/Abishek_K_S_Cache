
package com.sat.test;

import com.sat.model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    private Cache<String, String> cache;

    @BeforeEach
    void setup() {
        cache = new Cache<>(1000, 1); // 1 second TTL for testing
    }

    @Test
    void testBasicPutGet() {
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    void testExpiration() throws InterruptedException {
        cache.put("key2", "value2", 1000); // 1 second
        Thread.sleep(1500);
        assertNull(cache.get("key2"));
    }

    @Test
    void testEviction() {
        for (int i = 0; i < 1200; i++) {
            cache.put("data:" + i, "value_" + i);
        }
        int size = (int) cache.getStats().get("current_size");
        assertTrue(size <= 1000);
    }

    @Test
    void testConcurrency() throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                cache.put("thread:item_" + i, "value_" + i);
                cache.get("thread:item_" + (i / 2));
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start(); t2.start();
        t1.join(); t2.join();

        long total = (long) cache.getStats().get("total_requests");
        assertTrue(total > 0);
    }

    @Test
    void testDelete() {
        cache.put("temp", "123");
        cache.delete("temp");
        assertNull(cache.get("temp"));
    }
}
