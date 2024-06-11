package io.acme.arrow_merch.customers

data class Address(
    val id: Long? = null,

    val streetRow1: String,
    val streetRow2: String,
    val city: String,
    val zip: String,
    val country: String,
)
