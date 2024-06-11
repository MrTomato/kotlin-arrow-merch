package io.acme.arrow_merch.orders

import io.acme.arrow_merch.customers.Customer
import io.acme.arrow_merch.customers.repo.AddressEntity
import io.acme.arrow_merch.products.Product

data class Order(
    val customer: Customer,
    val products: Map<Product, Int>,
    val shipTo: AddressEntity,
)