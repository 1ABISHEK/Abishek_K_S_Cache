package com.sat;

import com.sat.controller.CacheController;
import com.sat.model.Cache;
import com.sat.view.ConsoleView;


public class Main {
    public static void main(String[] args) {
        int maxSize = 1000;
        long defaultTTLSeconds = 300;

        Cache<String, String> cache = new Cache<>(maxSize, defaultTTLSeconds);
        ConsoleView view = new ConsoleView();
        CacheController controller = new CacheController(cache, view);
        controller.start();
    }
}
