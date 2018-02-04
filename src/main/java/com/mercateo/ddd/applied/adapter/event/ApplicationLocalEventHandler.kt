package com.mercateo.ddd.applied.adapter.event

import com.mercateo.ddd.applied.domain.EventHandler
import mu.KLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ApplicationLocalEventHandler(private val eventPublisher: ApplicationEventPublisher) : EventHandler {

    companion object : KLogging()

    override fun <T> publish(data: T) {
        logger.info("publish({})", data)

        eventPublisher.publishEvent(data)
    }
}
