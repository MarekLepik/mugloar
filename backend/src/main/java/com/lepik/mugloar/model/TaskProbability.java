package com.lepik.mugloar.model;

import java.util.Arrays;

public enum TaskProbability {
    UNKNOWN(0.05, null),
    SUICIDE_MISSION(0.1, "Suicide mission"),
    IMPOSSIBLE(0.2, "Impossible"),
    PLAYING_WITH_FIRE(0.3, "Playing with fire"),
    RISKY(0.4, "Risky"),
    HMMM(0.5, "Hmmm...."),
    QUITE_LIKELY(0.7, "Quite likely"),
    PIECE_OF_CAKE(0.8, "Piece of cake"),
    WALK_IN_THE_PARK(0.9, "Walk in the park"),
    SURE_THING(1.0, "Sure thing");

    private final double successRate;
    private final String apiValue;

    TaskProbability(double successRate, String apiValue) {
        this.successRate = successRate;
        this.apiValue = apiValue;
    }

    public double successRate() {
        return successRate;
    }

    public static TaskProbability fromApiValue(String value) {
        if (value == null) {
            return UNKNOWN;
        }

        return Arrays.stream(values())
                .filter(tp -> value.equals(tp.apiValue))
                .findFirst()
                .orElse(UNKNOWN);
    }
}

