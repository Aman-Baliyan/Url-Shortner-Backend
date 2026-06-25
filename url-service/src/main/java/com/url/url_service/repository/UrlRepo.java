package com.url.url_service.repository;

import com.url.url_service.model.UrlModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepo extends JpaRepository<UrlModel, String> {

    Optional<UrlModel> findByLongKeyAndPasswordIsNullAndOneTimeFalseAndIsTimeSetFalse(String longKey);

}
