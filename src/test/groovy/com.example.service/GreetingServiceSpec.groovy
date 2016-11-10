package com.example.service

import com.example.datasource.GreetingDataSource
import com.example.service.GreetingService.Mood
import com.example.exception.DirtyLookException

import spock.lang.*


class GreetingServiceSpec extends Specification {

    GreetingService greetingService = new GreetingService();

    def "get a nice greeting from the service"() {
        expect:
        greetingService.getGreeting("nice").equals("Hi, how are you today?")
    }

    def "get a nasty greeting from the service"() {
        expect:
        greetingService.getGreeting("nasty").equals("I'm too happy to be nasty.")
    }

    def "get an elegant greeting from the service"() {
        expect:
        greetingService.getGreeting("elegant").equals("How are you this fine day?")
    }

    def "try to get a nice greeting from the service when it's angry"() {
        setup:
        greetingService = new GreetingService(Mood.ANGRY)

        expect:
        greetingService.getGreeting("nice").equals("Don't tell me to be nice, jerk!")
    }

    def "try to get a nasty greeting from the service when it's angry"() {
        given:
        greetingService = new GreetingService(Mood.ANGRY)

        expect:
        greetingService.getGreeting("nasty").equals("Take a long walk off a short pier!")
    }

    def "try to get an elegant greeting from the service when it's angry"() {
        given:
        greetingService = new GreetingService(Mood.ANGRY)

        when:
        greetingService.getGreeting("elegant")

        then:
        thrown(DirtyLookException)
    }

    def "test happy behavior using explicit constructor"() {
        given:
        greetingService = new GreetingService(Mood.HAPPY)

        expect:
        greetingService.getGreeting(greetingType).equals(greeting)

        where:
        greetingType << ["nice",
                         "nasty",
                         "elegant"]
        greeting << ["Hi, how are you today?", "I'm too happy to be nasty.", "How are you this fine day?"]
    }

    def "test service call to datasource"() {
        setup:
        def greetingDataSource = Mock(GreetingDataSource)
        greetingService.setGreetingDataSource(greetingDataSource)

        when:
        greetingService.getGreeting("nice")

        then:
        1 * greetingDataSource.getGreetingSuffix()
    }
}