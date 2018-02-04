package com.mercateo.ddd.applied.adapter.rest

import com.mercateo.ddd.applied.domain.Account
import com.mercateo.ddd.applied.domain.AccountCreationData
import com.mercateo.ddd.applied.domain.Accounts
import com.mercateo.ddd.applied.usecase.OpenAccount
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/accounts")
class AccountController(val openAccount: OpenAccount, val accounts: Accounts) {

    companion object : KLogging()

    @RequestMapping(method = [RequestMethod.POST])
    fun createAccount(@RequestBody data: AccountCreationData): ResponseEntity<Unit> {
        return openAccount
                .execute(data)
                .peek { logger.info { "successful: $it" } }
                .peekLeft { logger.info { "failure: $it" } }
                .fold(
                        { ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() },
                        { ResponseEntity.ok().build() }
                )
    }

    @RequestMapping(method = [RequestMethod.GET])
    fun getAccounts(): ResponseEntity<List<Account>> {
        return accounts.getAll().let { ResponseEntity.ok(it) }
    }
}