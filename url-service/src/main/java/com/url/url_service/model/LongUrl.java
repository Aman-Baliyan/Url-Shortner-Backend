package com.url.url_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "url_mapping")
public class LongUrl {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "long_key")
    String longKey;

    String shortKey;

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public LongUrl(String longKey, String shortKey, LocalDateTime expireAt) {
        this.longKey = longKey;
        this.shortKey = shortKey;
        this.expireAt = expireAt;
    }

    public LocalDateTime expireAt;

    public LongUrl(String longKey, String shortKey) {
        this.longKey = longKey;
        this.shortKey = shortKey;
    }

    public LongUrl() {
    }

    public String getLongKey() {
        return longKey;
    }

    public void setLongKey(String longKey) {
        this.longKey = longKey;
    }

    public String getShortKey() {
        return shortKey;
    }

    public void setShortKey(String shortKey) {
        this.shortKey = shortKey;
    }
}
