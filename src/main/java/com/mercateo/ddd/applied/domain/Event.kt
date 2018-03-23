package com.mercateo.ddd.applied.domain

import java.time.Instant

abstract class Event(
        vararg aggregateIds: AggregateId
) {
    val aggregateIds = aggregateIds.toSet()

    val timestamp = Instant.now()

    var serialId: Long = 0
        @Synchronized
        set(value) {
            if (serialId != 0L) {
                throw IllegalStateException("serialId is already set")
            }
            field = value
        }
}