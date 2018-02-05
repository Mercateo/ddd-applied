package com.mercateo.ddd.applied.adapter.rest

import com.mercateo.ddd.applied.domain.Account
import com.mercateo.ddd.applied.domain.AccountCreationData
import com.mercateo.ddd.applied.domain.AccountId
import com.mercateo.ddd.applied.domain.Accounts
import com.mercateo.ddd.applied.domain.usecase.OpenAccount
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*


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
                        {
                            val uri = ServletUriComponentsBuilder.fromCurrentRequest().path(
                                    "/{id}").buildAndExpand(it.id.id).toUri();
                            ResponseEntity.created(uri).build()
                        }
                )
    }

    @RequestMapping(method = [RequestMethod.GET])
    fun getAccounts(): ResponseEntity<List<Account>> {
        return accounts.getAll().let { ResponseEntity.ok(it) }
    }

    @RequestMapping("/{accountId}", method = [RequestMethod.GET])
    fun getAccount(@PathVariable("accountId") accountIdString: String): ResponseEntity<Account> {
        val accountId = AccountId(UUID.fromString(accountIdString))

        return accounts.byId(accountId)
                .fold(
                        { ResponseEntity.notFound().build() },
                        { ResponseEntity.ok(it) }
                )
    }
}