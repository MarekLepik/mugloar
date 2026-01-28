package com.lepik.mugloar.service;

import com.lepik.mugloar.bot.GameBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BotRunnerService {

    private static final Logger log = LoggerFactory.getLogger(BotRunnerService.class);

    private final GameBot gameBot;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public BotRunnerService(GameBot gameBot) {
        this.gameBot = gameBot;
    }

    public boolean start(int scoreLimit) {
        if (!isRunning.compareAndSet(false, true)) {
            return false;
        }

        executor.submit(() -> {
            try {
                log.info("Bot started with score limit {}", scoreLimit);
                gameBot.playAutomatically(scoreLimit);
                log.info("Bot finished execution");
            } catch (Exception e) {
                log.error("Exception during bot execution", e);
            } finally {
                isRunning.set(false);
            }
        });

        return true;
    }

    public boolean isRunning() {
        return isRunning.get();
    }
}
