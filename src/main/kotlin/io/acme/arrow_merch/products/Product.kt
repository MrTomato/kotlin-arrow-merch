package io.acme.arrow_merch.products

import java.math.BigDecimal
import java.time.LocalDateTime

data class Product(
    val id: Long? = null,
    val name: String,
    val description: String,

    val price: BigDecimal,

    val kongoProductId: String,

    val firstListing: LocalDateTime = LocalDateTime.now(),

    val lastUpdate: LocalDateTime = LocalDateTime.now(),

    val hidden: Boolean = false,
)