package com.example.exception.controller;

import com.example.exception.DirtyLookException;

import com.example.exception.InvalidMoodException;
import com.example.exception.MessingWithMyHappinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value= DirtyLookException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleDirtyLook(DirtyLookException e) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("facial_expression", e.getMessage());
        return map;
    }

    @ExceptionHandler(value = InvalidMoodException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleInvalidMood(InvalidMoodException e) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("error",  e.getMessage() + " is not a supported mood");
        return map;
    }

    @ExceptionHandler(value = MessingWithMyHappinessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleMessingWithMyHappiness() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("error", "I'm not going to allow you to mess with my happiness");
        return map;
    }
}
