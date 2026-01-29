package com.study.reservationqueue.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntryService {


    private final RedisTemplate<String,String> redisTemplate;

    public void entryQueue() {

        String token = UUID.randomUUID().toString();

        log.info("Generated Token: {}", token);

        ZSetOperations<String,String> sortedSet = redisTemplate.opsForZSet();

        sortedSet.add("reservationQueue",token,System.currentTimeMillis());





    }

}
