
package com.sat.model;

public class Node<K, V> {
    public K key;
    public V value;
    public long expiryTime;
    public Node<K, V> prev, next;

    public Node(K key, V value, long expiryTime) {
        this.key = key;
        this.value = value;
        this.expiryTime = expiryTime;
    }
}
