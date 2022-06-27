package com.example.SlaveNode.Index;

import java.io.*;
import java.util.HashMap;
import com.example.SlaveNode.BTree.BTree;

public class Index {
    private HashMap<String,HashMap<String, BTree>> indexes;
    private static Index index = new Index();

    public Index() {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        try {
            fileInputStream = new FileInputStream("DB/Indexes/index.out");
            objectInputStream = new ObjectInputStream(fileInputStream);
            indexes = (HashMap<String, HashMap<String, BTree>>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Index getInstance() {
        return index;
    }
    public static void updateIndexing(){index = new Index();}

    public  boolean isIndexed(String collectionName, String key) {
        return indexes.get(collectionName).containsKey(key);
    }

    public int getIndex(String collectionName, String key, int value) {
        return (int) indexes.get(collectionName).get(key).get(value);
    }
}
