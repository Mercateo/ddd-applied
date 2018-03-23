package com.mercateo.ddd.applied.domain

import io.vavr.control.Either
import io.vavr.kotlin.left
import java.math.BigDecimal


class Transactions(
        private val accounts: Accounts,
        private val eventHandler: EventHandler
) {

    fun create(creationData: TransactionCreationData) : Either<Failure<TransactionCause>, Transaction> {
        val failureCauses = mutableListOf<TransactionCause>()
        accounts.byId(creationData.sourceAccountId) ?: run {failureCauses.add(TransactionCause.SOURCE_ACCOUNT_NON_EXISTENT)}
        accounts.byId(creationData.targetAccountId) ?: run {failureCauses.add(TransactionCause.TARGET_ACCOUNT_NON_EXISTENT)}
        if (creationData.amount.compareTo(BigDecimal.ZERO) != 1) {
            failureCauses.add(TransactionCause.NON_POSITIVE_AMOUNT)
        }

        if (failureCauses.isNotEmpty()) {
            return left(Failure(*failureCauses.toTypedArray()))
        }

        val transactionId = TransactionId()
        eventHandler.publish(TransactionCreatedEvent(transactionId = transactionId, sourceAccountId = creationData.sourceAccountId, targetAccountId = creationData.targetAccountId, amount = creationData.amount ))

        return Either.right(Transaction(id=transactionId, sourceAccountId = creationData.sourceAccountId, targetAccountId = creationData.targetAccountId, amount = creationData.amount))
    }
}

data class TransactionCreationData(
        val sourceAccountId: AccountId,
        val targetAccountId: AccountId,
        val amount: BigDecimal
)

enum class TransactionCause {
    SOURCE_ACCOUNT_NON_EXISTENT,
    TARGET_ACCOUNT_NON_EXISTENT,
    NON_POSITIVE_AMOUNT
}

data class TransactionCreatedEvent(
        val transactionId: TransactionId,
        val sourceAccountId: AccountId,
        val targetAccountId: AccountId,
        val amount: BigDecimal
) : Event(sourceAccountId, targetAccountId)
