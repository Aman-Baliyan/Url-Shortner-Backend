package com.url.url_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Entity(name = "UrlTable")
public class UrlModel {

    public UrlModel() {
    }

    @Id
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public UrlModel(String shortKey, String longKey, String password, boolean oneTime, boolean isTimeSet, LocalDateTime expireAt) {
        this.shortKey = shortKey;
        this.longKey = longKey;
        this.password = password;
        this.oneTime = oneTime;
        this.isTimeSet = isTimeSet;
        this.expireAt = expireAt;
    }

    public UrlModel(String shortKey, String longKey, String password, boolean oneTime, boolean isTimeSet, LocalDateTime expireAt, LocalDateTime createdAt) {
        this.shortKey = shortKey;
        this.longKey = longKey;
        this.password = password;
        this.oneTime = oneTime;
        this.isTimeSet = isTimeSet;
        this.expireAt = expireAt;
        this.createdAt = createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isTimeSet() {
        return isTimeSet;
    }

    public void setTimeSet(boolean timeSet) {
        isTimeSet = timeSet;
    }

    private String longKey;

    private String password;

    private boolean oneTime;



    private boolean isTimeSet;

    private LocalDateTime expireAt;

    @CreationTimestamp
    private LocalDateTime createdAt;


}
