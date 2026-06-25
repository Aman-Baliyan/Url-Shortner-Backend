package com.url.url_service.controller;

import com.url.url_service.dtos.RequestData;
import com.url.url_service.service.GenerateShortKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
//@RequestMapping("/")
public class UrlController {

    @Autowired
    GenerateShortKeyService generateShortKeyService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateUrl(@RequestBody RequestData requestData){
        try{
            return ResponseEntity
                        .ok(generateShortKeyService.generateShortUrl(requestData));
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }


    @GetMapping("/getUrl/{shortUrl}")
    public ResponseEntity<String> findUrl(@PathVariable("shortUrl") String shortUrl) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(generateShortKeyService.findUrl(shortUrl)))
                    .build();

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
