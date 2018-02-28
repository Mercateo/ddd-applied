package com.mercateo.ddd.applied.adapter.pullviews

import com.mercateo.ddd.applied.adapter.event.InMemoryEventStore
import com.mercateo.ddd.applied.domain.*
import org.springframework.stereotype.Component

@Component
class AccountsPullView(val evStore: InMemoryEventStore, val eventHandler: EventHandler) {

    fun getAccount(accountId: AccountId): Account? {
        val st = evStore.getAll()
        var account: Account? = null
        st.forEach({
            when (it) {
                is AccountCreatedEvent -> {
                    if (it.accountId == accountId) {
                        account = Account.create(it, eventHandler)
                    }
                }
                is TransactionCreatedEvent -> {
                    account!!.apply(it)
                }
            }

        })
        return account
    }
}