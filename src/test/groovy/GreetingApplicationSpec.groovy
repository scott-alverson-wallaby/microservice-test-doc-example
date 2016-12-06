package com.example

import spock.lang.*

import static org.hamcrest.Matchers.equalTo;

import com.jayway.restassured.RestAssured
import com.jayway.restassured.http.ContentType
import static com.jayway.restassured.RestAssured.*
import static com.jayway.restassured.builder.RequestSpecBuilder.addFilter
import com.jayway.restassured.builder.RequestSpecBuilder
import com.jayway.restassured.specification.RequestSpecification

import org.springframework.restdocs.JUnitRestDocumentation
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.modifyUris;

import org.junit.Rule


class GreetingApplicationSpec extends Specification {

    RequestSpecification docSpec

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation('src/docs/generated-snippets');

    def port = 8080


    def setup() {
        docSpec = new RequestSpecBuilder().addFilter(documentationConfiguration(this.restDocumentation)).build()
    }

    def "request a nice greeting"() {
        setup:
        def request = RestAssured.given().urlEncodingEnabled(false).pathParam("greetingType", "nice")
        System.out.println("" + given(docSpec).queryParam("greetingType", "nice").filter(document("sample", preprocessRequest(modifyUris().scheme("http").host("localhost").removePort()))).when().port(port))


        expect:

        given(docSpec).queryParam("greetingType", "nice").filter(document("get", preprocessRequest(modifyUris().scheme("http").host("localhost").removePort()))).when().port(port).get("/greeting")

//        given(docSpec).queryParam("greetingType", "nice").when().get("http://localhost:8080/greeting")
//                .then().statusCode(200).contentType("application/json;charset=UTF-8")
//                .body("greeting", equalTo("Hi, how are you today?"))
    }
}