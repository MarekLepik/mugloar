package com.lepik.mugloar.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private String adId;
    private String message;
    private String reward;
    private int expiresIn;
    private String probability;
    private Integer encrypted;
}
