package me.jimmyberg.ams.test

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [HelloController::class])
class HelloControllerTest(
    @Autowired private val mockMvc: MockMvc
) {

    @Test
    fun `MockMvc 테스트`() {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/hello"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("Hello"))
            .andDo(MockMvcResultHandlers.print())
    }

}