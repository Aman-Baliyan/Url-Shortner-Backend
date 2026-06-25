package com.url.url_service.repository;

import com.url.url_service.model.RedisUrlData;
import org.springframework.data.repository.CrudRepository;

public interface RedisUrlRepo extends CrudRepository<RedisUrlData, String> {
}
