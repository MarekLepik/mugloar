package com.lepik.mugloar.service;

import com.lepik.mugloar.bot.model.BotState;
import com.lepik.mugloar.bot.model.Decision;
import com.lepik.mugloar.model.*;
import org.springframework.stereotype.Service;

@Service
public class BotDecisionService {

    private final TaskDecisionService taskDecisionService;
    private final ShopDecisionService shopDecisionService;

    public BotDecisionService(TaskDecisionService taskDecisionService, ShopDecisionService shopDecisionService) {
        this.taskDecisionService = taskDecisionService;
        this.shopDecisionService = shopDecisionService;
    }

    public Decision decide(BotState botState) {
        Decision taskDecision = taskDecisionService.decide(botState);
        Decision shopDecision = shopDecisionService.decide(botState).orElse(null);
        if (isSimpleTask(botState) && !isHealingNeeded(botState)) {
            return taskDecision;
        } else if (shopDecision != null) {
            return shopDecision;
        }

        return taskDecision;
    }

    private boolean isHealingNeeded(BotState botState) {
        return botState.getDesiredItem() != null &&
                botState.getDesiredItem().getId().equals(ItemType.HEALING_POTION.getItemId());
    }

    private boolean isSimpleTask(BotState botState) {
        return botState.getSelectedTask().getProbability() >= TaskProbability.WALK_IN_THE_PARK.successRate();
    }
}
