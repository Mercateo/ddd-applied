package com.mercateo.ddd.applied.adapter.event

import com.mercateo.ddd.applied.domain.AccountCreatedEvent
import com.mercateo.ddd.applied.domain.AccountHolder
import com.mercateo.ddd.applied.domain.AccountId
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.context.ApplicationEventPublisher

@RunWith(MockitoJUnitRunner::class)
class ApplicationLocalEventHandlerTest {

    @Mock
    lateinit var publisher: ApplicationEventPublisher

    @InjectMocks
    lateinit var uut : ApplicationLocalEventHandler

    @Test
    fun shouldPublishEvent() {
        val event = AccountCreatedEvent(AccountId(), AccountHolder("foo"))

        uut.publish(event)

        verify(publisher).publishEvent(event)
    }
}