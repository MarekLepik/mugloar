package com.lepik.mugloar.service;

import com.lepik.mugloar.bot.model.BotState;
import com.lepik.mugloar.bot.model.Decision;
import com.lepik.mugloar.model.Game;
import com.lepik.mugloar.model.Task;
import com.lepik.mugloar.model.TaskProbability;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TaskDecisionService {

    public Decision decide(BotState botState) {
        return decideTasksWithRiskAdjustment(botState);
    }

    private Decision decideTasksWithRiskAdjustment(BotState botState) {
        double risk = botState.getAcceptableRisk();

        while (risk >= TaskProbability.UNKNOWN.successRate()) {
            Optional<Task> task = decideBestTask(botState.getAvailableTasks(), risk);

            if (task.isPresent() && !taskIsBlacklisted(task.get(), botState)) {
                botState.setSelectedTask(task.get());
                return Decision.TASKS;
            }

            risk = findRiskier(risk).successRate();
        }

        return null;
    }

    private Optional<Task> decideBestTask(List<Task> tasks, double acceptableRisk) {
        return tasks.stream()
                .filter(task -> isAcceptableRisk(task, acceptableRisk))
                .max(Comparator.comparing(this::isPreferredTask)
                        .thenComparing(this::expectedValue));
    }

    private TaskProbability findRiskier(double currentRisk) {
        for (TaskProbability tp : TaskProbability.values()) {
            if (tp.successRate() < currentRisk) {
                return tp;
            }
        }
        return TaskProbability.UNKNOWN;
    }

    private boolean isAcceptableRisk(Task task, double acceptableRisk) {
        return task.getProbability() > acceptableRisk;
    }

    private boolean isPreferredTask(Task task) {
        String message = task.getMessage();
        if (message == null) return false;

        String lowerMessage = message.toLowerCase();
        return lowerMessage.startsWith("help") ||
                lowerMessage.startsWith("create") ||
                lowerMessage.startsWith("escort");
    }

    private double expectedValue(Task task) {
        return task.getRewardAmount() * task.getProbability();
    }

    private boolean taskIsBlacklisted(Task task, BotState botState) {
        return botState.getBlacklistedTaskIds().contains(task.getAdId());
    }
}
