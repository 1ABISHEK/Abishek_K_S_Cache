
package com.sat.view;

import java.util.Map;
import java.util.Scanner;

public class ConsoleView {
    private final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        System.out.println("\n--- Cache Menu ---");
        System.out.println("1. Put (default TTL)");
        System.out.println("2. Put (custom TTL)");
        System.out.println("3. Get");
        System.out.println("4. Delete");
        System.out.println("5. Clear Cache");
        System.out.println("6. Show Stats");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    public int readChoice() {
        return Integer.parseInt(scanner.nextLine());
    }

    public String readLine(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine();
    }

    public long readLong(String prompt) {
        System.out.print(prompt + " ");
        return Long.parseLong(scanner.nextLine());
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showStats(Map<String, Object> stats) {
        System.out.println("\n--- Cache Stats ---");
        stats.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
