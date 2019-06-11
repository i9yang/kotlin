package ssg.front.ssgsearchtestwebapp

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.http.HttpDocumentation.httpRequest
import org.springframework.restdocs.http.HttpDocumentation.httpResponse
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@RunWith(SpringRunner::class)
abstract class MockMvcBase {
    lateinit var mockMvc: MockMvc

    @get:Rule
    var restDocumentation = JUnitRestDocumentation()

    @Autowired
    lateinit var context: WebApplicationContext

    @Before
    fun setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(
                        documentationConfiguration(restDocumentation)
                                .snippets().withDefaults(httpRequest(), httpResponse())
                                .and()
                                .operationPreprocessors().withResponseDefaults(prettyPrint())
                )
                .alwaysExpect<DefaultMockMvcBuilder>(status().isOk)
                .build()
    }
}