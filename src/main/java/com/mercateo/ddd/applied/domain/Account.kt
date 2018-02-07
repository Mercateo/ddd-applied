package com.mercateo.ddd.applied.domain

import io.vavr.control.Either
import io.vavr.control.Option
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
        val balance: BigDecimal,
        val holder: AccountHolder,
        private val eventHandler: EventHandler,
        private val readModel: ReadModel
) {
    fun transfer(amount: BigDecimal, targetAccountId: AccountId): Either<Failure<TransactionCause>, Account> {
        return Option.of(readModel.accountById(targetAccountId))
                .toEither(Failure(TransactionCause.TARGET_ACCOUNT_NON_EXISTENT))
                .map { TransactionCreatedEvent(TransactionId(), id, targetAccountId, amount) }
                .peek { eventHandler.publish(it) }
                .map { copy(balance = balance - amount) }
    }
}

