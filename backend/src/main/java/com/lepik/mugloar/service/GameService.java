package com.lepik.mugloar.service;

import com.lepik.mugloar.client.GameApiClient;
import com.lepik.mugloar.model.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameApiClient gameApiClient;

    public GameService(GameApiClient gameApiClient) {
        this.gameApiClient = gameApiClient;
    }

    public Optional<Game> startNewGame() {
        return gameApiClient.startGame();
    }

    public Optional<List<Task>> getTasks(String gameId) {
        return gameApiClient.getTasks(gameId);
    }

    public Optional<SolvedTask> solveTask(String gameId, String adId) {
        return gameApiClient.SolveTask(gameId, adId);
    }

    public Optional<List<Item>> getShopItems(String gameId) {
        return gameApiClient.getShopItems(gameId);
    }

    public Optional<Purchase> buyItem(String gameId, String itemId) {
        return gameApiClient.buyItem(gameId, itemId);
    }

    public Optional<Reputation> getReputation(String gameId) {
        return gameApiClient.getReputation(gameId);
    }

    public void updateGameState(Game game, SolvedTask result) {
        game.setGold(result.getGold());
        game.setLives(result.getLives());
        game.setScore(result.getScore());
        game.setTurn(result.getTurn());

        if (game.getScore() > game.getHighScore()) {
            game.setHighScore(game.getScore());
        }
    }

    public void updateGameState(Game game, Purchase result) {
        game.setGold(result.getGold());
        game.setLives(result.getLives());
        game.setTurn(result.getTurn());
    }
}
