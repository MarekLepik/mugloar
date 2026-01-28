package com.lepik.mugloar.service;

import com.lepik.mugloar.bot.model.BotState;
import com.lepik.mugloar.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopDecisionServiceTest {

    private ShopDecisionService shopDecisionService;
    private BotState botState;

    @BeforeEach
    void setUp() {
        shopDecisionService = new ShopDecisionService();
        botState = new BotState();
        botState.setGold(100);
        Item shopItem = new Item();
        shopItem.setId("hpot");
        shopItem.setCost(50);
        shopItem.setName("healing potion");
        botState.setAvailableItems(List.of(shopItem));
    }

    @Test
    void decide_shop_and_choose_potion_as_preferred_item() {
        botState.setLives(3);
        var decision = shopDecisionService.decide(botState);
        assertTrue(decision.isPresent());
        assertEquals("hpot", botState.getDesiredItem().getId());
    }

    @Test
    void decide_no_shop_when_items_not_affordable() {
        botState.setGold(10);
        var decision = shopDecisionService.decide(botState);
        assertTrue(decision.isEmpty());
    }

    @Test
    void decide_item_if_no_healing_needed() {
        botState.setLives(4);
        Item shopItem = new Item();
        shopItem.setId("item");
        shopItem.setCost(100);
        shopItem.setName("item100");

        Item healingItem = new Item();
        healingItem.setId("hpot");
        healingItem.setCost(50);
        healingItem.setName("healing potion");

        botState.setAvailableItems(List.of(shopItem, healingItem));
        var decision = shopDecisionService.decide(botState);
        assertTrue(decision.isPresent());
        assertEquals("item", botState.getDesiredItem().getId());
    }
}