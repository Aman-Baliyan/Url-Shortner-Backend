package com.url.url_service.dtos;

import com.url.url_service.model.LongUrl;
import com.url.url_service.model.UrlModel;

import java.util.Queue;

public record UpdationData(Queue<LongUrl> queueForLong, Queue<UrlModel> queueForShort) {
}
