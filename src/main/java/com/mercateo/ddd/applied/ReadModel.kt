package com.mercateo.ddd.applied

import com.mercateo.ddd.applied.domain.Account
import com.mercateo.ddd.applied.domain.AccountCreatedEvent
import com.mercateo.ddd.applied.domain.AccountHolder
import com.mercateo.ddd.applied.domain.AccountId
import mu.KLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ReadModel {

    private data class MutableAccount(val accountId: AccountId, val holder: AccountHolder, val balance: BigDecimal = BigDecimal.ZERO)

    private val accounts = mutableMapOf<AccountId, MutableAccount>()

    companion object : KLogging()

    @EventListener
    fun eventReceiver(event: Any) {
        logger.info("eventReceiver({})", event)

        when (event) {
            is AccountCreatedEvent -> {
                accounts.put(event.accountId, MutableAccount(accountId = event.accountId, holder = event.holder))
            }
        }
    }

    fun accountById(id: AccountId): Account? = accounts[id]?.let(this::map)

    fun getAccounts(): List<Account> = accounts.values.map(this::map)

    private fun map(account: MutableAccount): Account = Account(account.accountId, account.balance, account.holder)

}