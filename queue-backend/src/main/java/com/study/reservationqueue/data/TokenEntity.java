package com.study.reservationqueue.data;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("token")

public class TokenEntity {

    @Id
    private String token;

    @TimeToLive
    private Long expiration;

}
