package com.example.SlaveNode.BTree;

import java.io.Serializable;

class Entry implements Serializable {
    protected Comparable key;
    protected Object value;
    protected Node next;     // helper field to iterate over array entries
    public Entry(Comparable key, Object value, Node next) {
        this.key  = key;
        this.value = value;
        this.next = next;
    }
    public Entry(){}
}
