package com.lepik.mugloar.service;

import com.lepik.mugloar.bot.model.BotState;
import com.lepik.mugloar.bot.model.Decision;
import com.lepik.mugloar.model.Item;
import com.lepik.mugloar.model.Task;
import com.lepik.mugloar.model.TaskProbability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BotDecisionServiceTest {

    @Mock
    private TaskDecisionService taskDecisionService;

    @Mock
    private ShopDecisionService shopDecisionService;

    private BotDecisionService botDecisionService;
    private BotState botState;

    @BeforeEach
    void setUp() {
        botDecisionService = new BotDecisionService(taskDecisionService, shopDecisionService);
        botState = new BotState();
        botState.setLives(4);
        botState.setGold(100);

        Task task = new Task();
        task.setMessage("Test Task");
        task.setExpiresIn(1);
        task.setAdId("123");
        task.setRewardAmount(100);
        task.setProbability(TaskProbability.WALK_IN_THE_PARK.successRate());
        botState.setAvailableTasks(List.of(task));
        botState.setSelectedTask(task);

        Item shopItem = new Item();
        shopItem.setId("hpot");
        shopItem.setCost(50);
        shopItem.setName("healing potion");
        botState.setAvailableItems(List.of(shopItem));
    }

    @Test
    void decide_shop_over_task_when_healing_needed() {
        botState.setLives(3);

        Item healingPotion = new Item();
        healingPotion.setId("hpot");
        botState.setDesiredItem(healingPotion);

        when(taskDecisionService.decide(botState)).thenReturn(Decision.TASKS);
        when(shopDecisionService.decide(botState)).thenReturn(Optional.of(Decision.BUY_ITEM));

        var decision = botDecisionService.decide(botState);

        assertEquals(Decision.BUY_ITEM, decision);
    }

    @Test
    void decide_task_when_task_is_simple_and_healing_not_needed() {
        botState.setLives(4);

        when(taskDecisionService.decide(botState)).thenReturn(Decision.TASKS);
        when(shopDecisionService.decide(botState)).thenReturn(Optional.of(Decision.BUY_ITEM));

        var decision = botDecisionService.decide(botState);

        assertEquals(Decision.TASKS, decision);
    }

    @Test
    void decide_shop_item_if_healing_not_needed_and_task_not_simple() {
        botState.setLives(4);
        Task task = new Task();
        task.setMessage("Test Task");
        task.setExpiresIn(1);
        task.setAdId("123");
        task.setRewardAmount(100);
        task.setProbability(TaskProbability.PLAYING_WITH_FIRE.successRate());
        botState.setAvailableTasks(List.of(task));
        botState.setSelectedTask(task);

        when(taskDecisionService.decide(botState)).thenReturn(Decision.TASKS);
        when(shopDecisionService.decide(botState)).thenReturn(Optional.of(Decision.BUY_ITEM));

        var decision = botDecisionService.decide(botState);

        assertEquals(Decision.BUY_ITEM, decision);
    }

    @Test
    void fallback_to_task_when_no_shop_decision() {
        botState.setLives(4);
        Task task = new Task();
        task.setMessage("Test Task");
        task.setExpiresIn(1);
        task.setAdId("123");
        task.setRewardAmount(100);
        task.setProbability(TaskProbability.PLAYING_WITH_FIRE.successRate());
        botState.setAvailableTasks(List.of(task));
        botState.setSelectedTask(task);

        when(taskDecisionService.decide(botState)).thenReturn(Decision.TASKS);
        when(shopDecisionService.decide(botState)).thenReturn(Optional.empty());

        var decision = botDecisionService.decide(botState);

        assertEquals(Decision.TASKS, decision);
    }
}