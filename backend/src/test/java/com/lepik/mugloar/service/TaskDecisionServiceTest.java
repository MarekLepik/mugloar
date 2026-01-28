package com.lepik.mugloar.service;

import com.lepik.mugloar.bot.model.BotState;
import com.lepik.mugloar.model.Task;
import com.lepik.mugloar.model.TaskProbability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskDecisionServiceTest {

    private TaskDecisionService taskDecisionService;
    private BotState botState;

    @BeforeEach
    void setUp() {
        taskDecisionService = new TaskDecisionService();
        botState = new BotState();
        botState.setAcceptableRisk(TaskProbability.QUITE_LIKELY.successRate());
        Task task = new Task();
        task.setMessage("Test Task");
        task.setExpiresIn(1);
        task.setAdId("123");
        task.setRewardAmount(100);
        task.setProbability(TaskProbability.QUITE_LIKELY.successRate());
        botState.setAvailableTasks(java.util.List.of(task));
        botState.setAvailableTasks(List.of(task));
    }

    @Test
    void decide_on_task_and_add_to_selected_task() {
        taskDecisionService.decide(botState);
        assertNotNull(botState.getSelectedTask());
        assertEquals("123", botState.getSelectedTask().getAdId());
    }

    @Test
    void decide_simpler_task_when_multiple_options() {
        Task easyTask = new Task();
        easyTask.setMessage("Easy Task");
        easyTask.setExpiresIn(1);
        easyTask.setAdId("easy123");
        easyTask.setRewardAmount(50);
        easyTask.setProbability(TaskProbability.WALK_IN_THE_PARK.successRate());

        Task hardTask = new Task();
        hardTask.setMessage("Hard Task");
        hardTask.setExpiresIn(1);
        hardTask.setAdId("hard123");
        hardTask.setRewardAmount(200);
        hardTask.setProbability(TaskProbability.RISKY.successRate());

        botState.setAvailableTasks(List.of(easyTask, hardTask));

        taskDecisionService.decide(botState);
        assertNotNull(botState.getSelectedTask());
        assertEquals("easy123", botState.getSelectedTask().getAdId());
    }

    @Test
    void select_task_that_is_not_blacklisted() {
        Task task1 = new Task();
        task1.setMessage("Task 1");
        task1.setExpiresIn(1);
        task1.setAdId("task1");
        task1.setRewardAmount(100);
        task1.setProbability(TaskProbability.QUITE_LIKELY.successRate());

        Task task2 = new Task();
        task2.setMessage("Task 2");
        task2.setExpiresIn(1);
        task2.setAdId("task2");
        task2.setRewardAmount(150);
        task2.setProbability(TaskProbability.QUITE_LIKELY.successRate());

        botState.setAvailableTasks(List.of(task1, task2));
        botState.getBlacklistedTaskIds().add("task1");

        taskDecisionService.decide(botState);
        assertNotNull(botState.getSelectedTask());
        assertEquals("task2", botState.getSelectedTask().getAdId());
    }

    @Test
    void select_task_with_preferred_prefix() {
        Task helpTask = new Task();
        helpTask.setMessage("Help needed");
        helpTask.setExpiresIn(1);
        helpTask.setAdId("help123");
        helpTask.setRewardAmount(100);
        helpTask.setProbability(TaskProbability.QUITE_LIKELY.successRate());

        Task otherTask = new Task();
        otherTask.setMessage("Other task");
        otherTask.setExpiresIn(1);
        otherTask.setAdId("other123");
        otherTask.setRewardAmount(150);
        otherTask.setProbability(TaskProbability.QUITE_LIKELY.successRate());

        botState.setAvailableTasks(List.of(otherTask, helpTask));

        taskDecisionService.decide(botState);
        assertNotNull(botState.getSelectedTask());
        assertEquals("help123", botState.getSelectedTask().getAdId());
    }
}