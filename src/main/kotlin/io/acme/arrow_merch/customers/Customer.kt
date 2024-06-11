package io.acme.arrow_merch.customers

import arrow.core.Option
import kotlinx.datetime.LocalDate

data class Customer(
    val id: Long? = null,

    val name: String,
    val email: String,

    val birthday: Option<LocalDate>?,
    val addresses: List<Address>,
)