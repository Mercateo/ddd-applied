package com.mercateo.ddd.applied.domain

import io.vavr.control.Either
import java.math.BigDecimal
import java.util.*

data class AccountId(
        val id: UUID
) {
    constructor() : this(UUID.randomUUID())
}

data class AccountHolder(
        val name: String
)

data class Account(
        val id: AccountId,
        var balance: BigDecimal,
        val holder: AccountHolder,
        val eventHandler: EventHandler
) {
    private val transactions = LinkedList<Transaction>()

    fun transfer(amount: BigDecimal, targetAccount: Account, accounts: Accounts): Either<Failure<TransactionErrorCause>, TransactionId> {

        if (balance >= amount) {
            val creationData = TransactionCreationData(amount = amount, sourceAccountId = id, targetAccountId = targetAccount.id)
            val trans = Transaction.create(creationData = creationData, accounts = accounts, eventHandler = eventHandler)
            transactions.add(trans.get())
            return trans.map { t -> t.id }
        } else {
            return Either.left(Failure(TransactionErrorCause.SOURCE_AMOUNT_NOT_SUFFICIENT))
        }
    }

    fun apply(ev: TransactionCreatedEvent) {
        val tr = Transaction.create(ev)
        transactions.add(tr)
        if (tr.isSource(id)) {
            balance = balance.minus(tr.amount)
        } else {
            balance = balance.plus(tr.amount)
        }
    }

    companion object Factory {
        fun create(creationData: AccountCreationData, eventHandler: EventHandler): Account {
            val accountId = AccountId()

            val ev = AccountCreatedEvent(accountId = accountId, holder = creationData.holder)

            eventHandler.publish(ev)

            return create(ev, eventHandler)
        }

        fun create(ev: AccountCreatedEvent, eventHandler: EventHandler): Account {
            return Account(
                    id = ev.accountId,
                    balance = BigDecimal(0),
                    holder = ev.holder,
                    eventHandler = eventHandler)
        }

    }
}

