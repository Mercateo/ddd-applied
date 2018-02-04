package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.EventHandler
import com.mercateo.ddd.applied.ReadModel
import io.vavr.control.Either
import io.vavr.kotlin.right
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant

@Component
class Accounts(private val eventHandler: EventHandler, private val readModel: ReadModel) {
    fun open(creationData: AccountCreationData): Either<Failure, Account> {
        val accountId = AccountId()

        eventHandler.publish(AccountCreatedEvent(accountId = accountId, holder = creationData.holder))

        return Either.right(Account(id = accountId, balance = BigDecimal(0), holder = creationData.holder))
    }

    fun byId(accountId: AccountId): Either<Failure, Account> {
        return readModel.accountById(accountId)?.let(this::wrapAccount) ?: Either.left(Failure())
    }

    fun wrapAccount(account: Account): Either<Failure, Account> {
        return right(account)
    }

    fun getAll(): List<Account> {
        return readModel.getAccounts()
    }
}

class Failure {

}

data class AccountCreationData(val holder: AccountHolder)

data class AccountCreatedEvent(val accountId: AccountId, val holder: AccountHolder) : Event()

abstract class Event {
    val timestamp = Instant.now()
}
