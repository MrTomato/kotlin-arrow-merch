package io.acme.arrow_merch.customers.repo

import arrow.core.None
import arrow.core.toOption
import io.acme.arrow_merch.customers.Customer
import io.acme.arrow_merch.jackson
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import org.slf4j.LoggerFactory

class CustomerEntityTest : FreeSpec({
    val om = jackson()
    val log = LoggerFactory.getLogger(this::class.java)


    "Serialization to CustomerEntity" - {
        val sut = Customer(
            id = 1,
            name = "Bob",
            email = "bob@email.com",
            birthday = None,
            addresses = emptyList()
        )

        "maps birthday value" - {
            "None to string 'None'" {
                val actual = sut.copy(birthday = None)
                    .fromCustomer(om)

                actual.apply {
                    birthday shouldBe "None"
                }
            }
            "null to null" {
                val actual = sut.copy(birthday = null)
                    .fromCustomer(om)

                actual.apply {
                    birthday shouldBe null
                }
            }
            "1.1.1990 to ISO string 1990-01-01" {
                val actual = sut.copy(
                    birthday = LocalDate(year = 1990, monthNumber = 1, dayOfMonth = 1).toOption()
                ).fromCustomer(om)

                actual.apply {
                    birthday shouldBe "1990-01-01"
                }
            }
        }
    }

    "Deserialization from CustomerEntity" - {

        "maps birthday value" - {
            "string 'None' to None" {
                val actual = CustomerEntity(
                    id = 1,
                    name = "Bob",
                    email = "bob@email.com",
                    birthday = "None"
                )
                    .toCustomer()

                actual.apply {
                    birthday!!.shouldBeNone()
                }
            }
            "null to null" {
                val actual = CustomerEntity(
                    id = 1,
                    name = "Bob",
                    email = "bob@email.com",
                    birthday = null
                ).toCustomer()

                actual.apply {
                    birthday shouldBe null
                }
            }
            "ISO string 1990-01-01 to LocalDate 1.1.1990" {
                val actual = CustomerEntity(
                    id = 1,
                    name = "Bob",
                    email = "bob@email.com",
                    birthday = "1990-01-01"
                ).toCustomer()

                actual.apply {
                    birthday!! shouldBeSome LocalDate(1990, 1, 1)
                }
            }
        }

        "throws exceptions on non-nullable fields" - {
            "id" {
                shouldThrowAny {
                    CustomerEntity(
                        id = null,
                        name = "Bob",
                        email = "bob@email.com",
                        birthday = "1990-01-01"
                    ).toCustomer()
                }
            }

            "name" {
                shouldThrowAny {
                    CustomerEntity(
                        id = 123,
                        name = null,
                        email = "bob@email.com",
                        birthday = "1990-01-01"
                    ).toCustomer()
                }.also {
                    log.info("Caught exception: $it")
                }
            }

            "email" {
                shouldThrowAny {
                    CustomerEntity(
                        id = 123,
                        name = "Bob",
                        email = null,
                        birthday = "1990-01-01"
                    ).toCustomer()
                }
            }
        }
    }
})
