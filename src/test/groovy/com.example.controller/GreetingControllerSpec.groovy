package com.example.controller

import com.example.GreetingApplication
import com.example.bo.Greeting
import com.example.controller.GreetingController
import com.example.exception.MessingWithMyHappinessException
import com.example.exception.controller.ExceptionAdvice
import com.example.service.AttitudeService

import org.junit.Rule
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.context.support.StaticApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.restdocs.RestDocumentation
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint


import spock.lang.*

@ContextConfiguration(classes = [GreetingApplication])
@WebAppConfiguration
class GreetingControllerSpec extends Specification {

    def mockMvc

    @Autowired
    protected WebApplicationContext context

    @Rule
    RestDocumentation restDocumentation = new RestDocumentation('src/docs/generated-snippets')


    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build()
    }


    def "get - ok - valid greeting request"() {
        when:
        ResultActions result = this.mockMvc.perform(get('/greeting').param('greetingType', 'nice'))

        then:
        result.andExpect(status().isOk())
                .andExpect(jsonPath('greeting').value('Hi, how are you today?'))
                .andDo(document("controller/nice",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("greeting").description("The greeting received"))))
    }

    def "get - bad request - elegant greeting from an angry person"() {
        expect:
        this.mockMvc.perform(get('/greeting').param('greetingType', "elegant").param('mood', 'angry'))
                .andExpect(status().isBadRequest())

    }

    def "get - bad request - invalid mood"() {
        expect:
        this.mockMvc.perform(get('/greeting').param('greetingType', "elegant").param('mood', 'blorfy'))
                .andExpect(status().isBadRequest())
    }

    def "post - attitude adjustment accepted"() {
        expect:
        this.mockMvc.perform(post('/greeting').param('source', 'anyone'))
            .andExpect(status().isOk())
    }

    def "delete - ok"() {
        setup:
        def stubAttitudeService = Stub(AttitudeService)
        GreetingController controller = new GreetingController(attitudeService: stubAttitudeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()

        expect:
        this.mockMvc.perform(delete('/greeting'))
                        .andExpect(status().isOk())
    }

    def "delete - unauthorized"() {
        setup:
        def stubAttitudeService = Stub(AttitudeService)
        stubAttitudeService.clearMood() >> {throw new MessingWithMyHappinessException()}
        GreetingController controller = new GreetingController(attitudeService: stubAttitudeService);
        StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.registerSingleton("exceptionHandler", ExceptionAdvice.class);
        WebMvcConfigurationSupport webMvcConfigurationSupport = new WebMvcConfigurationSupport();
        webMvcConfigurationSupport.setApplicationContext(applicationContext);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).
                setHandlerExceptionResolvers(webMvcConfigurationSupport.handlerExceptionResolver()).build()

        expect:
        this.mockMvc.perform(delete('/greeting')).andExpect(status().isUnauthorized())
    }
}