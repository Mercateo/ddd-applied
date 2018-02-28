package com.mercateo.ddd.applied.adapter.pullviews

import com.mercateo.ddd.applied.adapter.event.InMemoryEventStore
import com.mercateo.ddd.applied.domain.*
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal
import java.util.*

@RunWith(MockitoJUnitRunner::class)
internal class AccountsPullViewTest {
    @Mock
    lateinit var evStore: InMemoryEventStore

    @Mock
    lateinit var eventHandler: EventHandler

    @InjectMocks
    lateinit var uut: AccountsPullView

    @Test
    fun getAccount_noEvents() {
        whenever(evStore.getAll()).thenReturn(Arrays.stream(arrayOf()))
        assertThat(uut.getAccount(accountId = AccountId(UUID.randomUUID()))).isNull()
    }

    @Test
    fun getAccount_noEvents_for_Account() {
        whenever(evStore.getAll()).thenReturn(Arrays.stream(arrayOf(AccountCreatedEvent(accountId = AccountId(UUID.randomUUID()), holder = AccountHolder("test")))))
        assertThat(uut.getAccount(accountId = AccountId(UUID.randomUUID()))).isNull()
    }

    @Test
    fun getAccount_Events_for_Account() {
        val accountId = AccountId(UUID.randomUUID())
        whenever(evStore.getAll()).thenReturn(Arrays.stream(arrayOf(AccountCreatedEvent(accountId, holder = AccountHolder("test")))))
        assertThat(uut.getAccount(accountId)?.id).isEqualTo(accountId)
    }

    @Test
    fun getAccount_Events_for_Account_andTransactions() {
        val accountId = AccountId(UUID.randomUUID())
        whenever(evStore.getAll()).thenReturn(Arrays.stream(arrayOf(AccountCreatedEvent(accountId, holder = AccountHolder("test")), TransactionCreatedEvent(TransactionId(UUID.randomUUID()), accountId, AccountId(UUID.randomUUID()), BigDecimal.TEN))))
        val acc = uut.getAccount(accountId)
        assertThat(acc?.id).isEqualTo(accountId)
        assertThat(acc?.balance).isEqualTo(BigDecimal(-10))
    }

    @Test(expected = KotlinNullPointerException::class)
    fun getAccount_Events_in_Wrong_Order() {
        val accountId = AccountId(UUID.randomUUID())
        whenever(evStore.getAll()).thenReturn(Arrays.stream(arrayOf(TransactionCreatedEvent(TransactionId(UUID.randomUUID()), accountId, AccountId(UUID.randomUUID()), BigDecimal.TEN))))
        assertThat(uut.getAccount(accountId)?.id).isEqualTo(accountId)
    }
}