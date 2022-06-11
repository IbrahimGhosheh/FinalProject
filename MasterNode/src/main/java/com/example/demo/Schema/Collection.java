package com.example.demo.Schema;

import com.example.demo.Libraries.JsonSchemaWriter;
import org.everit.json.schema.Schema;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.*;
import org.everit.json.schema.loader.SchemaLoader;

public class Collection {


    public static void addDocument(String collectionName, JSONObject json) throws IOException {
        FileWriter fileWriter = new FileWriter("Collections/"+collectionName+".json",true);
        fileWriter.write("\n"+json.toJSONString());
        fileWriter.flush();

    }

    public static boolean validateSchema(String collectionName, JSONObject json) throws IOException {
        File file = new File("Schemas/"+collectionName+"Schema.json");
        if(file.exists()){
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getPath()));
            StringBuilder stringBuilder = new StringBuilder();
            while(true){
                String line = bufferedReader.readLine();
                if(line==null) break;
                stringBuilder.append(line);
            }
            Schema schema = SchemaLoader.load(new org.json.JSONObject(stringBuilder.toString()));
            try{
                org.json.JSONObject newJSON = new org.json.JSONObject(json.toJSONString());
                schema.validate(newJSON);
                return true;
            }catch(Exception ex){
                return false;
            }
        }
        else{
            file.createNewFile();
            String schema = JsonSchemaWriter.getJsonSchema(json.toJSONString());
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getPath()));
            bufferedWriter.write(schema);
            bufferedWriter.flush();
            return true;
        }
    }
}
