package com.url.url_service.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Entity(name = "short_key_data")
public class UrlModel {

    public UrlModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "short_key")
    private String shortKey;

    public String getShortKey() {
        return shortKey;
    }

    public void setShortKey(String shortKey) {
        this.shortKey = shortKey;
    }

    public String getLongKey() {
        return longKey;
    }

    public void setLongKey(String longKey) {
        this.longKey = longKey;
    }



    public boolean isOneTime() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UrlModel(String shortKey, String longKey, boolean oneTime,LocalDateTime expireAt) {
        this.shortKey = shortKey;
        this.longKey = longKey;
        this.oneTime = oneTime;
        this.expireAt = expireAt;
    }

    public UrlModel(String shortKey, String longKey, boolean oneTime, LocalDateTime expireAt, LocalDateTime createdAt) {
        this.shortKey = shortKey;
        this.longKey = longKey;
        this.oneTime = oneTime;
        this.expireAt = expireAt;
        this.createdAt = createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }



    private String longKey;

    private boolean oneTime;

    private LocalDateTime expireAt;

    @CreationTimestamp
    private LocalDateTime createdAt;


}
