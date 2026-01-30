package com.study.reservationqueue.endpoint;

import com.study.reservationqueue.dto.TokenDto;
import com.study.reservationqueue.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entry")
@RequiredArgsConstructor
public class EntryEndpoint {

    private final EntryService entryService;

    @PostMapping("")
    public TokenDto entryQueue() {

        return entryService.entryQueue();

    }

    @GetMapping("/check-status")
    public TokenDto getQueueRank(@RequestParam String token) {

        return entryService.getQueueRank(token);

    }

}
