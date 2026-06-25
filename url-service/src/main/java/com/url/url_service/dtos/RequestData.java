package com.url.url_service.dtos;

import java.time.LocalDateTime;
import java.util.Date;

public record RequestData(String url, String password, boolean oneTime, LocalDateTime expiryDate) {
}
