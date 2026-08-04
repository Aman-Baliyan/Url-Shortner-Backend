package com.url.url_service.repository;

import com.url.url_service.model.LongUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LongUrlRepo extends JpaRepository<LongUrl, String> {
    Optional<LongUrl> findByLongKey(String longUrl);


    @Query("DELETE FROM url_mapping l WHERE l.expireAt < CURRENT_TIMESTAMP")
    void deleteExpiredUrl();
}
