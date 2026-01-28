package com.lepik.mugloar.service;

import com.lepik.mugloar.bot.model.BotState;
import com.lepik.mugloar.bot.model.Decision;
import com.lepik.mugloar.model.ItemType;
import com.lepik.mugloar.model.Item;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ShopDecisionService {

    public Optional<Decision> decide(BotState botState) {
        if (!isAnyItemAffordable(botState.getGold(), botState.getAvailableItems())) {
            return Optional.empty();
        }

        if (isHealingNeeded(botState)) {
            return tryBuyHealing(botState);
        }

        return tryBuyItem(botState);
    }

    private Optional<Decision> tryBuyHealing(BotState botState) {
        Item healingItem = getSingleItem(botState.getAvailableItems(), ItemType.HEALING_POTION);
        if (isEnoughGold(botState.getGold(), healingItem.getCost())) {
            botState.setDesiredItem(healingItem);
            return Optional.of(Decision.BUY_ITEM);
        }
        return Optional.empty();
    }

    private Optional<Decision> tryBuyItem(BotState botState) {
        Item bestItem = findBestAffordableShopItem(botState);
        if (bestItem != null) {
            botState.setDesiredItem(bestItem);
            return Optional.of(Decision.BUY_ITEM);
        }

        return Optional.empty();
    }

    private Item findBestAffordableShopItem(BotState botState) {
        List<Item> items = botState.getAvailableItems();
        if (items == null || items.isEmpty()) {
            return null;
        }

        if (!isEveryItemOwned(items, botState)) {
            return findMissingItem(botState, items);
        } else if (botState.getShoppingCart().isEmpty()){
            int bestTierCost = findBestTierCost(items, botState);
            fillShoppingCart(items, botState, bestTierCost);
        }

        return findDesiredItemInShop(botState);
    }

    private Item findDesiredItemInShop(BotState botState) {
        List<Item> items = botState.getAvailableItems();
        for (Item item : items) {
            for (Item desiredItem : botState.getShoppingCart()) {
                if (item.getId().equalsIgnoreCase(desiredItem.getId()) && isEnoughGold(botState.getGold(), item.getCost())) {
                    return item;
                }
            }
        }
        return null;
    }

    private void fillShoppingCart(List<Item> items, BotState botState, int bestTierCost) {
        for (Item item : items) {
            if (item.getCost() == bestTierCost) {
                botState.getShoppingCart().add(item);
            }
        }
    }

    private int findBestTierCost(List<Item> items, BotState botState) {
        int bestTierCost = 0;
        for (Item item : items) {
            if (item.getCost() > bestTierCost) {
                bestTierCost = item.getCost();
            }
        }
        return bestTierCost;
    }

    private Item findMissingItem(BotState botState, List<Item> items) {
        return items.stream()
                .filter(item -> !itemIsOwned(item, botState) && isEnoughGold(botState.getGold(), item.getCost()))
                .findFirst()
                .orElse(null);
    }

    private boolean isEveryItemOwned(List<Item> items, BotState botState) {
        Set<String> owned = botState.getOwnedItemIds();
        return items.stream()
                .allMatch(item -> owned.contains(item.getId().toLowerCase()));
    }

    private boolean itemIsOwned(Item item, BotState botState) {
        Set<String> owned = botState.getOwnedItemIds();
        return owned.contains(item.getId().toLowerCase());
    }

    private boolean isAnyItemAffordable(int gold, List<Item> items) {
        return items
                .stream()
                .anyMatch(item -> gold >= item.getCost());
    }

    private boolean isHealingNeeded(BotState botState) {
        return botState.getLives() < 4;
    }

    private Item getSingleItem(List<Item> items, ItemType itemType) {
        return items.stream()
                .filter(item -> item.getId().equalsIgnoreCase(itemType.getItemId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Item not found: " + itemType.getItemId()));
    }

    private boolean isEnoughGold(int gold, int itemCost) {
        return gold >= itemCost;
    }
}