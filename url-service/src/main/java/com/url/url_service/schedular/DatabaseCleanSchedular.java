package com.url.url_service.schedular;
import com.url.url_service.repository.LongUrlRepo;
import com.url.url_service.repository.UrlRepo;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseCleanSchedular {

    @Autowired
    LongUrlRepo longUrlRepo;

    @Autowired
    UrlRepo urlRepo;

    @Scheduled(cron = "0 0 2 * * *")
    @SchedulerLock(
            name = "cleanupOldRecordsFromLongUrl",
            lockAtMostFor = "20m",
            lockAtLeastFor = "2m"
    )
    public void cleanupOldRecordsFromLongUrl() {

        List<String> list = List.of("ds0", "ds1");

        for (String shardId : list) {
            try (HintManager hintManager = HintManager.getInstance()) {
                hintManager.setDatabaseShardingValue(shardId);
                longUrlRepo.deleteExpiredUrl();
            }
        }
    }

    @Scheduled(cron = "0 0 2 * * *")
    @SchedulerLock(
            name = "cleanupOldRecordsFromShortUrl",
            lockAtMostFor = "20m",
            lockAtLeastFor = "2m"
    )
    public void cleanupOldRecordsFromShortUrl() {
        List<String> list = List.of("ds0", "ds1");
        for (String shardId : list) {
            try (HintManager hintManager = HintManager.getInstance()) {
                hintManager.setDatabaseShardingValue(shardId);
                urlRepo.deleteExpiredUrl();
            }
        }
    }

}
