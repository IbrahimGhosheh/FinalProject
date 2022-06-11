package com.example.SlaveNode.Cache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class LRUCache<Key, Value> {
    private final int MAX_CAPACITY;
    private AtomicInteger curSize = new AtomicInteger();
    private ConcurrentHashMap<Key, Node> map;
    private static Node head;
    private static Node tail;
    private static ReentrantReadWriteLock readWriteLock;



    public static class Node <K,V>{
        public K key;
        public V val;
        Node prev;
        Node next;

        public Node(K key, V value) {
            this.key = key;
            this.val = value;
        }
    }

    public LRUCache(int maxCapacity) {
        this.MAX_CAPACITY = maxCapacity;
        readWriteLock = new ReentrantReadWriteLock();
        map = new ConcurrentHashMap<>(maxCapacity);
        head = new Node(0, null);
        tail = new Node(0, null);
        head.next = tail;
        tail.prev = head;
        head.prev = null;
        tail.next = null;
    }

    public Value get(Key key) {
        if (!map.containsKey(key))
            return null;
        Node node = map.get(key);
        moveToHead(node);
        return (Value) node.val;

    }

    public void put(Key key, Value value) {
        Node curNode = map.get(key);
        if (curNode != null) {
            curNode.val = value;
            moveToHead(curNode);
            return;
        }
        Node newNode = new Node(key, value);
        map.put(key, newNode);
        addToHead(newNode);
        if (curSize.incrementAndGet() > MAX_CAPACITY) {
            Node nodeToRemove = tail.prev;
            removeNode(nodeToRemove);
            map.remove(nodeToRemove.key);
            curSize.decrementAndGet();
        }
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }

    private void removeNode(Node node) {
        try {
            readWriteLock.writeLock().lock();
            Node prev = node.prev;
            Node next = node.next;
            prev.next = next;
            next.prev = prev;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private void addToHead(Node node) {
        try {
            readWriteLock.writeLock().lock();
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }


    public void evict(Key key) {
        Node node = map.get(key);
        if (node != null) {
            removeNode(node);
            map.remove(key);
        }
    }

}



