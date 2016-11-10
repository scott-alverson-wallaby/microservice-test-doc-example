package com.example.controller;

import com.example.bo.Greeting;
import com.example.service.GreetingService;
import com.example.service.GreetingService.Mood;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by alverson on 11/4/16.
 */
@RestController
public class GreetingController {

    @GetMapping(path="/greeting", produces="application/json")
    public Greeting get(@RequestParam(value = "greetingType", required = true) String greetingType,
                      @RequestParam(value = "mood", required = false) String mood) {

        GreetingService greetingService;


        if (mood == null) {
            greetingService = new GreetingService();
        }
        else {
            try {
                Mood moodEnum = GreetingService.Mood.valueOf(mood.toUpperCase());
                greetingService = new GreetingService(moodEnum);
            } catch (IllegalArgumentException e) {
                greetingService = new GreetingService();
            }
        }

        String greetingText = greetingService.getGreeting(greetingType);
        Greeting greeting = new Greeting();
        greeting.setGreeting(greetingText);
        return greeting;
    }
}
