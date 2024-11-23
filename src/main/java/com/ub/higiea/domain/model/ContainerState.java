package com.ub.higiea.domain.model;

import java.util.Arrays;

public enum ContainerState {
    FULL,
    EMPTY;

    public static boolean isValid(String value) {
        return Arrays.stream(ContainerState.values())
                .anyMatch(state -> state.name().equalsIgnoreCase(value));
    }
}
