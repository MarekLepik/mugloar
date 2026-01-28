package com.lepik.mugloar.model;

import lombok.Getter;

@Getter
public enum ItemType {
    HEALING_POTION("hpot", "healing"),
    CLAW_SHARPENING("cs", "claw"),
    GAS("gas", "fuel"),
    COPPER_PLATING("wax", "armor"),
    BOOK_OF_TRICKS("tricks", "tricks"),
    POTION_OF_STRONGER_WINGS("wingpot", "wings"),
    CLAW_HONING("ch", "claw"),
    ROCKET_FUEL("rf", "fuel"),
    IRON_PLATING("iron", "armor"),
    BOOK_OF_MEGA_TRICKS("mtrix", "tricks"),
    POTION_OF_AWESOME_WINGS("wingpotmax", "wings");

    private final String itemId;
    private final String tag;

    ItemType(String itemId, String tag) {
        this.itemId = itemId;
        this.tag = tag;
    }
}
