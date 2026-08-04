package com.url.url_service.config;

import com.url.url_service.repository.LongUrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    LongUrlRepo longUrlRepo;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
    public void deleteExpiredUrl() {

    }
}
