package com.url.url_service.repository;

import com.url.url_service.model.UrlModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepo extends JpaRepository<UrlModel, String> {
    Optional<UrlModel> findByShortKey(String shortKey);

    @Query("DELETE FROM short_key_data u WHERE u.expireAt < CURRENT_TIMESTAMP")
    void deleteExpiredUrl();
}
