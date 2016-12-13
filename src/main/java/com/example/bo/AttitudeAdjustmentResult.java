package com.example.bo;

import lombok.Data;
import lombok.experimental.Builder;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class AttitudeAdjustmentResult {

    @JsonProperty
    private boolean successful;

    @JsonProperty
    private String response;
}
