package com.example.bo;

import lombok.Data;
import lombok.experimental.Builder;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class Greeting {

    @JsonProperty("greeting")
    String text;
}
