package me.jimmyberg.ams.hello

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [HelloController::class])
class HelloControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val mapper: ObjectMapper
) {

    @Test
    fun `MockMvc GET 테스트`() {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/hello"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("Hello"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `MockMvc POST 테스트`() {
        val request = Hello(text = "World")
        val body = mapper.writeValueAsString(request)
        mockMvc
            .perform(MockMvcRequestBuilders.post("/api/hello").content(body).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `MockMvc POST JSON 응답 테스트`() {
        val request = Hello(text = "World")
        val body = mapper.writeValueAsString(request)
        mockMvc
            .perform(MockMvcRequestBuilders.post("/api/hello/json").content(body).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("text").value("Hello World !"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `MockMvc GET 모던-패턴 테스트`() {
    	mockMvc
            .get("/api/hello")
            .andExpect {
                status { isOk() }
                content { string("Hello") }
            }
            .andDo { print() }
    }
    @Test
    fun `MockMvc POST 모던-패턴 테스트`() {
        val request = Hello(text = "World")
        val body = mapper.writeValueAsString(request)

        mockMvc
            .post("/api/hello/json") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }
            .andExpect {
                status { isOk() }
                content { jsonPath("text") { value("Hello World !") } }
            }
            .andDo { print() }
    }

}