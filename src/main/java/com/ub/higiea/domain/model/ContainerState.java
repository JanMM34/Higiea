package com.ub.higiea.domain.model;

import java.util.Arrays;

public enum ContainerState {
    EMPTY(0),
    HALF(50),
    FULL(80);

    private final int level;

    ContainerState(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static ContainerState fromLevel(int level) {
        return switch (level) {
            case 0 -> EMPTY;
            case 50 -> HALF;
            case 80 -> FULL;
            default -> throw new IllegalArgumentException("Unknown container level: " + level);
        };
    }

    public static boolean isValid(String state) {
        return Arrays.stream(ContainerState.values())
                .anyMatch(cs -> cs.name().equalsIgnoreCase(state));
    }

}