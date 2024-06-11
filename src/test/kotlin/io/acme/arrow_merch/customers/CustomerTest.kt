package io.acme.arrow_merch.customers

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class CustomerTest : FreeSpec({
    "Change a field in the customer" {
        val customer = Customer(
            id = 1L,
            name = "someName",
            email = "someEmail",
            birthday = null,
            addresses = emptyList()
        )

        val changed = customer.copy(
            name = "newName"
        )

        changed.name shouldBe "newName"
        customer.name shouldBe "someName"
    }

    "Change a field in the nested address" {
        val customer = Customer(
            1L,
            "someName",
            "someEmail",
            null,
            listOf(
                Address(
                    1L,
                    "someStreet1",
                    "someStreet2",
                    "someCity",
                    "someZip",
                    "someCountry"
                )
            )
        )
        val changed = customer.copy(
            addresses = listOf(customer.addresses[0].copy(streetRow1 = "newStreet1"))
        )

        changed.addresses[0].streetRow1 shouldBe "newStreet1"
        customer.addresses[0].streetRow1 shouldBe "someStreet1"

    }

    "Change a field in the multiple nested addresses" {
        val customer = Customer(
            1L,
            "someName",
            "someEmail",
            null,
            listOf(
                Address(
                    1L,
                    "someStreet11",
                    "someStreet12",
                    "someCity1",
                    "someZip1",
                    "someCountry1"
                ),
                Address(
                    2L,
                    "someStreet21",
                    "someStreet22",
                    "someCity2",
                    "someZip2",
                    "someCountry2"
                ),
                Address(
                    3L,
                    "someStreet31",
                    "someStreet32",
                    "someCity3",
                    "someZip3",
                    "someCountry3"
                )
            )
        )
        val changed = customer.copy(
            addresses = customer.addresses.map { it.copy(streetRow1 = "newStreet1") }
        )

        changed.addresses.map { it.streetRow1 } shouldBe listOf("newStreet1", "newStreet1", "newStreet1")
        customer.addresses.map { it.streetRow1 } shouldBe listOf("someStreet11", "someStreet21", "someStreet31")

    }
})
