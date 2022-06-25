package com.example.demo.Services;

import com.example.demo.BTree.BTree;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;

public class Index {

    private HashMap<String, HashMap<String,BTree>> indexes;
    private static Index index = new Index();

    private Index() {
        FileInputStream fileInputStream ;
        ObjectInputStream objectInputStream ;
        try {
            fileInputStream = new FileInputStream("index.out");
            objectInputStream = new ObjectInputStream(fileInputStream);
            indexes = (HashMap<String, HashMap<String, BTree>>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Index getInstance() {
        return index;
    }

    public boolean alreadyExists(String collectionName, String field) {
       return indexes.get(collectionName).containsKey(field);
    }

    public void createIndex(String collectionName, String field) throws IOException, ParseException {
        BTree<Integer, Integer> index = new BTree<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("Collections/" + collectionName + ".json"));
        JSONParser jsonParser = new JSONParser();
        JSONArray collection = (JSONArray) jsonParser.parse(bufferedReader);
        for( Object json : collection){

        }
        int i = 0;
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) break;
            JSONObject json = new JSONObject(line);
            index.put(json.getInt(field), i++);
        }
        FileOutputStream fileOutputStream = new FileOutputStream("Indexes/" + collectionName + "-" + field + ".txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(index);

    }

    public static void updateIndex(String collectionName, String field,int value) throws IOException, ClassNotFoundException {
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader("Collections/"+collectionName+".json"));
        int numOfDocuments = lineNumberReader.getLineNumber();

        FileInputStream fileInputStream = new FileInputStream("Indexes/"+collectionName+ "-" + field + ".txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        BTree<Integer,Integer> index = (BTree<Integer, Integer>) objectInputStream.readObject();
        index.put(value,numOfDocuments);
        FileOutputStream fileOutputStream = new FileOutputStream("Indexes/"+collectionName+ "-" + field + ".txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(index);
    }
}
