package com.example.SlaveNode.BTree;


import java.io.Serializable;

import static com.example.SlaveNode.BTree.BTree.getMaxNodeChildrenNum;

class Node implements Serializable {
    protected int childrenNum;
    protected Entry[] children = new Entry[getMaxNodeChildrenNum()];

    public Node(int childrenNum) {
        this.childrenNum = childrenNum;
    }
    public Node(){}
}
