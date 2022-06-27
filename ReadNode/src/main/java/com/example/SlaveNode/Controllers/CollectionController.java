package com.example.SlaveNode.Controllers;



import com.example.SlaveNode.Cache.CacheController;
import com.example.SlaveNode.Index.Index;
import com.example.SlaveNode.Services.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/read")
public class CollectionController {
    @Autowired
    CollectionService collectionService;

    @GetMapping("/collection/{collectionName}")
    public ResponseEntity<String> getCollection(@PathVariable String collectionName){
        String response = collectionService.getCollection(collectionName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/document/{collectionName}")
    public ResponseEntity<String> getDocument(@PathVariable String collectionName , @RequestParam String key, @RequestParam int value){
        String response = collectionService.getDocument(collectionName,key,value);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/update")
    public void update(){
        CacheController.clearCache();
        Index.updateIndexing();
        System.out.println("updated successfully");
    }
}
