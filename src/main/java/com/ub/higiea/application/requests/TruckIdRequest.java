package com.ub.higiea.application.requests;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class TruckIdRequest {
    @NotNull
    private UUID id;

    // Getter and Setter
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}