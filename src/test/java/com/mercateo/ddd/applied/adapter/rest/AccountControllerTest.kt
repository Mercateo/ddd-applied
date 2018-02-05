package com.mercateo.ddd.applied.adapter.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercateo.ddd.applied.Application
import com.mercateo.ddd.applied.domain.AccountCreationData
import com.mercateo.ddd.applied.domain.AccountHolder
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Application::class])
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun shouldCreateNewAccount() {
        mockMvc.perform(get("/accounts"))
                .andExpect(jsonPath("$").isEmpty)
                .andExpect(status().isOk)

        val creationData = AccountCreationData(AccountHolder("foo"))
        mockMvc.perform(post("/accounts")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsBytes(creationData)))
                .andExpect(status().isCreated)

        mockMvc.perform(get("/accounts"))
                .andExpect(jsonPath("$").isNotEmpty)
                .andExpect(status().isOk)
    }
}