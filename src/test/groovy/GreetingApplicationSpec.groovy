package com.example

import spock.lang.*

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.empty;

import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import static com.jayway.restassured.RestAssured.*
import static com.jayway.restassured.builder.RequestSpecBuilder.addFilter
import com.jayway.restassured.builder.RequestSpecBuilder
import com.jayway.restassured.specification.RequestSpecification

import org.springframework.restdocs.JUnitRestDocumentation
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;

import org.junit.Rule


@IgnoreIf({ System.getProperty("testenv").equals("standalone") })
class GreetingApplicationSpec extends Specification {

    RequestSpecification docSpec

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation('src/docs/generated-snippets');

    def port = 8080


    def setup() {
        docSpec = new RequestSpecBuilder().addFilter(documentationConfiguration(this.restDocumentation)).build()
        // set application to default happy state
        given().queryParam("greetingType", "elegant").queryParam("mood", "happy").get("/greeting")
    }

    def cleanupSpec() {
        // set application to default happy state
        given().queryParam("greetingType", "elegant").queryParam("mood", "happy").get("/greeting")
    }

    def "get a nice greeting"() {

        expect:
        given(docSpec).queryParam("greetingType", "nice").filter(document("get", responseFields(
                fieldWithPath("greeting").description("The greeting received")),
                requestParameters(parameterWithName("greetingType").description("The type of greeting desired"))))
                    .when().get("/greeting")
                    .then().statusCode(200)
                        .contentType("application/json;charset=UTF-8")
                        .body("greeting", equalTo("Hi, how are you today?"))
    }

    @Ignore
    def "get an elegant greeting"() {
        expect:
        given().queryParam("greetingType", "elegant")
                .when().get("/greeting")
                    .then().statusCode(200)
                        .body("greeting", equalTo("How are you this fine day?"))
    }

    @Ignore
    def "get a nasty greeting"() {
        expect:
        given().queryParam("greetingType", "nasty")
                .when().get("/greeting")
                    .then().statusCode(200)
                        .contentType("application/json;charset=UTF-8")
                        .body("greeting", equalTo("I'm too happy to be nasty."))
    }

    @Ignore
    def "get a nice greeting from an angry person"() {
        expect:
        given().queryParam("greetingType", "nice").queryParam("mood", "angry")
                .when().get("/greeting")
                    .then().statusCode(200)
                        .contentType("application/json;charset=UTF-8")
                        .body("greeting", equalTo("Don't tell me to be nice, jerk!"))
    }

    @Ignore
    def "get a nasty greeting from an angry person"() {
        expect:
        given().queryParam("greetingType", "nasty").queryParam("mood", "angry")
                .when().get("/greeting")
                    .then().statusCode(200)
                        .contentType("application/json;charset=UTF-8")
                        .body("greeting", equalTo("Take a long walk off a short pier!"))
    }


    def "get greeting with greetingType parameter only"() {
        expect:
        given(docSpec).queryParam("greetingType", greetingType).filter(document("get", responseFields(
                fieldWithPath("greeting").description("The greeting received"))))
                .when().get("/greeting")
                    .then().statusCode(200)
                        .contentType("application/json;charset=UTF-8")
                        .body("greeting", equalTo(greeting))

        where:
        greetingType || greeting
        "nice"       || "Hi, how are you today?"
        "elegant"    || "How are you this fine day?"
        "nasty"      || "I'm too happy to be nasty."
        "other"      || "Hi, how are you today?"
    }

    def "get greeting with both greetingType and mood specified"() {
        expect:
        given(docSpec).queryParam("greetingType", greetingType).queryParam("mood", mood)
                    .when().get("/greeting")
                        .then().statusCode(200)
                            .contentType("application/json;charset=UTF-8")
                            .body("greeting", equalTo(greeting))

        where:
        greetingType |   mood   || greeting
        "nice"       | "happy"  || "Hi, how are you today?"
        "elegant"    | "happy"  || "How are you this fine day?"
        "nasty"      | "happy"  || "I'm too happy to be nasty."
        "whichever"  | "happy"  || "Hi, how are you today?"
        "nice"       | "angry"  || "Don't tell me to be nice, jerk!"
        "nasty"      | "angry"  || "Take a long walk off a short pier!"
        "whatever"   | "angry"  || "Take a long walk off a short pier!"
        "nice"       | "calm"   || "Have a calm, tranquil day - I sure am"
        "elegant"    | "calm"   || "Have a calm, tranquil day - I sure am"
        "nasty"      | "calm"   || "Have a calm, tranquil day - I sure am"
        "any"        | "calm"   || "Have a calm, tranquil day - I sure am"
    }

