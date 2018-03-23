package com.mercateo.ddd.applied.adapter.model

import com.mercateo.ddd.applied.domain.*
import com.mercateo.ddd.applied.model.ReadModel
import com.mercateo.ddd.applied.model.ValidationModel
import mu.KLogging
import org.springframework.stereotype.Component
import java.math.BigDecimal
import javax.annotation.PostConstruct

@Component
class InMemoryReadModel(private val eventHandler: EventHandler, private val validationModel: ValidationModel) : ReadModel, EventReceiver {

    companion object : KLogging()

    private val accounts = mutableMapOf<AccountId, MutableAccount>()

    private val transactions = mutableMapOf<TransactionId, Transaction>()

    @PostConstruct
    fun init() {
        eventHandler.subscribe(this)
    }

    override fun accounts(): List<Account> {
        return accounts.values.map(::map)
    }

    override fun accountById(id: AccountId): Account? {
        return accounts[id]?.let(::map)
    }

    private fun map(account: MutableAccount): Account = Account(account.accountId, account.balance, account.holder, eventHandler, validationModel)

    override fun transactions(): List<Transaction> {
        return transactions.values.toList()
    }

    override fun transactionById(id: TransactionId): Transaction? {
        return transactions[id]
    }

    override fun receive(event: Event) {
        logger.info("eventReceiver({})", event)

        when (event) {
            is AccountCreatedEvent -> {
                accounts[event.accountId] = MutableAccount(event.accountId, event.holder, BigDecimal.ZERO)
            }

            is TransactionCreatedEvent -> {
                transactions[event.transactionId] = Transaction(event.transactionId, event.sourceAccountId, event.targetAccountId, event.amount)

                accounts[event.sourceAccountId]?.also { it.balance -= event.amount }
                        ?: logger.warn { "no source account ${event.sourceAccountId}" }
                accounts[event.targetAccountId]?.also { it.balance += event.amount }
                        ?: logger.warn { "no target account ${event.targetAccountId}" }
            }
        }
    }

    private data class MutableAccount(
            val accountId: AccountId,
            val holder: AccountHolder,
            var balance: BigDecimal = BigDecimal.ZERO)
}

