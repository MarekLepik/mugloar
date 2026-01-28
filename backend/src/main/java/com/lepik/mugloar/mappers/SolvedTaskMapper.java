package com.lepik.mugloar.mappers;

import com.lepik.mugloar.client.dto.SolvedTaskResponse;
import com.lepik.mugloar.model.SolvedTask;
import org.springframework.stereotype.Component;

@Component
public class SolvedTaskMapper {

    public SolvedTask toDomain(SolvedTaskResponse response) {
        if (response == null) {
            return null;
        }

        return new SolvedTask(
                response.isSuccess(),
                response.getLives(),
                response.getGold(),
                response.getScore(),
                response.getHighScore(),
                response.getTurn(),
                response.getMessage()
        );
    }
}
