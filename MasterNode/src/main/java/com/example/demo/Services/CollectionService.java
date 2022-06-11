package com.example.demo.Services;


import com.example.demo.Schema.Collection;
import com.example.demo.Schema.IdAllocator;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


@Service
public class CollectionService {
    ArrayList<String> collections;

    public CollectionService() {
        collections = new ArrayList<>();
        File collectionsFolder = new File("Collections");

        for (File collection:collectionsFolder.listFiles()) {
            collections.add(collection.getName().split("\\.")[0]);
        }
    }

    public boolean createCollection(String collectionName) {
        File newCollection =  new File("Collections/"+collectionName+".json");
        if(newCollection.exists()) return false;
        else {
            try {
                newCollection.createNewFile();
                collections.add(collectionName);

                return true;
            } catch (IOException e) {
                return false;
            }
        }

    }

    public boolean deleteCollection(String collectionName) {
        File collection =  new File("Collections/"+collectionName+".json");
        return collection.delete();
    }

    public boolean addDocument(String collectionName, JSONObject json) throws IOException, ClassNotFoundException, ParseException {
        if(!collections.contains(collectionName)) return false;
        if(!Collection.validateSchema(collectionName,json)) return false;

        IdAllocator idAllocator = IdAllocator.getInstance();
        if(!idAllocator.addID(json)) return false;

        Collection.addDocument(collectionName,json);
        if(Index.getInstance().alreadyExists(collectionName,"_id")) Index.updateIndex(collectionName,"_id", (Integer) json.get("_id"));
        else Index.getInstance().createIndex(collectionName,"_id");
        return true;

    }

    public boolean createIndex(String collectionName, String field) {
        if(!collections.contains(collectionName)) return false;
        if(Index.getInstance().alreadyExists(collectionName,field)) return false;
        try {
            Index.getInstance().createIndex(collectionName,field);
            return true;
        } catch (IOException | ParseException e) {
            return false;
        }

    }
}
