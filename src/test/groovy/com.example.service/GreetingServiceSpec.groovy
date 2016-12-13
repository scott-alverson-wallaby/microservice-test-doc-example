package com.example.service


import com.example.bo.Greeting
import com.example.datasource.AttitudeDataSource
import com.example.enumeration.Mood
import com.example.exception.DirtyLookException

import spock.lang.*


class GreetingServiceSpec extends Specification {


    def greetingService;
    def attitudeService;

    def setup() {
        greetingService = new GreetingService();
        attitudeService = Mock(AttitudeService)
        greetingService.setAttitudeService(attitudeService)
    }


    def "get a nice greeting from the service"() {
        setup:
        attitudeService.getMood() >> Mood.HAPPY

        expect:
        greetingService.getGreeting("nice", null).getText().equals("Hi, how are you today?")
    }

    def "get a nasty greeting from the service"() {
        setup:
        attitudeService.getMood() >> Mood.HAPPY

        expect:
        greetingService.getGreeting("nasty", null).getText().equals("I'm too happy to be nasty.")
    }

    def "get an elegant greeting from the service"() {
        setup:
        attitudeService.getMood() >> Mood.HAPPY

        expect:
        greetingService.getGreeting("elegant", null).getText().equals("How are you this fine day?")
    }

    def "try to get a nice greeting from the service when it's angry"() {
        expect:
        greetingService.getGreeting("nice", Mood.ANGRY).getText().equals("Don't tell me to be nice, jerk!")
    }

    def "try to get a nasty greeting from the service when it's angry"() {
        expect:
        greetingService.getGreeting("nasty", Mood.ANGRY).getText().equals("Take a long walk off a short pier!")
    }

    def "try to get an elegant greeting from the service when it's angry"() {
        when:
        greetingService.getGreeting("elegant", Mood.ANGRY)

        then:
        thrown(DirtyLookException)
    }

    def "test calm response"() {
        setup:
        attitudeService.getMood() >> Mood.CALM

        expect:
        greetingService.getGreeting(greetingType, null).getText().equals("Have a calm, tranquil day - I sure am")

        where:
        greetingType << ["nice",
                         "nasty",
                         "elegant"]
    }

    @Unroll
    def "test happy behavior using explicit parameter"() {
        expect:
        greetingService.getGreeting(greetingType, Mood.HAPPY).getText().equals(greeting)

        where:
        greetingType << ["nice",
                         "nasty",
                         "elegant"]
        greeting << ["Hi, how are you today?",
                     "I'm too happy to be nasty.",
                     "How are you this fine day?"]
    }

    def "test explicit parameter overriding datasource"() {
        setup:
        attitudeService.getMood() >> Mood.HAPPY

        expect:
        greetingService.getGreeting(greetingType, mood).getText().equals(greeting)

        where:
        greetingType |    mood     || greeting
        "nice"       |  Mood.ANGRY || "Don't tell me to be nice, jerk!"
        "elegant"    |  Mood.CALM  || "Have a calm, tranquil day - I sure am"
        "nasty"      |  Mood.CALM  || "Have a calm, tranquil day - I sure am"
    }
}