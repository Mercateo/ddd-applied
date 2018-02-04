package com.mercateo.ddd.applied.usecase

import com.mercateo.ddd.applied.domain.*
import com.nhaarman.mockito_kotlin.whenever
import io.vavr.kotlin.right
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
    lateinit var accounts: Accounts

    @InjectMocks
    lateinit var uut: OpenAccount

    @Test
    fun shouldOpenAccount() {
        val holder = AccountHolder("foo")
        val data = AccountCreationData(holder)
        val account = Account(AccountId(), BigDecimal.ZERO, holder)
        whenever(accounts.open(data)).thenReturn(right(account))

        val result = uut.execute(data)

        assertThat(result.get()).isSameAs(account)
    }
}