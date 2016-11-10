package com.example.bo;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Greeting {

    @JsonProperty
    String greeting;
}
