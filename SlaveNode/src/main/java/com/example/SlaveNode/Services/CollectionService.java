package com.example.SlaveNode.Services;

import com.example.SlaveNode.Cache.CacheController;
import com.example.SlaveNode.Index.Index;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
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

    public String getCollection(String collectionName) {
        if(!collections.contains(collectionName)) return "No Such Collection!";
        JSONParser jsonParser = new JSONParser();
        try(FileReader fileReader = new FileReader("Collections/"+collectionName+".json")){
            JSONArray collection = (JSONArray) jsonParser.parse(fileReader);
            return(collection.toJSONString());
        }catch(IOException | ParseException e){
            return "error reading collection";
        }
    }

    public String getDocument(String collectionName, String key, int value) {
        if(!collections.contains(collectionName)) return "No Such Collection!";
        if(CacheController.getCacheObject(collectionName+":"+key+"="+value)==null) {
            JSONParser jsonParser = new JSONParser();
            JSONArray collection;
            try (FileReader fileReader = new FileReader("Collections/" + collectionName + ".json")) {
                collection = (JSONArray) jsonParser.parse(fileReader);
            } catch (IOException | ParseException e) {
                return "error reading collection";
            }

            Index indexObject = Index.getInstance();
            if (indexObject.isIndexed(collectionName, key)) {
                int index = indexObject.getIndex(collectionName, key, value);
                JSONObject result = (JSONObject) collection.get(index);
                CacheController.add(collectionName+":"+key+"="+value,result);
                return collection.get(index).toString();
            } else {
                for (Object obj : collection) {
                    JSONObject json = (JSONObject) obj;
                    if ((Integer) json.get(key) == value){
                       CacheController.add(collectionName+":"+key+"="+value,json);
                       return json.toJSONString();
                    }
                }
                return "No Such Document";
            }
        }
        JSONObject result = (JSONObject) CacheController.getCacheObject(collectionName+":"+key+"="+value);
        return result.toJSONString();
    }
}
