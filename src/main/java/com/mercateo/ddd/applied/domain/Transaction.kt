package com.mercateo.ddd.applied.domain

import com.fasterxml.jackson.annotation.JsonUnwrapped
import java.math.BigDecimal
import java.util.*


data class Transaction(
        val id: TransactionId,
        val sourceAccountId: AccountId,
        val targetAccountId: AccountId,
        val amount: BigDecimal
)

data class TransactionId(@JsonUnwrapped val id: UUID) {
    constructor() : this(UUID.randomUUID())
}