package me.jimmyberg.ams.testsupport.restdocs

import io.kotest.core.test.TestType
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec
import org.springframework.restdocs.ManualRestDocumentation
// removed MockMvc REST Docs import; using WebTestClient REST Docs only
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.context.WebApplicationContext

abstract class RestDocsBehaviorSpec(
    protected val webApplicationContext: WebApplicationContext
) : CustomBehaviorSpec({}) {

    protected lateinit var mockMvc: MockMvc
    protected lateinit var webTestClient: WebTestClient
    private val restDocumentation = ManualRestDocumentation()

    init {
        aroundTest { (testCase, execute) ->
            if (testCase.type == TestType.Test) {
                restDocumentation.beforeTest(javaClass, testCase.name.testName)
                mockMvc = MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .build()
                webTestClient = MockMvcWebTestClient
                    .bindTo(mockMvc)
                    .filter(WebTestClientRestDocumentation.documentationConfiguration(restDocumentation))
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


