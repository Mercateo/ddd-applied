package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.model.ReadModel
import com.mercateo.ddd.applied.model.ValidationModel
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class Accounts(private val eventHandler: EventHandler, private val readModel: ReadModel, private val validationModel: ValidationModel) {
    fun create(creationData: AccountCreationData): Account {
        val accountId = AccountId()

        eventHandler.publish(AccountCreatedEvent(accountId = accountId, holder = creationData.holder))

        return Account(
                id = accountId,
                balance = BigDecimal(0),
                holder = creationData.holder,
                eventHandler = eventHandler,
                validationModel = validationModel)
    }

    fun byId(accountId: AccountId): Account? {
        return readModel.accountById(accountId)
    }

    fun getAll(): List<Account> {
        return readModel.accounts()
    }
}

data class AccountCreationData(
        val holder: AccountHolder
)

data class AccountCreatedEvent(
        val accountId: AccountId,
        val holder: AccountHolder
) : Event(accountId)

