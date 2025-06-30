
package com.sat.model;

public class DoublyLinkedList<K, V> {
    private Node<K, V> head, tail;

    public void addFirst(Node<K, V> node) {
        node.prev = null;
        node.next = head;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = node;
    }

    public void moveToFront(Node<K, V> node) {
        if (node == head) return;
        remove(node);
        addFirst(node);
    }

    public void remove(Node<K, V> node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;
        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;
    }

    public Node<K, V> removeLast() {
        if (tail == null) return null;
        Node<K, V> node = tail;
        remove(tail);
        return node;
    }

    public void clear() {
        head = tail = null;
    }
}
