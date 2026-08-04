package com.url.url_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import com.url.url_service.dtos.RequestData;
import com.url.url_service.dtos.UpdationData;
import com.url.url_service.model.LongUrl;
import com.url.url_service.model.RedisUrlData;
import com.url.url_service.model.UrlModel;
import com.url.url_service.repository.LongUrlRepo;
import com.url.url_service.repository.RedisUrlRepo;
import com.url.url_service.repository.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.*;

@Service
public class GenerateShortKeyService {


    @Autowired
    RedisUrlRepo redisUrlRepo;

    @Autowired
    LongUrlRepo longUrlRepo;

    @Autowired
    UrlRepo urlRepo;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RangeKeyGenerationService rangeKeyGenerationService;

    @Autowired
    BackgroundService backgroundService;

    private static final String prefix = "url::";

    private static final String prefixForLongUrl = "actualurl::";

    private static final String prefixForPass = "pass::";


    public String generateShortUrl(RequestData requestData) throws Exception {

        try {
            LocalDateTime expireAt = requestData.expiryDate() != null ? requestData.expiryDate()
                    : LocalDateTime.now().plusDays(30);
            if (requestData.oneTime() || requestData.expiryDate() != null) {
                long num = rangeKeyGenerationService.generateUniqueId();
                String shortKey = encodeUrl(num);

                // Addition to database and redis
                UrlModel urlData = urlRepo.save(new UrlModel(shortKey, requestData.url(),
                                requestData.oneTime(), expireAt));
                redisUrlRepo.save(new RedisUrlData(prefix + shortKey, urlData));
                return shortKey;
            }
            else {
                RedisUrlData redisUrlData = redisUrlRepo.findById(prefixForLongUrl + requestData.url()).orElse(null);
                if(redisUrlData != null) {
                    return redisUrlData.getUrlModel().getShortKey();
                } else {
                    LongUrl longUrl = longUrlRepo.findByLongKey(requestData.url()).orElse(null);

                    if(longUrl == null) {
                        long num = rangeKeyGenerationService.generateUniqueId();
                        String shortKey = encodeUrl(num);
                        UrlModel urlData = urlRepo.save(new UrlModel(shortKey, requestData.url(),
                                false, expireAt));
                        redisUrlRepo.save(new RedisUrlData(prefix + shortKey, urlData));
                        backgroundService.addData(new LongUrl(requestData.url(), shortKey, LocalDateTime.now().plusDays(30)),
                                new RedisUrlData(prefixForLongUrl + requestData.url(), urlData));
                        return shortKey;
                    }
                    else {
                        if (longUrl.getExpireAt().isBefore(LocalDateTime.now().plusDays(30))) {
                            longUrl.setExpireAt(LocalDateTime.now().plusDays(30));
                            backgroundService.updateData(longUrl);
                        }

                    }
                    return longUrl.getShortKey();
                }
            }

        } catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String findUrl(String shortKey) throws Exception {

        try {
            RedisUrlData redisUrlData = redisUrlRepo.findById(prefix + shortKey).orElse(null);
            if(redisUrlData != null) {
                if (redisUrlData.getUrlModel().getExpireAt().isBefore(LocalDateTime.now())) {
                    throw new RuntimeException("Invalid url");
                }
                if(redisUrlData.getUrlModel().isOneTime()) {
                    redisUrlRepo.deleteById(prefix + shortKey);
                    UrlModel updatedUrlModel = redisUrlData.getUrlModel();
                    updatedUrlModel.setExpireAt(LocalDateTime.now().minusDays(1));
                    urlRepo.save(updatedUrlModel);
                }
//                if (redisUrlData.getUrlModel().getPassword() != null) {
//                    redisUrlRepo.save(new RedisUrlData(prefixForPass + shortKey, redisUrlData.getUrlModel()));
//                    return "Password required";
//                }
                return redisUrlData.getUrlModel().getLongKey();
            }
            else {
                UrlModel  urlModel = urlRepo.findByShortKey(shortKey).orElse(null);
                if(urlModel == null) throw new RuntimeException("Invalid url");

                if(urlModel.getExpireAt().isBefore(LocalDateTime.now())) {
                    throw new RuntimeException("Invalid url");
                }
                // store in redis
                if(!urlModel.isOneTime()) {
                    redisUrlRepo.save(new RedisUrlData(prefix + shortKey, urlModel));
                } else {
                    urlModel.setExpireAt(LocalDateTime.now().minusDays(1));
                    urlRepo.save(urlModel);
                }

                return urlModel.getLongKey();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ResponseEntity<ByteArrayResource> handleCsv(MultipartFile file) throws Exception {
        Reader reader = new InputStreamReader(file.getInputStream());

        // Parse CSV data
        CSVReader csvReader = new CSVReaderBuilder(reader).build();
        List<String[]> rows = csvReader.readAll();
        Set<String> uniqueUrls = new HashSet<>();
        List<String[]> answer = new ArrayList<>();
        for (String[] row : rows) {
            uniqueUrls.add(row[0]);
        }
        for (String key : uniqueUrls) {
            try {
                String shortKey = generateShortUrl(new RequestData(key, false, null));
                answer.add(new String[]{key, shortKey});
            } catch (Exception e) {
                answer.add(new String[]{key, "Error in generation"});
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (Writer streamWriter = new OutputStreamWriter(outputStream);
             ICSVWriter csvWriter = new CSVWriterBuilder(streamWriter)
                     .withSeparator('\t') // Using Tab separator as requested
                     .build()) {

            csvWriter.writeAll(answer);
        }

        // 4. Wrap byte array into a Spring Resource
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        // 5. Return HTTP Response with proper download headers
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"shortened_urls.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(resource.contentLength())
                .body(resource);
    }

    public void deleteUrl(String key) {
        urlRepo.deleteById(key);
    }

    private String encodeUrl(long num) {

        System.out.println("key is :" +num);

        String hash = "ab0c1d2e3f456789ghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int remainder = (int)(num % 62);
            sb.append(hash.charAt(remainder));
            num = num / 62L;
        }

        return sb.toString();
    }
}
