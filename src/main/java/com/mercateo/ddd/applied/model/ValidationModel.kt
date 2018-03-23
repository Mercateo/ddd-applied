package com.mercateo.ddd.applied.model

import com.mercateo.ddd.applied.domain.Account
import com.mercateo.ddd.applied.domain.AccountId

interface ValidationModel {
    fun accountById(id: AccountId): Account?
}