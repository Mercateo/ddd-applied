package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.adapter.pullviews.AccountsPullView
import org.springframework.stereotype.Component

@Component
class Accounts(private val eventHandler: EventHandler, private val pullView: AccountsPullView) {
    fun byId(accountId: AccountId): Account? {
        return pullView.getAccount(accountId)
    }
}


