package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.read.ReadModel
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class TransactionTest {

    @Mock
    private lateinit var accounts: Accounts

    @Mock
    private lateinit var eventHandler: EventHandler

    @Mock
    private lateinit var readModel: ReadModel


    @Test
    fun shouldCreateTransaction() {
        val sourceAccount = Account(AccountId(), BigDecimal.ZERO, AccountHolder("source"), eventHandler)
        val targetAccount = Account(AccountId(), BigDecimal.ZERO, AccountHolder("target"), eventHandler)
        whenever(accounts.byId(sourceAccount.id)).thenReturn(sourceAccount)
        whenever(accounts.byId(targetAccount.id)).thenReturn(targetAccount)

        val result = Transaction.create(TransactionCreationData(sourceAccountId = sourceAccount.id, targetAccountId = targetAccount.id, amount = BigDecimal(10)), accounts, eventHandler)

        assertThat(result).isNotEmpty
        val transaction = result.get()
        assertThat(transaction.id).isNotNull()
        assertThat(transaction.sourceAccountId).isEqualTo(sourceAccount.id)
        assertThat(transaction.targetAccountId).isEqualTo(targetAccount.id)
        assertThat(transaction.amount).isEqualTo(BigDecimal(10))

        verify(eventHandler).publish(TransactionCreatedEvent(transaction.id, transaction.sourceAccountId, transaction.targetAccountId, transaction.amount))
        verifyNoMoreInteractions(eventHandler)
    }

    @Test
    fun shouldAccumulateFailuresWhenCreatingTransaction() {
        val result = Transaction.create(TransactionCreationData(sourceAccountId = AccountId(), targetAccountId = AccountId(), amount = BigDecimal(-10)), accounts, eventHandler)

        assertThat(result).isEmpty()

        val failure = result.left
        assertThat(failure.causes).containsExactlyInAnyOrder(*TransactionErrorCause.values().copyOfRange(0, 3))

        verifyZeroInteractions(eventHandler)
    }
}