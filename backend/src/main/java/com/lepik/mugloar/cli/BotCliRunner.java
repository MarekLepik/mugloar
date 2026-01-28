package com.lepik.mugloar.cli;

import com.lepik.mugloar.service.BotRunnerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BotCliRunner implements CommandLineRunner {

    private final BotRunnerService botRunnerService;

    public BotCliRunner(BotRunnerService botRunnerService) {
        this.botRunnerService = botRunnerService;
    }

    @Override
    public void run(String... args) {
        if (args.length == 0 || !"start".equals(args[0])) {
            return;
        }

        int scoreLimit = 1000;
        if (args.length > 1) {
            scoreLimit = Integer.parseInt(args[1]);
        }

        botRunnerService.start(scoreLimit);
    }
}
