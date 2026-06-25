package com.url.url_service.model;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(timeToLive = 300L)
public class RedisUrlData {

    @Id
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UrlModel getUrlModel() {
        return urlModel;
    }

    public void setUrlModel(UrlModel urlModel) {
        this.urlModel = urlModel;
    }

    UrlModel urlModel;

    public RedisUrlData(String id, UrlModel urlModel) {
        this.id = id;
        this.urlModel = urlModel;
    }
}
