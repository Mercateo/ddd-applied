package com.mercateo.ddd.applied.adapter.event

import com.mercateo.ddd.applied.domain.AccountCreatedEvent
import com.mercateo.ddd.applied.domain.AccountHolder
import com.mercateo.ddd.applied.domain.AccountId
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*


internal class InMemoryEventStoreTest {

    @Test
    fun getAll() {
        val uut = InMemoryEventStore()
        val ev = AccountCreatedEvent(accountId = AccountId(UUID.randomUUID()), holder = AccountHolder("test"))

        assertThat(uut.getAll()).isEmpty()

        uut.eventReceiver(ev)

        assertThat(uut.getAll()).containsOnly(ev)

    }
}