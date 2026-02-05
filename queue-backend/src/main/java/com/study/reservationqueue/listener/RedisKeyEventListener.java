package com.study.reservationqueue.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyEventListener extends KeyExpirationEventMessageListener {

    private final RedisTemplate<String,String> redisTemplate;

    public RedisKeyEventListener(RedisMessageListenerContainer listenerContainer, RedisTemplate<String, String> redisTemplate) {
        super(listenerContainer);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if(!expiredKey.startsWith("reservation:token")){
            return;
        }
        String token = expiredKey.replace("reservation:token:","");

        redisTemplate.opsForSet().remove("reservation:entered",token);
        redisTemplate.opsForValue().decrement("reservation:activeCount",1);
        redisTemplate.opsForZSet().remove("reservation:queue",token);


    }
}
