package com.example.datasource;

import com.example.bo.AttitudeAdjustmentResult;
import com.example.enumeration.Mood;
import com.example.exception.MessingWithMyHappinessException;

/**
 * Created by alverson on 11/7/16.
 */
public class AttitudeDataSource {

    Mood mood = Mood.HAPPY;

    public Mood getMood() {
        return mood;
    }

    public AttitudeAdjustmentResult attemptAttitudeAdjustment(String source)  {

        AttitudeAdjustmentResult result;

        if (source != null
                && (source.equalsIgnoreCase("Dad")
                    || source.equalsIgnoreCase("Mom"))) {

            mood = Mood.HAPPY;
            return AttitudeAdjustmentResult.builder().successful(true)
                                .response("OK I feel happy again").build();
        }

        return AttitudeAdjustmentResult.builder().successful(false).response("You're not my mom or dad!").build();
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public void clearMood() throws MessingWithMyHappinessException {
        if (mood == Mood.HAPPY) {
            throw new MessingWithMyHappinessException();
        }

        this.mood = Mood.CALM;
    }
}
