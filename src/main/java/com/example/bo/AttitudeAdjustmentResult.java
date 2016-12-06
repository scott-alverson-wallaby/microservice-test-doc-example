package com.example.bo;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class AttitudeAdjustmentResult {

    @JsonProperty
    private boolean successful;

    @JsonProperty
    private String response;
}
