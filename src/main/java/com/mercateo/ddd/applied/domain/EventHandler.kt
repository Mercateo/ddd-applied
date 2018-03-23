package com.mercateo.ddd.applied.domain

interface EventHandler {
    fun <T : Event> publish(event: T)

    fun pullUpdates(lastKnownSerialId: Long, eventReceiver: EventReceiver)

    fun pullUpdates(lastKnownSerialId: Long, aggregateId: AggregateId, eventReceiver: EventReceiver)

    fun subscribe(eventReceiver: EventReceiver)
}

interface EventReceiver {
    fun receive(event: Event)
}