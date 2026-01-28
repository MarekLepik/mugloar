package com.lepik.mugloar.bot;

import com.lepik.mugloar.bot.model.BotState;
import com.lepik.mugloar.bot.model.Decision;
import com.lepik.mugloar.model.*;
import com.lepik.mugloar.service.BotDecisionService;
import com.lepik.mugloar.service.GameService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GameBot {
    private final GameService gameService;
    private final BotDecisionService botDecisionService;

    public GameBot(GameService gameService, BotDecisionService botDecisionService) {
        this.gameService = gameService;
        this.botDecisionService = botDecisionService;
    }

    public void playAutomatically(int scoreLimit) {
        Optional<Game> game = gameService.startNewGame();
        if (game.isEmpty()) {
            System.out.println("Failed to start a new game.");
            return;
        }
        Game startedGame = game.get();

        BotState botState = new BotState();
        gameLoop(startedGame, botState, scoreLimit);

        if (isGameOver(startedGame)) {
            System.out.println("Game over! Final score: " + startedGame.getScore() + " Game ID: " + startedGame.getGameId());
        } else {
            System.out.println("Score limit reached. Stopping bot execution. Final score: " + startedGame.getScore() + " Game ID: " + startedGame.getGameId());
        }

    }

    private void gameLoop(Game game, BotState botState, int scoreLimit) {
        while (!isGameOver(game) && game.getScore() < scoreLimit) {
            fetchTaskList(game, botState);
            fetchShopList(game, botState);
            updateBotStateWithGameState(game, botState);
            Decision decision = botDecisionService.decide(botState);
            System.out.println("Making decision: " + decision);

            if (decision == Decision.BUY_ITEM) {
                buyItem(game, botState);
            }
            handleTasks(game, botState);
            System.out.println("End of turn " + game.getTurn() + ": Gold = " + game.getGold() + ", Lives = " + game.getLives() + ", Score = " + game.getScore() + " Game ID: " + game.getGameId());
        }
    }

    private void handleTasks(Game game, BotState botState) {
            Task selectedTask = botState.getSelectedTask();
            System.out.println("Selected task with reward: " + selectedTask.getRewardAmount() + " and probability: " + selectedTask.getProbability());
            if (selectedTask.getProbability() == TaskProbability.UNKNOWN.successRate()) {
                System.out.println(selectedTask);
                System.out.println(game.getGameId());
                return;
            }

            Optional<SolvedTask> taskResponse = gameService.solveTask(game.getGameId(), selectedTask.getAdId());
            if (taskResponse.isEmpty()) {
                System.out.println("No response for the task.");
                botState.getBlacklistedTaskIds().add(selectedTask.getAdId());
                return;
            }
            gameService.updateGameState(game, taskResponse.get());
            System.out.println("Task message: " + taskResponse.get().getMessage());
    }

    private void fetchShopList(Game game, BotState botState) {
        Optional<List<Item>> itemList = gameService.getShopItems(game.getGameId());
        botState.setLastShopVisitTurn(game.getTurn());
        if (itemList.isEmpty()) {
            System.out.println("No shop items available.");
            botState.setAvailableItems(List.of());
            return;
        }
        botState.setAvailableItems(itemList.get());
    }

    private void updateBotStateWithGameState(Game game, BotState botState) {
        botState.setGold(game.getGold());
        botState.setLives(game.getLives());
    }

    private void fetchTaskList(Game game, BotState botState) {
        Optional<List<Task>> taskList = gameService.getTasks(game.getGameId());
        if (taskList.isEmpty()) {
            System.out.println("No tasks available.");
            botState.setAvailableTasks(List.of());
            return;
        }
        botState.setAvailableTasks(taskList.get());
    }

    private void buyItem(Game game, BotState botState) {
        Item item = botState.getDesiredItem();
        botState.setDesiredItem(null);
        Optional<Purchase> purchaseOptional = gameService.buyItem(game.getGameId(), item.getId());
        if (purchaseOptional.isEmpty()) {
            System.out.println("No response for the purchase.");
            return;
        }

        Purchase purchase = purchaseOptional.get();
        if (purchase.isShoppingSuccess()) {
            botState.getPurchasedItemsHistory().add(item);
            botState.getOwnedItemIds().add(item.getId());
            botState.getShoppingCart().remove(item);
        }
        gameService.updateGameState(game, purchase);
        System.out.println("Bought item: " + purchase.isShoppingSuccess() + " lives " + purchase.getLives() + " turn " + purchase.getTurn() + " gold " + purchase.getGold());
    }

    private boolean isGameOver(Game game) {
        return game.getLives() <= 0;
    }
}