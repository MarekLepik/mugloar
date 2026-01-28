package com.lepik.mugloar.mappers;

import com.lepik.mugloar.client.dto.StartGameResponse;
import com.lepik.mugloar.model.Game;
import org.springframework.stereotype.Component;


@Component
public class GameMapper {

    public Game toDomain(StartGameResponse game) {
        if (game == null) {
            return null;
        }

        return new Game(
                game.getGameId(),
                game.getLives(),
                game.getGold(),
                game.getLevel(),
                game.getScore(),
                game.getHighScore(),
                game.getTurn()
        );
    }
}

