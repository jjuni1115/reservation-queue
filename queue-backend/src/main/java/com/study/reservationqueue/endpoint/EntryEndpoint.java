package com.study.reservationqueue.endpoint;

import com.study.reservationqueue.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entry")
@RequiredArgsConstructor
public class EntryEndpoint {

    private final EntryService entryService;

    @PostMapping("")
    public void entryQueue() {

        entryService.entryQueue();

    }

}
