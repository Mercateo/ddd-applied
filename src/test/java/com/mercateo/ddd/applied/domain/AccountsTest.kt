package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.EventHandler
import com.mercateo.ddd.applied.ReadModel
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
    lateinit var readModel: ReadModel

    @InjectMocks
    lateinit var uut: Accounts

    @Test
    fun shouldOpenNewAccount() {
        val holder = AccountHolder("foo")
        val data = AccountCreationData(holder)

        val result = uut.open(data)

        assertThat(result).isNotEmpty()
        val account = result.get()
        assertThat(account.holder).isEqualTo(holder)
        assertThat(account.balance).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun shouldReturnFailureForUnknownId() {
        val result = uut.byId(AccountId())

        assertThat(result).isEmpty()
        assertThat(result.left).isNotNull()
    }

    @Test
    fun shouldReturnAccountById() {
        val accountId = AccountId()
        val account = Account(id = accountId, balance = BigDecimal.ZERO, holder = AccountHolder("foo"))
        whenever(readModel.accountById(accountId)).thenReturn(account)

        val result = uut.byId(accountId)

        assertThat(result).isNotEmpty()
        assertThat(result.get()).isSameAs(account)
    }

    @Test
    fun shouldGetListOfAllAccounts() {
        val accounts = listOf(
                Account(id = AccountId(), balance = BigDecimal.ZERO, holder = AccountHolder("foo")),
                Account(id = AccountId(), balance = BigDecimal.ZERO, holder = AccountHolder("bar"))
        )
        whenever(readModel.getAccounts()).thenReturn(accounts)

        val result = uut.getAll()

        assertThat(result).extracting("holder").extracting("name").containsExactlyInAnyOrder("foo", "bar")
    }
}