package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.EventHandler
import com.mercateo.ddd.applied.ReadModel
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
}