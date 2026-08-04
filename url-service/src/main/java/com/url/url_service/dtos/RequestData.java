package com.url.url_service.dtos;

import java.time.LocalDateTime;
import java.util.Date;

public record RequestData(String url, boolean oneTime, LocalDateTime expiryDate) {
}
