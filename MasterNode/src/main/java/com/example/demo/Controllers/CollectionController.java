package com.example.demo.Controllers;

import com.example.demo.Services.CollectionService;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;


@Controller
public class CollectionController {

    @Autowired
    CollectionService collectionService;

    @GetMapping("/create/{collectionName}")
    public ResponseEntity<String> createCollection(@PathVariable String collectionName){
        if(collectionService.createCollection(collectionName))
            return new ResponseEntity<>("Collection Created Successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("ERROR: couldn't create Collection", HttpStatus.OK);
    }

    @GetMapping("/delete/{collectionName}")
    public ResponseEntity<String> deleteCollection(@PathVariable String collectionName){
        if(collectionService.deleteCollection(collectionName))
            return new ResponseEntity<>("Collection deleted Successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("ERROR: couldn't delete Collection", HttpStatus.OK);
    }
    @GetMapping("/add/{collectionName}")
    public ResponseEntity<String> addDocument(@PathVariable String collectionName, @RequestBody JSONObject json) throws IOException, ClassNotFoundException, ParseException {
        if(collectionService.addDocument(collectionName,json))
            return new ResponseEntity<>("Document added Successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("ERROR: couldn't add Document", HttpStatus.OK);
    }

    @GetMapping("/create-index/{collectionName}/{field}")
    public ResponseEntity<String> createIndex(@PathVariable String collectionName,@PathVariable String field){
        if(collectionService.createIndex(collectionName,field))
            return new ResponseEntity<>("Index Created Successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("ERROR: couldn't Create Index", HttpStatus.OK);
    }
}
