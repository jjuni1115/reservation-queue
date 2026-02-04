package com.study.reservationqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReservationQueueApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationQueueApplication.class, args);
    }

}
