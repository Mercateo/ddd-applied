package com.mercateo.ddd.applied.adapter.model

import com.mercateo.ddd.applied.domain.*
import com.mercateo.ddd.applied.model.ValidationModel
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

    @Mock
    private lateinit var validationModel: ValidationModel

    @InjectMocks
    lateinit var uut: InMemoryReadModel

    @Test
    fun shouldReturnAllAccounts() {
        assertThat(uut.accounts()).isEmpty()

        uut.receive(AccountCreatedEvent(AccountId(), AccountHolder("foo")))

        assertThat(uut.accounts()).hasSize(1)
    }

    @Test
    fun shouldReturnNullAtUnknownAccountId() {
        assertThat(uut.accountById(AccountId())).isNull()
    }

    @Test
    fun shouldReturnAccountById() {
        val accountId = AccountId()
        val holder = AccountHolder("foo")
        uut.receive(AccountCreatedEvent(accountId, holder))

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
        uut.receive(AccountCreatedEvent(sourceAccountId, AccountHolder("source")))
        val targetAccountId = AccountId()
        uut.receive(AccountCreatedEvent(targetAccountId, AccountHolder("target")))
        uut.receive(TransactionCreatedEvent(transactionId, sourceAccountId, targetAccountId, BigDecimal(123)))

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
        uut.receive(AccountCreatedEvent(sourceAccountId, AccountHolder("source")))
        val targetAccountId = AccountId()
        uut.receive(AccountCreatedEvent(targetAccountId, AccountHolder("target")))

        uut.receive(TransactionCreatedEvent(transactionId, sourceAccountId, targetAccountId, BigDecimal(123)))

        assertThat(uut.accountById(sourceAccountId)?.balance).isEqualTo(BigDecimal(-123))
        assertThat(uut.accountById(targetAccountId)?.balance).isEqualTo(BigDecimal(123))
    }
}