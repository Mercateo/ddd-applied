package com.mercateo.ddd.applied.adapter.model

import com.mercateo.ddd.applied.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class InMemoryReadModelTest {

    @Mock
    private lateinit var eventHandler: EventHandler

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

    @Test
    fun shouldReturnTransactionById() {
        val transactionId = TransactionId()
        val sourceAccountId = AccountId()
        uut.eventReceiver(AccountCreatedEvent(sourceAccountId, AccountHolder("source")))
        val targetAccountId = AccountId()
        uut.eventReceiver(AccountCreatedEvent(targetAccountId, AccountHolder("target")))
        uut.eventReceiver(TransactionCreatedEvent(transactionId, sourceAccountId, targetAccountId, BigDecimal(123)))

        val result = uut.transactionById(transactionId)

        assertThat(result).isNotNull()

        assertThat(result!!.id).isEqualTo(transactionId)
        assertThat(result.sourceAccountId).isEqualTo(sourceAccountId)
        assertThat(result.targetAccountId).isEqualTo(targetAccountId)
        assertThat(result.amount).isEqualTo(BigDecimal(123))
    }

    @Test
    fun shouldApplyTransaction() {
        val transactionId = TransactionId()
        val sourceAccountId = AccountId()
        uut.eventReceiver(AccountCreatedEvent(sourceAccountId, AccountHolder("source")))
        val targetAccountId = AccountId()
        uut.eventReceiver(AccountCreatedEvent(targetAccountId, AccountHolder("target")))

        uut.eventReceiver(TransactionCreatedEvent(transactionId, sourceAccountId, targetAccountId, BigDecimal(123)))

        assertThat(uut.accountById(sourceAccountId)?.balance).isEqualTo(BigDecimal(-123))
        assertThat(uut.accountById(targetAccountId)?.balance).isEqualTo(BigDecimal(123))
    }
}