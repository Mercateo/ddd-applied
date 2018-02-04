package com.mercateo.ddd.applied

import mu.KLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class EventHandler(private val eventPublisher: ApplicationEventPublisher) {

    companion object : KLogging()

    fun <T> publish(data: T) {
        logger.info("publish({})", data)

        eventPublisher.publishEvent(data)
    }
}
