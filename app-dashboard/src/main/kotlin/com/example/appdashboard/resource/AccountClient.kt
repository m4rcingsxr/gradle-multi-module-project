package com.example.appdashboard.resource

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod.GET
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AccountClient(private val restTemplate: RestTemplate) {
    fun fetchAccounts(): List<Account> {
        return restTemplate.exchange(
            "http://localhost:8081/accounts",
            GET,
            null,
            object : ParameterizedTypeReference<List<Account>>() {}
        ).body.orEmpty()
    }
}
