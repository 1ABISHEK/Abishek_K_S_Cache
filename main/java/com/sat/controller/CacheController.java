
package com.sat.controller;

import com.sat.model.Cache;
import com.sat.view.ConsoleView;



public class CacheController {
    private final Cache<String, String> cache;
    private final ConsoleView view;

    public CacheController(Cache<String, String> cache, ConsoleView view) {
        this.cache = cache;
        this.view = view;
    }

    public void start() {
        boolean running = true;
        while (running) {
            view.showMenu();
            int choice = view.readChoice();
            switch (choice) {
                case 1 -> {
                    String key = view.readLine("Enter key:");
                    String value = view.readLine("Enter value:");
                    cache.put(key, value);
                    view.showMessage("Inserted with default TTL.");
                }
                case 2 -> {
                    String key = view.readLine("Enter key:");
                    String value = view.readLine("Enter value:");
                    long ttl = view.readLong("Enter TTL in seconds:") * 1000;
                    cache.put(key, value, ttl);
                    view.showMessage("Inserted with custom TTL.");
                }
                case 3 -> {
                    String key = view.readLine("Enter key to retrieve:");
                    String val = cache.get(key);
                    view.showMessage("Value: " + (val == null ? "null (expired or not found)" : val));
                }
                case 4 -> {
                    String key = view.readLine("Enter key to delete:");
                    cache.delete(key);
                    view.showMessage("Deleted.");
                }
                case 5 -> {
                    cache.clear();
                    view.showMessage("Cache cleared.");
                }
                case 6 -> {
                    view.showStats(cache.getStats());
                }
                case 0 -> {
                    view.showMessage("Shutting down...");
                    cache.shutdown();
                    running = false;
                }
                default -> view.showMessage("Invalid choice. Try again.");
            }
        }
    }
}
