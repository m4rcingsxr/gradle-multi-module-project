package com.example.appaccounts.resource

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts")
class AccountResource {

    private val accounts: List<Account> = listOf(
        Account(1, "Alice", "alice@example.com"),
        Account(2, "Bob", "bob@example.com")
    )

    @GetMapping
    fun listAccounts(): List<Account> {
        return accounts
    }
}
