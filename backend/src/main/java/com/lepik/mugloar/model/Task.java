package com.lepik.mugloar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String adId;
    private String message;
    private int rewardAmount;
    private int expiresIn;
    private double probability;
}
