package com.example.demo.Index;

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
            fileInputStream = new FileInputStream("DB/Indexes/index.out");
            objectInputStream = new ObjectInputStream(fileInputStream);
            indexes = (HashMap<String, HashMap<String, BTree>>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            indexes = new HashMap<>();
        }
    }

    public static Index getInstance() {
        return index;
    }

    public boolean alreadyExists(String collectionName, String field) {
       return indexes.get(collectionName).containsKey(field);
    }

    public void createIndex(String collectionName, String field) throws IOException, ParseException {
        BTree<Integer, Integer> newIndex = new BTree<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("DB/Collections/" + collectionName + ".json"));
        JSONParser jsonParser = new JSONParser();
        JSONArray collection = (JSONArray) jsonParser.parse(bufferedReader);
        for( Object json : collection){

        }
        int i = 0;
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) break;
            JSONObject json = new JSONObject(line);
            newIndex.put(json.getInt(field), i++);
        }
        if(indexes.containsKey(collectionName)){
            HashMap<String,BTree> x = indexes.get(collectionName);
            x.put(field,newIndex);
        }
        else{
            indexes.put(collectionName, (HashMap<String, BTree>) new HashMap<>().put(field,newIndex));
        }
        FileOutputStream fileOutputStream = new FileOutputStream("DB/Indexes/index.out");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(indexes);

    }

    public void updateIndex(String collectionName, String field,int value) throws IOException, ClassNotFoundException {
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader("DB/Collections/"+collectionName+".json"));
        int numOfDocuments = lineNumberReader.getLineNumber();

        indexes.get(collectionName).get(field).put(value,numOfDocuments);

        FileOutputStream fileOutputStream = new FileOutputStream("DB/Indexes/"+collectionName+ "-" + field + ".txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(indexes);
    }
}
