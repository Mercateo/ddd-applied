package com.mercateo.ddd.applied

import com.mercateo.ddd.applied.domain.Account
import com.mercateo.ddd.applied.domain.AccountId
import org.springframework.stereotype.Component

@Component
class ReadModel {

    fun accountById(id: AccountId): Account? {
        return null
    }
}