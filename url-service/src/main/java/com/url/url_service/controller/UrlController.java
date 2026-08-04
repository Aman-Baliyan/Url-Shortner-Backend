package com.url.url_service.controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.url.url_service.dtos.RequestData;
import com.url.url_service.dtos.UrlResponse;
import com.url.url_service.service.GenerateShortKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
//@RequestMapping("/")
public class UrlController {

    @Value("origin.value")
    String pre;

    @Autowired
    GenerateShortKeyService generateShortKeyService;

    @PostMapping("/generate")
    public ResponseEntity<UrlResponse> generateUrl(@RequestBody RequestData requestData){
        try{
            String shortKey = generateShortKeyService.generateShortUrl(requestData);
            return ResponseEntity
                        .ok(new UrlResponse("ok", pre + 't' +shortKey));
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new UrlResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/t/{shortUrl}")
    public ResponseEntity<String> findUrl(@PathVariable("shortUrl") String shortUrl) {
        try {
            String url = generateShortKeyService.findUrl(shortUrl);
//            if (url.equals("Password required")) {
//                // no code
//            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url))
                    .build();

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/upload/csv")
    public ResponseEntity<ByteArrayResource> parseCSV(@RequestParam MultipartFile file) throws Exception {
        return generateShortKeyService.handleCsv(file);
    }
}
