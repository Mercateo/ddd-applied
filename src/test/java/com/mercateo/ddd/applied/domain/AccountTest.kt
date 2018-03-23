package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.model.ValidationModel
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class AccountTest {

    @Mock
    private lateinit var eventHandler: EventHandler

    @Mock
    private lateinit var validationModel: ValidationModel

    private lateinit var uut: Account

    @Before
    fun setUp() {
        uut = Account(AccountId(), BigDecimal(123.12), AccountHolder("holder"), eventHandler, validationModel)
    }

    @Test
    fun shouldUpdateAccountWithTransfer() {
        val targetAccountId = AccountId()
        whenever(validationModel.accountById(targetAccountId)).thenReturn(
                Account(targetAccountId, BigDecimal.ZERO, AccountHolder("target"), eventHandler, validationModel))

        val result = uut.transfer(BigDecimal(33.12), targetAccountId)

        assertThat(result).isNotEmpty
        val updatedAccount = result.get()

        assertThat(updatedAccount.balance).isCloseTo(BigDecimal(90), Percentage.withPercentage(0.001))
        assertThat(updatedAccount.id).isEqualTo(uut.id)
        assertThat(updatedAccount.holder).isEqualTo(uut.holder)

        val captor = argumentCaptor<TransactionCreatedEvent>()
        verify(eventHandler).publish(captor.capture())

        val transactionCreatedEvent = captor.firstValue
        assertThat(transactionCreatedEvent.sourceAccountId).isEqualTo(uut.id)
        assertThat(transactionCreatedEvent.targetAccountId).isEqualTo(targetAccountId)
        assertThat(transactionCreatedEvent.amount).isEqualTo(BigDecimal(33.12))
        assertThat(transactionCreatedEvent.transactionId).isNotNull()
    }
}