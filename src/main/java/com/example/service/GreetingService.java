package com.example.service;

import com.example.datasource.GreetingDataSource;
import com.example.exception.DirtyLookException;
/**
 * Created by alverson on 11/4/16.
 */
public class GreetingService {

    private Mood mood;

    protected GreetingDataSource dataSource = new GreetingDataSource();

    public GreetingService()  {
        mood = Mood.HAPPY;
    }

    public GreetingService(Mood mood) {
        this.mood = mood;
    }

    public String getGreeting(String type) {
        String greeting;

        if (mood == Mood.ANGRY) {
            switch (type) {
                case "nice": greeting = "Don't tell me to be nice, jerk!"; break;
                case "elegant": throw new DirtyLookException();
                default: greeting = "Take a long walk off a short pier!"; break;
            }
        }
        else {
            switch (type) {
                case "nasty": greeting =  "I'm too happy to be nasty."; break;
                case "elegant": greeting = "How are you this fine day?"; break;
                default: greeting = "Hi, how are you today?"; break;
            }
        }

        return greeting + dataSource.getGreetingSuffix();
    }

    protected void setGreetingDataSource(GreetingDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public enum Mood {
        HAPPY, ANGRY;
    }
}
