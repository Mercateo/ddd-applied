package com.mercateo.ddd.applied

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
@RunWith(SpringRunner::class)
class MainTest {

    @Autowired
    lateinit var application: Application

    @Test
    fun shouldCreateApplication() {
        assertThat(application).isNotNull()
    }
}