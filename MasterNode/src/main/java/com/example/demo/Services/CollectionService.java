package com.example.demo.Services;


import com.example.demo.Index.Index;
import com.example.demo.Schema.Collection;
import com.example.demo.Schema.IdAllocator;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class CollectionService {
    private ArrayList<String> collections;
    @Autowired
    private EurekaClient readNodes;

    public CollectionService() {
        collections = new ArrayList<>();
        File collectionsFolder = new File("DB/Collections");


        for (File collection:collectionsFolder.listFiles()) {
            collections.add(collection.getName().split("\\.")[0]);
        }
    }

    public boolean createCollection(String collectionName) {
        File newCollection =  new File("Db/Collections/"+collectionName+".json");
        if(newCollection.exists()) return false;
        else {
            try {
                newCollection.createNewFile();
                collections.add(collectionName);
                updateAll();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

    }

    public boolean deleteCollection(String collectionName) {
        File collection =  new File("DB/Collections/"+collectionName+".json");
        if(collection.delete()){
            updateAll();
            return true;
        }
        return false;
    }

    public boolean addDocument(String collectionName, JSONObject json) throws IOException, ClassNotFoundException, ParseException {
        if(!collections.contains(collectionName)) return false;
        if(!Collection.validateSchema(collectionName,json)) return false;

        IdAllocator idAllocator = IdAllocator.getInstance();
        if(!idAllocator.addID(json)) return false;

        Collection.addDocument(collectionName,json);
        if(Index.getInstance().alreadyExists(collectionName,"_id")) Index.getInstance().updateIndex(collectionName,"_id", (Integer) json.get("_id"));
        else Index.getInstance().createIndex(collectionName,"_id");
        updateAll();
        return true;

    }

    public boolean createIndex(String collectionName, String field) {
        if(!collections.contains(collectionName)) return false;
        if(Index.getInstance().alreadyExists(collectionName,field)) return false;
        try {
            Index.getInstance().createIndex(collectionName,field);
            updateAll();
            return true;
        } catch (IOException | ParseException e) {
            return false;
        }

    }

    private void updateAll() {
        List<InstanceInfo> replicas = readNodes.getApplication("Read-Node").getInstances();

        for(InstanceInfo replica:replicas){
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(replica.getHomePageUrl() + "read/update", String.class);
        }
    }
}