    def "get greeting / invalid request - elegant greeting from an angry person"() {
        expect:
        given(docSpec).queryParam("greetingType", "elegant").queryParam("mood", "angry")
                .filter(document("get/bad", preprocessRequest(modifyUris())))
                .when().get("/greeting")
                .then().statusCode(400)
                        .contentType("application/json;charset=UTF-8")
                        .body("facial_expression", equalTo("You can't be serious!"))
    }

    def "get greeting / invalid mood"() {
        expect:
        given(docSpec).queryParam("greetingType", "elegant").queryParam("mood", "fast")
                .filter(document("get/bad", preprocessRequest(modifyUris())))
                .when().get("/greeting")
                    .then().statusCode(400)
                        .contentType("application/json;charset=UTF-8")
                        .body("error", equalTo("fast is not a supported mood"))
    }

    def "get greeting with lingering mood"() {
        when:
        given().queryParam("greetingType", "any").queryParam("mood", "angry").get("greeting/")

        then:
        given().queryParam("greetingType", "nice")
            .when().get("/greeting")
                .then().statusCode(200)
                .contentType("application/json;charset=UTF-8")
                .body("greeting", equalTo("Don't tell me to be nice, jerk!"))
    }

    def "post attitude adjustment, default state"() {
        expect:
        given(docSpec).queryParam("source", source).filter(document("post", preprocessRequest(modifyUris())))
            .when().post("/greeting")
            .then().statusCode(200)
                    .contentType("application/json;charset=UTF-8")
                    .body("successful", equalTo(false))
                    .body("response", equalTo("I'm good, thanks anyway"))

        where:
        source << ["mom", "friend"]
    }

    def "post attitude adjustment, angry person"() {
        when:
        given().queryParam("greetingType", "any").queryParam("mood", "angry").get("greeting/")

        then:
        given().queryParam("source", source)
                .when().post("/greeting")
                    .then().statusCode(200)
                            .contentType("application/json;charset=UTF-8")
                            .body("successful", equalTo(success))
                            .body("response", equalTo(response))

        where:
        source     || success | response
        "mom"      || true    | "OK I feel happy again"
        "dad"      || true    | "OK I feel happy again"
        "friend"   || false   | "You're not my mom or dad!"
        "stranger" || false   | "You're not my mom or dad!"
    }

    def "get shows response after attitude adjustment"() {
        when:
        given().queryParam("greetingType", "any").queryParam("mood", "angry").get("greeting/")
        given().queryParam("source", source).post("/greeting")

        then:
        given().queryParam("greetingType", "nice")
                .when().get("/greeting")
                .then().statusCode(200)
                        .contentType("application/json;charset=UTF-8")
                        .body("greeting", equalTo(greeting))

        where:
        source     || greeting
        "stranger" || "Don't tell me to be nice, jerk!"
        "dad"      || "Hi, how are you today?"
    }

    def "delete mood, unauthorized due to being in default happy state"() {
        expect:
        given(docSpec).filter(document("delete", preprocessRequest(modifyUris())))
            .when().delete("/greeting")
            .then().statusCode(401)
                .contentType("application/json;charset=UTF-8")
                .body("error", equalTo("I'm not going to allow you to mess with my happiness"))
    }

    def "delete mood successful"() {
        when:
        given().queryParam("greetingType", "any").queryParam("mood", mood).get("greeting/")

        then:
        given(docSpec).filter(document("delete", preprocessRequest(modifyUris())))
                .when().delete("/greeting")
                .then().statusCode(200)

        where:
        mood << ["angry", "calm"]
    }

    def "get shows return to happiness after deleting other mood"() {
        when:
        given().queryParam("greetingType", "any").queryParam("mood", mood).get("greeting/")
        given().delete("/greeting")

        then:
        given().queryParam("greetingType", "nice")
                .when().get("greeting")
                .then().statusCode(200)
                        .contentType("application/json;charset=UTF-8")
                        .body("greeting", equalTo("Have a calm, tranquil day - I sure am"))

        where:
        mood << ["angry", "calm"]
    }

}