package com.example.controller;

import com.example.bo.Greeting;
import com.example.bo.AttitudeAdjustmentResult;
import com.example.exception.DirtyLookException;
import com.example.exception.InvalidMoodException;
import com.example.exception.MessingWithMyHappinessException;
import com.example.service.AttitudeService;
import com.example.service.GreetingService;
import com.example.enumeration.Mood;

import org.springframework.web.bind.annotation.*;


@RestController
public class GreetingController {

    GreetingService greetingService = new GreetingService();
    AttitudeService attitudeService = new AttitudeService();

    @GetMapping(path="/greeting", produces="application/json")
    public Greeting get(@RequestParam(value = "greetingType", required = true) String greetingType,
                      @RequestParam(value = "mood", required = false) String mood)
                                                            throws InvalidMoodException, DirtyLookException {

        Greeting greeting = null;
        Mood moodEnum = null;

        System.out.println("mood string = "  + mood);

        if (mood != null) {
            try {
                moodEnum = Mood.valueOf(mood.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidMoodException(mood);
            }
        }

        greeting = greetingService.getGreeting(greetingType, moodEnum);

        if (greeting == null) {
            throw new RuntimeException();
        }

        return greeting;
    }

    @PostMapping(path="/greeting", produces="application/json")
    public AttitudeAdjustmentResult post(@RequestParam(value = "source", required = true) String source) {
        return attitudeService.submitAttitudeAdjustment(source);
    }

    @DeleteMapping(path="/greeting")
    public void delete() throws MessingWithMyHappinessException {
        attitudeService.clearMood();
    }
}
