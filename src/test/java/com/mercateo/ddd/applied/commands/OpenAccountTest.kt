package com.mercateo.ddd.applied.commands

import com.mercateo.ddd.applied.domain.*
import com.mercateo.ddd.applied.model.ValidationModel
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal


@RunWith(MockitoJUnitRunner::class)
class OpenAccountTest {

    @Mock
    private lateinit var accounts: Accounts

    @Mock
    private lateinit var eventHandler: EventHandler

    @Mock
    private lateinit var validationModel: ValidationModel

    @InjectMocks
    private lateinit var uut: OpenAccount

    @Test
    fun shouldOpenAccount() {
        val holder = AccountHolder("foo")
        val data = AccountCreationData(holder)
        val account = Account(AccountId(), BigDecimal.ZERO, holder, eventHandler, validationModel)
        whenever(accounts.create(data)).thenReturn(account)

        val result = uut.execute(data)

        assertThat(result.get()).isSameAs(account)
    }
}