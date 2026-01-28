package com.lepik.mugloar.controller;

import com.lepik.mugloar.service.BotRunnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bot")
public class BotController {

    private final BotRunnerService botRunnerService;

    public BotController(BotRunnerService botRunnerService) {
        this.botRunnerService = botRunnerService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startBot(
            @RequestParam(defaultValue = "1000") int scoreLimit) {

        if (scoreLimit <= 0) {
            return ResponseEntity.badRequest().body("Score limit must be positive");
        }

        boolean started = botRunnerService.start(scoreLimit);
        if (!started) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Bot is already running");
        }

        return ResponseEntity.ok("Bot started playing");
    }
}

