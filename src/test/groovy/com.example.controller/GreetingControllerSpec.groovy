package com.example.service

import com.example.GreetingApplication
import groovyx.net.http.RESTClient
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.restdocs.RestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint


import spock.lang.*

@ContextConfiguration(classes = [GreetingApplication])
@WebAppConfiguration
class GreetingControllerSpec extends Specification {

    def client
    def mockMvc

    @Autowired
    protected WebApplicationContext context

    @Rule
    RestDocumentation restDocumentation = new RestDocumentation('src/docs/generated-snippets')


    def setup() {
        client = new RESTClient("http://localhost:8080/greeting")
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build()
    }


    def "request a nice greeting"() {
/*         when:
       assert client != null
        def resp = client.get(query: [greetingType: "nice"])

        then:
        resp.getStatus() == 200
        resp.getContentType().equals("application/json")
        resp.getData().get("greeting").equals("Hi, how are you today?")
        */

        when:
        ResultActions result = this.mockMvc.perform(get('/greeting').param('greetingType', 'nice'))

        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('greeting').value('Hi, how are you today?'))
                .andDo(document("nice",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("greeting").description("The greeting received"))))
    }

    def "request a nasty greeting"() {
/*        when:
        def resp = client.get(query: [greetingType: "nasty"])

        then:
        resp.getStatus() == 200
        resp.getContentType().equals("application/json")
        resp.getData().get("greeting").equals("I'm too happy to be nasty.") */

        when:
        ResultActions result = this.mockMvc.perform(get('/greeting').param('greetingType', 'nasty'))

        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('greeting').value('I\'m too happy to be nasty.')).andDo(document("nasty"))
    }

    def "request an elegant greeting"() {
        /*
        when:
        def resp = client.get(query: [greetingType: "elegant"])

        then:
        resp.getStatus() == 200
        resp.getContentType().equals("application/json")
        resp.getData().get("greeting").equals("How are you this fine day?")'
        */

        when:
        ResultActions result = this.mockMvc.perform(get('/greeting').param('greetingType', 'elegant'))

        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('greeting').value('How are you this fine day?')).andDo(document("elegant"))
    }

    def "request greetings from an angry person"() {
/*        setup:
        client.handler.failure = client.handler.success

        expect:
        client.get(query: [greetingType: typeOfGreeting, mood: 'ANGRY']).getStatus() == status
*/
        expect:
        this.mockMvc.perform(get('/greeting').param('greetingType', typeOfGreeting).param('mood', 'angry')).andExpect(status().is(status)).andDo(document("angry"))

        where:
        typeOfGreeting || status
        "nice"         || 200
        "nasty"        || 200
        "elegant"      || 400
    }
}