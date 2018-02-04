package com.mercateo.ddd.applied.adapter.model

import com.mercateo.ddd.applied.domain.AccountCreatedEvent
import com.mercateo.ddd.applied.domain.AccountHolder
import com.mercateo.ddd.applied.domain.AccountId
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class InMemoryReadModelTest {

    @InjectMocks
    lateinit var uut: InMemoryReadModel

    @Test
    fun shouldReturnAllAccounts() {
        assertThat(uut.getAccounts()).isEmpty()

        uut.eventReceiver(AccountCreatedEvent(AccountId(), AccountHolder("foo")))

        assertThat(uut.getAccounts()).hasSize(1)
    }

    @Test
    fun shouldReturnNullAtUnknownAccountId() {
        assertThat(uut.accountById(AccountId())).isNull()
    }

    @Test
    fun shouldReturnAccountById() {
        val accountId = AccountId()
        val holder = AccountHolder("foo")
        uut.eventReceiver(AccountCreatedEvent(accountId, holder))

        val result = uut.accountById(accountId)

        assertThat(result).isNotNull()

        assertThat(result?.id).isEqualTo(accountId)
        assertThat(result?.balance).isEqualTo(BigDecimal.ZERO)
        assertThat(result?.holder).isEqualTo(holder)
    }
}