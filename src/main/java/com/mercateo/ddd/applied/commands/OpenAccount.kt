package com.mercateo.ddd.applied.commands

import com.mercateo.ddd.applied.domain.Account
import com.mercateo.ddd.applied.domain.AccountCreationData
import com.mercateo.ddd.applied.domain.EventHandler
import com.mercateo.ddd.applied.domain.Failure
import io.vavr.control.Either
import io.vavr.kotlin.right
import org.springframework.stereotype.Component

@Component
class OpenAccount(val eventHandler: EventHandler) {

    fun execute(creationData: AccountCreationData) : Either<Failure<Nothing>, Account> {
        // TODO add initial transfer of bonus here
        return Account.create(creationData, eventHandler).let { right(it) }
    }
}