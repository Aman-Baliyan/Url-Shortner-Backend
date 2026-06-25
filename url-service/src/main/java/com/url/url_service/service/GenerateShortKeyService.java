package com.url.url_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.url.url_service.dtos.RequestData;
import com.url.url_service.model.RedisUrlData;
import com.url.url_service.model.UrlModel;
import com.url.url_service.repository.RedisUrlRepo;
import com.url.url_service.repository.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GenerateShortKeyService {


    @Autowired
    RedisUrlRepo redisUrlRepo;

    @Autowired
    UrlRepo urlRepo;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RangeKeyGenerationService rangeKeyGenerationService;

    private static final String prefix = "url::";

    private static final String prefixForLongUrl = "actualurl::";

    public String generateShortUrl(RequestData requestData) throws Exception {

        try {
            LocalDateTime expireAt = requestData.expiryDate() != null ? requestData.expiryDate()
                    : LocalDateTime.now().plusDays(30);
            if (requestData.password() != null || requestData.oneTime() || requestData.expiryDate() != null) {
                long num = rangeKeyGenerationService.generateUniqueId();
                String shortKey = encodeUrl(num);

                // Addition to database and redis
                UrlModel urlData = urlRepo.save(new UrlModel(shortKey, requestData.url(), requestData.password(),
                                requestData.oneTime(), true, expireAt));
                redisUrlRepo.save(new RedisUrlData(prefix + shortKey, urlData));
                return shortKey;
            }
            else {
                RedisUrlData redisUrlData = redisUrlRepo.findById(prefixForLongUrl + requestData.url()).orElse(null);
                if(redisUrlData != null) {
                    return redisUrlData.getUrlModel().getShortKey();
                } else {
                    UrlModel optional =
                            urlRepo.findByLongKeyAndPasswordIsNullAndOneTimeFalseAndIsTimeSetFalse(requestData.url()).orElse(null);
                    if(optional == null) {
                        long num = rangeKeyGenerationService.generateUniqueId();
                        String shortKey = encodeUrl(num);
                        UrlModel urlData = urlRepo.save(new UrlModel(shortKey, requestData.url(), null,
                                false, false, expireAt));

                        redisUrlRepo.save(new RedisUrlData(prefixForLongUrl + requestData.url(), urlData));
                        redisUrlRepo.save(new RedisUrlData(prefix + shortKey, urlData));

                        return shortKey;
                    }
                    return optional.getShortKey();
                }
            }

        } catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String findUrl(String shortKey) throws Exception {

        try {
            RedisUrlData redisUrlData = redisUrlRepo.findById(prefix + shortKey).orElse(null);

            if(redisUrlData != null) {
                if(redisUrlData.getUrlModel().isOneTime()) {
                    redisUrlRepo.deleteById(prefix + shortKey);
                    deleteUrl(redisUrlData.getUrlModel().getShortKey());
                }
                return redisUrlData.getUrlModel().getLongKey();
            }
            else {
                Optional<UrlModel>  url = urlRepo.findById(shortKey);
                if(url.isEmpty()) return "Invalid shortKey";
                UrlModel urlModel = url.stream().toList().get(0);

                // store in redis
                if(!urlModel.isOneTime()) {
                    redisUrlRepo.save(new RedisUrlData(prefix + shortKey, urlModel));
                } else {
                    deleteUrl(shortKey);
                }

                return urlModel.getShortKey();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteUrl(String key) {
        urlRepo.deleteById(key);
    }

    private String encodeUrl(long num) {

        System.out.println("key is :" +num);

        String hash = "ab0c1d2e3f456789ghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int remainder = (int)(num % 62);
            sb.append(hash.charAt(remainder));
            num = num / 62L;
        }

        return sb.toString();
    }
}
