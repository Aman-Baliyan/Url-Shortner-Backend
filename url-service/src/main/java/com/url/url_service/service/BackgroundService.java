package com.url.url_service.service;

import com.url.url_service.model.LongUrl;
import com.url.url_service.model.RedisUrlData;
import com.url.url_service.repository.LongUrlRepo;
import com.url.url_service.repository.RedisUrlRepo;
import com.url.url_service.repository.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BackgroundService {

    @Autowired
    LongUrlRepo longUrlRepo;

    @Autowired
    UrlRepo urlRepo;

    @Autowired
    RedisUrlRepo redisUrlRepo;

    @Async("urlBackgroundExecutor")
    public void addData(LongUrl longUrl, RedisUrlData longKey) {
        try {
            longUrlRepo.save(longUrl);
            redisUrlRepo.save(longKey);
        } catch(Exception e) {
//            System.out.println("Error in background task");
        }

    }

    @Async("urlBackgroundExecutor")
    public void updateData(LongUrl longUrl) {
        try {
            longUrlRepo.save(longUrl);
        } catch(Exception e) {
//            System.out.println("Error in background task");
        }

    }

}
