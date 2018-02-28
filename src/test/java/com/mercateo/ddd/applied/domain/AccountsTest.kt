package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.adapter.pullviews.AccountsPullView
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal

@RunWith(MockitoJUnitRunner::class)
class AccountsTest {

    @Mock
    lateinit var eventHandler: EventHandler

    @Mock
    lateinit var pullView: AccountsPullView

    @InjectMocks
    lateinit var uut: Accounts

    @Test
    fun shouldReturnFailureForUnknownId() {
        val result = uut.byId(AccountId())

        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnAccountById() {
        val accountId = AccountId()
        val account = Account(id = accountId, balance = BigDecimal.ZERO, holder = AccountHolder("foo"),
                eventHandler = eventHandler)
        whenever(pullView.getAccount(accountId)).thenReturn(account)

        val result = uut.byId(accountId)

        assertThat(result).isNotNull()
        assertThat(result).isSameAs(account)
    }
}