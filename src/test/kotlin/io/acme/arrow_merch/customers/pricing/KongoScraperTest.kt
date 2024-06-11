package io.acme.arrow_merch.customers.pricing

import io.acme.arrow_merch.jackson
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.math.BigDecimal
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

class KongoScraperTest : FreeSpec({
    "GIVEN Kongo is online" - {
        val rest = mockk<RestTemplate>()
        val om = jackson()
        val kongoUrl = "https://kongo.at"
        val sut = KongoApi(om, rest, kongoUrl)

        "AND Kongo has the product" - {
            val urlSlot = slot<String>()
            val productId = "p123"
            val productName = "mockedProduct"
            val productPrice = BigDecimal.valueOf(4224, 2)
            every { rest.getForObject(capture(urlSlot), String::class.java) } returns """
                {
                    "name": "$productName",
                    "price": $productPrice
                }
            """.trimIndent()

            "WHEN scraping the price" - {
                val actual = sut.scrapePrice(productId)
                "THEN the Kongo url is called" {
                    verify(exactly = 1) {
                        rest.getForObject(
                            "$kongoUrl/api/products/$productId",
                            String::class.java
                        )
                    }
                }

                "THEN the product price is returned" {
                    actual shouldBe productPrice
                }
            }

        }
        "AND Kongo doesn't have the product" - {
            every {
                rest.getForObject(
                    any<String>(),
                    String::class.java
                )
            } throws RestClientException("404 Not Found")

            "WHEN scraping the price" - {
                "THEN an exception is thrown" {
                    shouldThrowAny { sut.scrapePrice("someProductId") }
                }
            }
        }
    }

    "GIVEN Kongo is offline" - {
        //TODO: do a retry or something
    }
})
