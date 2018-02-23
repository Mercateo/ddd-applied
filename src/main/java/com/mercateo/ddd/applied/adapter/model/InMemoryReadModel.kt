package com.mercateo.ddd.applied.adapter.model


import com.mercateo.ddd.applied.domain.*
import com.mercateo.ddd.applied.read.ReadModel
import mu.KLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class InMemoryReadModel(private val eventHandler: EventHandler) : ReadModel {

    companion object : KLogging()

    private data class MutableAccount(val accountId: AccountId, val holder: AccountHolder, var balance: BigDecimal = BigDecimal.ZERO)

    private val accounts = mutableMapOf<AccountId, MutableAccount>()

    private val transactions = mutableMapOf<TransactionId, Transaction>()

    @EventListener
    fun eventReceiver(event: Any) {
        logger.info("eventReceiver({})", event)

        when (event) {
            is AccountCreatedEvent -> {
                accounts.put(event.accountId, MutableAccount(accountId = event.accountId, holder = event.holder))
            }
            is TransactionCreatedEvent -> {
                transactions[event.transactionId] = Transaction(event.transactionId, event.sourceAccountId, event.targetAccountId, event.amount)

                accounts[event.sourceAccountId]!!.balance -= event.amount
                accounts[event.targetAccountId]!!.balance += event.amount
            }
        }
    }

    override fun accountById(id: AccountId): Account? = accounts[id]?.let(this::map)

    override fun getAccounts(): List<Account> = accounts.values.map(this::map)

    private fun map(account: MutableAccount): Account = Account(account.accountId, account.balance, account.holder, eventHandler)

    override fun transactionById(id: TransactionId) = transactions[id]
}