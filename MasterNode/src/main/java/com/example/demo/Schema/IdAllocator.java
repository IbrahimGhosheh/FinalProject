package com.example.demo.Schema;

import org.json.simple.JSONObject;

public class IdAllocator {
    private int idCounter;
    private static IdAllocator idAllocator = new IdAllocator();

    private IdAllocator() {
        idCounter =0; //////fix
    }
    public static IdAllocator getInstance(){
        return idAllocator;
    }
    public boolean addID(JSONObject json){
        if(json.containsKey("_id")){
            int id = (int)json.get("_id");
            if(isValidId(id)) return true;
            else return false;
        }
        json.put("_id",idCounter++);
        return true;
    }

    private boolean isValidId(int id) {
        return true;
    }
}
