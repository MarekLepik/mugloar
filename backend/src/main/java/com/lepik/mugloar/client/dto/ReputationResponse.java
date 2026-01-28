package com.lepik.mugloar.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReputationResponse {
    private int people;
    private int state;
    private int underworld;
}
