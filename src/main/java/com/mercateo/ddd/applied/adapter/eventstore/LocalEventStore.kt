package com.mercateo.ddd.applied.adapter.eventstore

import com.mercateo.ddd.applied.domain.AggregateId
import com.mercateo.ddd.applied.domain.Event
import com.mercateo.ddd.applied.domain.EventHandler
import com.mercateo.ddd.applied.domain.EventReceiver
import mu.KLogging
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class LocalEventStore : EventHandler {

    companion object : KLogging()

    private val serialId = AtomicLong()

    private val events: MutableList<Event> = mutableListOf()

    private val subscriptions = mutableSetOf<EventReceiver>()

    override fun <T : Event> publish(event: T) {
        logger.info { "publish($event)" }
        event.serialId = serialId.incrementAndGet()

        events.add(event)

        subscriptions.forEach { it.receive(event) }
    }

    override fun pullUpdates(lastKnownSerialId: Long, eventReceiver: EventReceiver) {
        logger.info { "pullUpdates($lastKnownSerialId, $eventReceiver)" }

        events.filter { it.serialId > lastKnownSerialId }
                .forEach { eventReceiver.receive(it) }
    }

    override fun pullUpdates(lastKnownSerialId: Long, aggregateId: AggregateId, eventReceiver: EventReceiver) {
        logger.info { "pullUpdates($lastKnownSerialId, $aggregateId, $eventReceiver" }

        events.filter { it.serialId > lastKnownSerialId }
                .filter { it is AggregateId && it.aggregateIds.contains(aggregateId) }
                .forEach { eventReceiver.receive(it) }
    }

    override fun subscribe(eventReceiver: EventReceiver) {
        logger.info { "subscribe($eventReceiver)" }

        subscriptions.add(eventReceiver)
    }
}