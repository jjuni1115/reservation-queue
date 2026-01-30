package com.study.reservationqueue.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    ENTER("ENTER"),
    WAIT("WAIT"),
    EXIT("EXIT");

    private String status;

}
