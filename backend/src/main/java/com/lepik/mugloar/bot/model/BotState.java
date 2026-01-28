package com.lepik.mugloar.bot.model;

import com.lepik.mugloar.model.Item;
import com.lepik.mugloar.model.Task;
import com.lepik.mugloar.model.TaskProbability;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class BotState {
    private int lastShopVisitTurn = 0;
    private List<Item> availableItems;
    private Item desiredItem;
    private double acceptableRisk = TaskProbability.QUITE_LIKELY.successRate();
    private List<Task> availableTasks;
    private Task selectedTask;
    private List<Item> purchasedItemsHistory = new ArrayList<>();
    private List<Item> shoppingCart = new ArrayList<>();
    private Set<String> ownedItemIds = new HashSet<>();
    private Set<String> blacklistedTaskIds = new HashSet<>();
    private int gold;
    private int lives;
}