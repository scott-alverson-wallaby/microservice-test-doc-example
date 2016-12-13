package com.example.service;

import com.example.bo.AttitudeAdjustmentResult;
import com.example.bo.Greeting;
import com.example.service.AttitudeService;
import com.example.enumeration.Mood;
import com.example.exception.DirtyLookException;


public class GreetingService {


    protected AttitudeService attitudeService = new AttitudeService();


    public Greeting getGreeting(String type, Mood updatedMood) throws DirtyLookException {
        String greeting;
        Mood mood;

        if (updatedMood != null) {
            mood = updatedMood;
            attitudeService.setMood(mood);
        }
        else {
            mood = attitudeService.getMood();
        }

        if (mood == Mood.ANGRY) {
            switch (type) {
                case "nice": greeting = "Don't tell me to be nice, jerk!"; break;
                case "elegant": throw new DirtyLookException("You can't be serious!");
                default: greeting = "Take a long walk off a short pier!"; break;
            }
        }
        else if (mood == Mood.HAPPY) {
            switch (type) {
                case "nasty": greeting =  "I'm too happy to be nasty."; break;
                case "elegant": greeting = "How are you this fine day?"; break;
                default: greeting = "Hi, how are you today?"; break;
            }
        }
        else {
            greeting = "Have a calm, tranquil day - I sure am";
        }

        return Greeting.builder().text(greeting).build();
    }


    protected void setAttitudeService(AttitudeService attitudeService) {
        this.attitudeService = attitudeService;
    }
}
