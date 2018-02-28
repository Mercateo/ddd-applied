package com.mercateo.ddd.applied.adapter.event

import com.mercateo.ddd.applied.domain.Event
import mu.KLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Stream

@Component
class InMemoryEventStore {
    companion object : KLogging()

    private val events = LinkedList<Event>()

    @EventListener
    fun eventReceiver(event: Event) {
        logger.info("eventStore storing {}", event)
        events.add(event)
    }

    fun getAll(): Stream<Event> {
        return Collections.unmodifiableList(events).stream()
    }
}

