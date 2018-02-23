package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.read.ReadModel
import org.springframework.stereotype.Component

@Component
class Accounts(private val eventHandler: EventHandler, private val readModel: ReadModel) {
    fun byId(accountId: AccountId): Account? {
        return readModel.accountById(accountId)
    }

    fun getAll(): List<Account> {
        return readModel.getAccounts()
    }
}

data class AccountCreationData(
        val holder: AccountHolder
)

data class AccountCreatedEvent(
        val accountId: AccountId,
        val holder: AccountHolder
) : Event()

