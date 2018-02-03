package com.mercateo.ddd.applied

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class Application {
    @Bean
    fun objectMapper() = ObjectMapper().apply {
        registerModule(KotlinModule())
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}

