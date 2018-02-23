package com.mercateo.ddd.applied.domain

import com.fasterxml.jackson.annotation.JsonUnwrapped
import io.vavr.control.Either
import io.vavr.kotlin.left
import java.math.BigDecimal
import java.util.*


data class Transaction(
        val id: TransactionId,
        val sourceAccountId: AccountId,
        val targetAccountId: AccountId,
        val amount: BigDecimal
) {
    fun isSource(accId: AccountId): Boolean {
        return sourceAccountId.equals(accId)
    }

    fun isTarget(accId: AccountId): Boolean {
        return targetAccountId.equals(accId)
    }

    companion object Factory {
        fun create(creationData: TransactionCreationData, accounts: Accounts, eventHandler: EventHandler): Either<Failure<TransactionErrorCause>, Transaction> {
            val failureCauses = mutableListOf<TransactionErrorCause>()
            accounts.byId(creationData.sourceAccountId)
                    ?: run { failureCauses.add(TransactionErrorCause.SOURCE_ACCOUNT_NON_EXISTENT) }
            accounts.byId(creationData.targetAccountId)
                    ?: run { failureCauses.add(TransactionErrorCause.TARGET_ACCOUNT_NON_EXISTENT) }
            if (creationData.amount.compareTo(BigDecimal.ZERO) != 1) {
                failureCauses.add(TransactionErrorCause.NON_POSITIVE_AMOUNT)
            }

            if (failureCauses.isNotEmpty()) {
                return left(Failure(*failureCauses.toTypedArray()))
            }

            val transactionId = TransactionId()
            eventHandler.publish(TransactionCreatedEvent(transactionId = transactionId, sourceAccountId = creationData.sourceAccountId, targetAccountId = creationData.targetAccountId, amount = creationData.amount))

            return Either.right(Transaction(id = transactionId, sourceAccountId = creationData.sourceAccountId, targetAccountId = creationData.targetAccountId, amount = creationData.amount))
        }

        fun create(ev: TransactionCreatedEvent): Transaction {
            return Transaction(id = ev.transactionId, targetAccountId = ev.targetAccountId, sourceAccountId = ev.sourceAccountId, amount = ev.amount)
        }
    }
}
data class TransactionId(@JsonUnwrapped val id: UUID) {
    constructor() : this(UUID.randomUUID())
}


data class TransactionCreationData(
        val sourceAccountId: AccountId,
        val targetAccountId: AccountId,
        val amount: BigDecimal
)

enum class TransactionErrorCause {
    SOURCE_ACCOUNT_NON_EXISTENT,
    TARGET_ACCOUNT_NON_EXISTENT,
    NON_POSITIVE_AMOUNT,
    SOURCE_AMOUNT_NOT_SUFFICIENT
}

data class TransactionCreatedEvent(
        val transactionId: TransactionId,
        val sourceAccountId: AccountId,
        val targetAccountId: AccountId,
        val amount: BigDecimal
) : Event()