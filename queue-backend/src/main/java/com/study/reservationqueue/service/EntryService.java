package com.study.reservationqueue.service;

import com.study.reservationqueue.dto.TokenDto;
import com.study.reservationqueue.type.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntryService {


    private final RedisTemplate<String, String> redisTemplate;

    private static final Duration TOKEN_TTL = Duration.ofMinutes(5);

    private final int MAX_ACTIVE = 1;

    public TokenDto entryQueue() {

        String token = UUID.randomUUID().toString();

        log.info("Generated Token: {}", token);

        Long activeCount = redisTemplate.opsForValue().increment("reservation:activeCount", 1);

        if (activeCount <= MAX_ACTIVE) {
            log.info("User with token {} has entered the reservation system.", token);
            redisTemplate.opsForSet().add("reservation:entered", token);

            redisTemplate.opsForValue().set("reservation:token:" + token, UserStatus.ENTER.getStatus(), TOKEN_TTL);

            TokenDto res = TokenDto.builder()
                    .token(token)
                    .status(UserStatus.ENTER.getStatus())
                    .build();

            return res;
        }

        log.info("User with token {} is placed in the waiting queue.", token);

        redisTemplate.opsForValue().decrement("reservation:activeCount", 1);

        redisTemplate.opsForValue().set("reservation:token:" + token, UserStatus.WAIT.getStatus(), TOKEN_TTL);


        ZSetOperations<String, String> sortedSet = redisTemplate.opsForZSet();

        sortedSet.add("reservation:queue", token, System.currentTimeMillis());

        Long rank = redisTemplate.opsForZSet().rank("reservation:queue", token);

        TokenDto res = TokenDto.builder()
                .token(token)
                .status(UserStatus.WAIT.getStatus())
                .rank(rank + 1)
                .build();

        return res;


    }

    public TokenDto getQueueRank(String token) {

        String status = redisTemplate.opsForValue().get("reservation:token:" + token);

        if(status == null){

            TokenDto res = TokenDto.builder()
                    .status(UserStatus.EXIT.getStatus())
                    .build();

            return res;

        }

        if(status.equals(UserStatus.WAIT.getStatus())){

            TokenDto res = TokenDto.builder()
                    .status(UserStatus.WAIT.getStatus())
                    .rank(redisTemplate.opsForZSet().rank("reservation:queue", token) + 1)
                    .build();
            return res;
        }

        if(status.equals(UserStatus.ENTER.getStatus())){
            TokenDto res = TokenDto.builder()
                    .status(UserStatus.ENTER.getStatus())
                    .build();
            return res;
        }


        return null;

    }

}
