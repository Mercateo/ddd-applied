package com.mercateo.ddd.applied.model

import com.mercateo.ddd.applied.domain.Account
import com.mercateo.ddd.applied.domain.AccountId
import com.mercateo.ddd.applied.domain.Transaction
import com.mercateo.ddd.applied.domain.TransactionId

interface ReadModel {
    fun accounts(): List<Account>
    fun accountById(id: AccountId): Account?
    fun transactions(): List<Transaction>
    fun transactionById(id: TransactionId): Transaction?
}