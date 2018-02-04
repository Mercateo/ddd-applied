package com.mercateo.ddd.applied.domain

import com.mercateo.ddd.applied.domain.Account
import com.mercateo.ddd.applied.domain.AccountId

interface ReadModel {
    fun getAccounts(): List<Account>
    fun accountById(id: AccountId): Account?
}