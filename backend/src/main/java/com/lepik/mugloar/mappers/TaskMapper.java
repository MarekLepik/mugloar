package com.lepik.mugloar.mappers;

import com.lepik.mugloar.client.dto.TaskResponse;
import com.lepik.mugloar.model.Task;
import com.lepik.mugloar.model.TaskProbability;
import com.lepik.mugloar.service.TaskDecryptionService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TaskMapper {

    private final TaskDecryptionService decryptionService;

    public TaskMapper(TaskDecryptionService decryptionService) {
        this.decryptionService = decryptionService;
    }

    public List<Task> toDomainList(TaskResponse[] taskResponses) {
        return Arrays.stream(taskResponses)
                .map(decryptionService::decrypt)
                .map(this::toDomain)
                .toList();
    }

    private Task toDomain(TaskResponse taskResponse) {
        return new Task(
                taskResponse.getAdId(),
                taskResponse.getMessage(),
                parseReward(taskResponse.getReward()),
                taskResponse.getExpiresIn(),
                TaskProbability.fromApiValue(taskResponse.getProbability()).successRate()
        );
    }

    private int parseReward(String reward) {
        if (reward == null) {
            return 0;
        }

        String digits = reward.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(digits);
    }
}
