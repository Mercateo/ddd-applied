package com.mercateo.ddd.applied.domain

import java.math.BigDecimal
import java.util.*

data class AccountId(
        val id: UUID
) {
    constructor() : this(UUID.randomUUID())
}

data class AccountHolder(
        val name: String
)

data class Account(
        val id: AccountId,
        val balance: BigDecimal,
        val holder: AccountHolder
)

