package com.example.service;

import com.example.bo.AttitudeAdjustmentResult;
import com.example.datasource.AttitudeDataSource;
import com.example.enumeration.Mood;
import com.example.exception.MessingWithMyHappinessException;

/**
 * Created by alverson on 12/2/16.
 */
public class AttitudeService {

    private static AttitudeDataSource dataSource = new AttitudeDataSource();

    public AttitudeAdjustmentResult submitAttitudeAdjustment(String source) {
        Mood mood = dataSource.getMood();

        if (mood == Mood.ANGRY) {
            return dataSource.attemptAttitudeAdjustment(source);
        }
        else {
            return AttitudeAdjustmentResult.builder().successful(false).response("I'm good, thanks anyway").build();
        }
    }


    public void setMood(Mood mood) {
        dataSource.setMood(mood);
    }


    public Mood getMood() {
        return dataSource.getMood();
    }


    public void clearMood() throws MessingWithMyHappinessException {
        dataSource.clearMood();
    }
}
