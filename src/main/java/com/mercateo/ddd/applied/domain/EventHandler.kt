package com.mercateo.ddd.applied.domain

interface EventHandler {
    fun <T> publish(data: T)
}