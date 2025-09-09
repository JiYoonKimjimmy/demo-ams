package me.jimmyberg.ams.testsupport.restdocs

import io.kotest.core.test.TestType
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

abstract class RestDocsBehaviorSpec(
    protected val webApplicationContext: WebApplicationContext
) : CustomBehaviorSpec({}) {

    protected lateinit var mockMvc: MockMvc
    private val restDocumentation = ManualRestDocumentation()

    init {
        aroundTest { (testCase, execute) ->
            if (testCase.type == TestType.Test) {
                restDocumentation.beforeTest(javaClass, testCase.name.testName)
                mockMvc = MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .apply<DefaultMockMvcBuilder>(
                        MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                    )
                    .build()
                val result = execute(testCase)
                restDocumentation.afterTest()
                result
            } else {
                execute(testCase)
            }
        }
    }
}


