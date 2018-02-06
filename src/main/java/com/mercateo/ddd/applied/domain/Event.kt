package com.mercateo.ddd.applied.domain

import java.time.Instant

abstract class Event {
    val timestamp = Instant.now()
}