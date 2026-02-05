package com.study.reservationqueue.service;

import com.study.reservationqueue.dto.TokenDto;
import com.study.reservationqueue.type.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntryService {


    private final RedisTemplate<String, String> redisTemplate;

    private static final Duration TOKEN_TTL = Duration.ofSeconds(10);

    private final int MAX_ACTIVE = 10;

    public TokenDto entryQueue() {

        String token = UUID.randomUUID().toString();

        log.info("Generated Token: {}", token);


        log.info("User with token {} is placed in the waiting queue.", token);

        redisTemplate.opsForValue().decrement("reservation:activeCount", 1);

        redisTemplate.opsForValue().set("reservation:token:" + token, UserStatus.WAIT.getStatus());


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


    private void promoteBatch(){


        String value = redisTemplate.opsForValue().get("reservation:activeCount");

        Long activeCount = value == null ? 0L : Long.parseLong(value);

        if(activeCount >= MAX_ACTIVE){
            log.info("Active reservation count {} has reached the maximum limit. No promotion from waiting queue.", activeCount);
            return;
        }

        Long promoteCount = MAX_ACTIVE - activeCount;

        ZSetOperations<String,String> zset = redisTemplate.opsForZSet();

        Set<ZSetOperations.TypedTuple<String>> candidates = zset.popMin("reservation:queue", promoteCount);

        if(candidates.isEmpty() || candidates.size() == 0){
            log.info("No candidates available for promotion from waiting queue.");
            return;
        }


        redisTemplate.opsForValue().increment("reservation:activeCount", candidates.size());

        candidates.forEach(candidate -> {

            String token = candidate.getValue();

            log.info("Promoting user with token {} from waiting queue to active reservation.", token);

            redisTemplate.opsForSet().add("reservation:entered", token);

            redisTemplate.opsForValue().set("reservation:token:" + token, UserStatus.ENTER.getStatus(), TOKEN_TTL);


        });


    }



    public void exit(String token){

        String status = redisTemplate.opsForValue().get("reservation:token:"+token);

        if(status == null || !status.equals(UserStatus.ENTER.getStatus())){
            log.info("Token {} is not in ENTER status. Exit operation aborted.", token);
            return;
        }

        redisTemplate.delete("reservation:token:"+token);
        redisTemplate.opsForSet().remove("reservation:entered", token);

        redisTemplate.opsForValue().decrement("reservation:activeCount", 1);

        log.info("User with token {} has exited the reservation system.", token);

        this.promoteBatch();



    }

    @Scheduled(fixedDelay = 5000)
    private void promoteScheduled(){
        promoteBatch();
    }

}
