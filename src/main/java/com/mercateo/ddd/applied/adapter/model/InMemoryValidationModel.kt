package com.mercateo.ddd.applied.adapter.model

import com.mercateo.ddd.applied.domain.*
import com.mercateo.ddd.applied.model.ValidationModel
import mu.KLogging
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class InMemoryValidationModel(private val eventHandler: EventHandler) : ValidationModel {

    companion object : KLogging()

    private val accounts = mutableMapOf<AccountId, MutableAccount>()

    override fun accountById(accountId: AccountId): Account? {
        val receiver = AccountEventReceiver(accountId)

        val account = accounts[accountId]
        val serialId = if (account != null) account.serialId else 0

        eventHandler.pullUpdates(serialId, accountId, receiver)

        return accounts[accountId]?.let(::map)
    }

    private fun map(account: MutableAccount): Account = Account(account.accountId, account.balance, account.holder, eventHandler, this)

    inner class AccountEventReceiver(private val accountId: AccountId) : EventReceiver {

        override fun receive(event: Event) {
            logger.info("eventReceiver({})", event)

            if (event.serialId == 0L) {
                logger.warn { "unable to use event without serial id $event" }
                return
            }

            when (event) {
                is AccountCreatedEvent -> {
                    accounts[accountId] = MutableAccount(accountId, event.holder, BigDecimal.ZERO, event.serialId)
                }

                is TransactionCreatedEvent -> {
                    accounts[accountId]?.also { account -> account.balance += if (accountId == event.sourceAccountId) -event.amount else event.amount }
                }
            }
        }
    }

    private data class MutableAccount(
            val accountId: AccountId,
            val holder: AccountHolder,
            var balance: BigDecimal = BigDecimal.ZERO,
            var serialId: Long)
}

