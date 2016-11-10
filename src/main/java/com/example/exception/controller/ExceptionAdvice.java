package com.example.exception.controller;

import com.example.exception.DirtyLookException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value= DirtyLookException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleDirtyLookException(DirtyLookException e) {

    }
}
