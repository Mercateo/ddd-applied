package com.mercateo.ddd.applied.domain

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class TransactionsTest {

    @Mock
    private lateinit var accounts: Accounts

    @Mock
    private lateinit var eventHandler: EventHandler

    @InjectMocks
    private lateinit var uut: Transactions

    @Test
    fun shouldCreateTransaction() {
        val sourceAccount = Account(AccountId(), BigDecimal.ZERO, AccountHolder("source"))
        val targetAccount = Account(AccountId(), BigDecimal.ZERO, AccountHolder("target"))
        whenever(accounts.byId(sourceAccount.id)).thenReturn(sourceAccount)
        whenever(accounts.byId(targetAccount.id)).thenReturn(targetAccount)

        val result = uut.create(TransactionCreationData(sourceAccountId = sourceAccount.id, targetAccountId = targetAccount.id, amount = BigDecimal(10)))

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
        val result = uut.create(TransactionCreationData(sourceAccountId = AccountId(), targetAccountId = AccountId(), amount = BigDecimal(-10)))

        assertThat(result).isEmpty()

        val failure = result.left
        assertThat(failure.causes).containsExactlyInAnyOrder(*TransactionCause.values())

        verifyZeroInteractions(eventHandler)
    }
}