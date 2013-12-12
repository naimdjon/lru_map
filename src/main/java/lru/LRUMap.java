package lru;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU map backed by doubly-linked list.
 */
public class LRUMap<K, V> {
    private final Map<K, Node<K, V>> backingMap;
    private Node<K, V> eldest;
    private final int maxSize;

    public LRUMap() {
        this(10000);
    }

    public LRUMap(final int maxSize) {
        if (maxSize <= 0)
            throw new IllegalArgumentException("size should at least be 1");
        backingMap = new HashMap<>();
        this.maxSize = maxSize;
    }

    public synchronized V get(K key) {
        Node<K, V> node = backingMap.get(key);
        if (node != null) {
            addFront(node);
            return node.value;
        }
        return null;
    }

    public synchronized void put(K key, V value) {
        Node<K, V> node = new Node<>(null, key, value, null);
        backingMap.put(key, node);
        addFront(node);
        if (removeEldestEntry())
            remove(eldest.key);
    }

    public synchronized void remove(K key) {
        Node<K, V> node = backingMap.remove(key);
        if (node != null)
            removeNode(node);
    }

    private static class Node<K, V> {
        private Node<K, V> left, right;
        private K key;
        private V value;

        private Node(Node<K, V> left, K key, V value, Node<K, V> right) {
            this.left = left;
            this.key = key;
            this.value = value;
            this.right = right;
        }

        public String toString() {
            return value.toString();
        }
    }

    public V getFrontValue() {
        if(eldest==null)return null;
        return eldest.left.value;
    }

    private void addFront(Node<K, V> node) {
        removeNode(node);
        if (eldest == null) {
            node.left = node.right = node;
            eldest = node;
        } else {
            Node<K, V> back = eldest.left;
            back.right = node;
            node.left = back;
            eldest.left = node;
            node.right = eldest;
        }
    }

    private void removeNode(Node<K, V> node) {
        if(node==null)return;

        if (node.left != null) node.left.right = node.right;
        if (node.right != null) node.right.left = node.left;
        if (node == eldest) eldest = node.right;
    }

    private boolean removeEldestEntry() {
        return backingMap.size() > maxSize;
    }

    public String toString() {
        return backingMap.toString();
    }
}