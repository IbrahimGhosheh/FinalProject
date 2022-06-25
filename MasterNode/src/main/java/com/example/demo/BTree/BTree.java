package com.example.demo.BTree;

import java.io.Serializable;

public class BTree<Key extends Comparable<Key>, Value> implements Serializable {
    private static final int MAX_NODE_CHILDREN_NUM = 4; // must be even and greater than 2

    private Node root;
    private int height;
    private int groupNum;



    public BTree() {
        root = new Node(0);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return groupNum;
    }


    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException();
        return gettingHelper(root, key, height);
    }

    private Value gettingHelper(Node nodeToSearch, Key key, int height) {
        Entry[] children = nodeToSearch.children;

        // external node
        if (height == 0) {
            for (int j = 0; j < nodeToSearch.childrenNum; j++) {
                if (equals(key, children[j].key)) return (Value) children[j].value;
            }
        }

        // internal node
        else {
            for (int j = 0; j < nodeToSearch.childrenNum; j++) {
                if (j + 1 == nodeToSearch.childrenNum || lessThan(key, children[j + 1].key))
                    return gettingHelper(children[j].next, key, height - 1);
            }
        }
        return null;
    }


    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("argument key to put() is null");
        Node insertNode = puttingHelper(root, key, val, height);
        groupNum++;
        if (insertNode == null) return;

        // need to split root
        Node temp = new Node(2);
        temp.children[0] = new Entry(root.children[0].key, null, root);
        temp.children[1] = new Entry(insertNode.children[0].key, null, insertNode);
        root = temp;
        height++;
    }

    private Node puttingHelper(Node nodeToInsert, Key key, Value value, int height) {
        int j;
        Entry tempEntry = new Entry(key, value, null);

        // external node
        if (height == 0) {
            for (j = 0; j < nodeToInsert.childrenNum; j++) {
                if (lessThan(key, nodeToInsert.children[j].key)) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < nodeToInsert.childrenNum; j++) {
                if ((j + 1 == nodeToInsert.childrenNum) || lessThan(key, nodeToInsert.children[j + 1].key)) {
                    Node tempNode = puttingHelper(nodeToInsert.children[j++].next, key, value, height - 1);
                    if (tempNode == null) return null;
                    tempEntry.key = tempNode.children[0].key;
                    tempEntry.value = null;
                    tempEntry.next = tempNode;
                    break;
                }
            }
        }

        for (int i = nodeToInsert.childrenNum; i > j; i--)
            nodeToInsert.children[i] = nodeToInsert.children[i - 1];
        nodeToInsert.children[j] = tempEntry;
        nodeToInsert.childrenNum++;
        if (nodeToInsert.childrenNum < MAX_NODE_CHILDREN_NUM) return null;
        else return split(nodeToInsert);

    }

    private Node split(Node nodeToSplit) {
        Node resultNode = new Node(MAX_NODE_CHILDREN_NUM / 2);
        nodeToSplit.childrenNum = MAX_NODE_CHILDREN_NUM / 2;
        for (int j = 0; j < MAX_NODE_CHILDREN_NUM / 2; j++)
            resultNode.children[j] = nodeToSplit.children[MAX_NODE_CHILDREN_NUM / 2 + j];
        return resultNode;
    }

    private boolean lessThan(Comparable firstKey, Comparable secondKey) {
        return firstKey.compareTo(secondKey) < 0;
    }

    private boolean equals(Comparable firstKey, Comparable secondKey) {
        return firstKey.compareTo(secondKey) == 0;
    }

    public static int getMaxNodeChildrenNum() {
        return MAX_NODE_CHILDREN_NUM;
    }

    @Override
    public String toString() {
        return toString(root, height) + "\n";
    }

    private String toString(Node rootNode, int height) {
        StringBuilder resultString = new StringBuilder();
        Entry[] children = rootNode.children;

        if (height == 0) {
            for (int i = 0; i < rootNode.childrenNum; i++)
                if (rootNode.childrenNum - i == 1)
                    resultString.append(children[i].value + "\n ");
                else
                    resultString.append( children[i].value + ",\n ");
        }else {
            for (int i = 0; i < rootNode.childrenNum; i++) {
                if (rootNode.childrenNum - i == 1)
                    resultString.append(toString(children[i].next, height - 1));
                else
                    resultString.append(toString(children[i].next, height - 1)+",");
            }
        }
        return resultString.toString();
    }
}