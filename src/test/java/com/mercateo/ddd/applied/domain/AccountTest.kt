package com.mercateo.ddd.applied.domain

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal
import java.time.Instant

@RunWith(MockitoJUnitRunner::class)
class AccountTest {

    @Mock
    private lateinit var eventHandler: EventHandler

    @Mock
    private lateinit var accounts: Accounts

    private lateinit var uut: Account

    @Before
    fun setUp() {
        uut = Account(AccountId(), BigDecimal(123.12), AccountHolder("holder"), eventHandler)
    }

    @Test
    fun shouldUpdateAccountWithTransfer() {
        val targetAccount = Account(id = AccountId(), eventHandler = eventHandler, balance = BigDecimal.ZERO, holder = AccountHolder("test"))
        val targetAccountId = targetAccount.id
        whenever(accounts.byId(targetAccountId)).thenReturn(
                targetAccount)
        whenever(accounts.byId(uut.id)).thenReturn(
                uut)

        val result = uut.transfer(BigDecimal(33.12), targetAccount, accounts)

        assertThat(result).isNotEmpty

        val captor = argumentCaptor<TransactionCreatedEvent>()
        verify(eventHandler).publish(captor.capture())

        val transactionCreatedEvent = captor.firstValue
        assertThat(transactionCreatedEvent.sourceAccountId).isEqualTo(uut.id)
        assertThat(transactionCreatedEvent.targetAccountId).isEqualTo(targetAccount.id)
        assertThat(transactionCreatedEvent.amount).isEqualTo(BigDecimal(33.12))
        assertThat(transactionCreatedEvent.transactionId).isNotNull()
    }

    @Test
    fun shouldOpenNewAccount() {
        val holder = AccountHolder("foo")
        val data = AccountCreationData(holder)
        val startTime = Instant.now()

        val account = Account.create(data, eventHandler)

        assertThat(account.holder).isEqualTo(holder)
        assertThat(account.balance).isEqualTo(BigDecimal.ZERO)
        val captor = argumentCaptor<AccountCreatedEvent>()
        verify(eventHandler).publish(captor.capture())

        val event = captor.firstValue
        assertThat(event.accountId).isEqualTo(account.id)
        assertThat(event.holder).isEqualTo(holder)
        assertThat(event.timestamp).isAfterOrEqualTo(startTime)
        assertThat(event.timestamp).isBeforeOrEqualTo(Instant.now())
    }
}