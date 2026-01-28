package com.lepik.mugloar.mappers;

import com.lepik.mugloar.client.dto.ReputationResponse;
import com.lepik.mugloar.model.Reputation;
import org.springframework.stereotype.Component;

@Component
public class ReputationMapper {
    public Reputation toDomain(ReputationResponse reputationResponse) {
        if (reputationResponse == null) {
            return null;
        }

        return new Reputation(
                reputationResponse.getPeople(),
                reputationResponse.getState(),
                reputationResponse.getUnderworld()
        );
    }
}

