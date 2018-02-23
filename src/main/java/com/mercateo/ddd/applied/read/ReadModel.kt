package com.mercateo.ddd.applied.read

import com.mercateo.ddd.applied.domain.Account
import com.mercateo.ddd.applied.domain.AccountId
import com.mercateo.ddd.applied.domain.Transaction
import com.mercateo.ddd.applied.domain.TransactionId

interface ReadModel {
    fun getAccounts(): List<Account>
    fun accountById(id: AccountId): Account?
    fun transactionById(transactionId: TransactionId): Transaction?
}