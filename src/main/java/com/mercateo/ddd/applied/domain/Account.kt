package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.model.ValidationModel
import io.vavr.control.Either
import io.vavr.control.Option
import java.math.BigDecimal
import java.util.*

data class AccountId(
        override val id: UUID
) : AggregateId {
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
        private val validationModel: ValidationModel
) {
    fun transfer(amount: BigDecimal, targetAccountId: AccountId): Either<Failure<TransactionCause>, Account> {
        return Option.of(validationModel.accountById(targetAccountId))
                .toEither(Failure(TransactionCause.TARGET_ACCOUNT_NON_EXISTENT))
                .map { TransactionCreatedEvent(TransactionId(), id, targetAccountId, amount) }
                .peek { eventHandler.publish(it) }
                .map { copy(balance = balance - amount) }
    }
}

