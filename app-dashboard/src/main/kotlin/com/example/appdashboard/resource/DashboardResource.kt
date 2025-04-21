package com.example.appdashboard.resource

import j2html.TagCreator.body
import j2html.TagCreator.div
import j2html.TagCreator.each
import j2html.TagCreator.h1
import j2html.TagCreator.head
import j2html.TagCreator.html
import j2html.TagCreator.link
import j2html.TagCreator.table
import j2html.TagCreator.tbody
import j2html.TagCreator.td
import j2html.TagCreator.th
import j2html.TagCreator.thead
import j2html.TagCreator.title
import j2html.TagCreator.tr
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dashboard")
class DashboardResource(
    private val accountClient: AccountClient
) {

    @GetMapping(produces = ["text/html"])
    fun dashboard(): String {
        val accounts = accountClient.fetchAccounts()
        return renderHtml(accounts)
    }

    private fun renderHtml(accounts: List<Account>): String {
        return html(
            head(
                title("Accounts Table"),
                link().withRel("stylesheet")
                    .withHref("https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css")
            ),
            body(
                div().withClass("container mt-4").with(
                    h1("Accounts Table").withClass("h1"),
                    table().withClass("table table-striped").with(
                        thead(
                            tr(
                                th("ID"),
                                th("Name"),
                                th("Email")
                            )
                        ),
                        tbody(
                            each(accounts) {
                                tr(
                                    td(it.id.toString()),
                                    td(it.name),
                                    td(it.email)
                                )
                            }
                        )
                    )
                )
            )
        ).renderFormatted()
    }
}
