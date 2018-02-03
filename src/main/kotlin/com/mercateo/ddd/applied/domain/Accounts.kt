package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.ReadModel
import io.vavr.control.Either
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class Accounts(private val readModel: ReadModel) {
    fun open(creationData: AccountCreationData): Either<Failure, Account> {
        val accountId = AccountId()

        return Either.right(Account(id = accountId, balance = BigDecimal(0), holder = creationData.holder))
    }

    fun byId(accountId: AccountId): Either<Failure, Account> {
        return readModel.accountById(accountId)?.let(this::wrapAccount) ?: Either.left(Failure())
    }

    fun wrapAccount(account: Account): Either<Failure, Account> {
        return Either.right(account)
    }
}

class Failure {

}

data class AccountCreationData(val holder: AccountHolder)
