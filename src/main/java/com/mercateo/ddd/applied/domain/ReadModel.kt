package com.mercateo.ddd.applied.domain

interface ReadModel {
    fun getAccounts(): List<Account>
    fun accountById(id: AccountId): Account?
    fun transactionById(transactionId: TransactionId): Transaction?
}