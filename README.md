# Abishek_K_S_Cache

# 🧠 Thread-Safe In-Memory Cache System (Java)

A lightweight thread-safe in-memory caching system implemented in pure Java using the MVC pattern. Supports LRU eviction, TTL expiration, concurrency, and cache statistics.

---

## 🚀 How to Run

### Requirements:
- JDK 21+
- Maven 3.6+

### Setup & Build:
```bash
git clone https://github.com/your-username/thread-safe-cache.git
cd thread-safe-cache
mvn clean install
```

### Run the Test File:
mvn test


## Dependencies:

Java 21

JUnit 5 (Jupiter)

Maven Surefire Plugin

## Design Decisions:

O(1) operations using HashMap + Doubly Linked List.

Cache entries expire based on TTL timestamps.

LRU Eviction triggered when max size exceeds.

Auto background cleanup thread for expired entries.

## Concurrency Model:

Uses synchronized methods and fine-grained locking.

Read/write operations are thread-safe.

Cleanup thread runs independently.

## Eviction Logic:

Cache maintains access order via Doubly Linked List.

Oldest (least recently used) node is evicted when limit exceeds.


## Sample Stats Output:

![Output -1](https://github.com/user-attachments/assets/c1fca631-d016-44a9-9f0e-314565f863ac)


