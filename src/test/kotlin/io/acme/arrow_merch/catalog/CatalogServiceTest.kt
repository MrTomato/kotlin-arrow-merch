package io.acme.arrow_merch.catalog

import io.acme.arrow_merch.customers.pricing.KongoApi
import io.acme.arrow_merch.products.Product
import io.acme.arrow_merch.products.repo.ProductRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class CatalogServiceTest : BehaviorSpec({
    val productRepository = mockk<ProductRepository>()
    val kongoApi = mockk<KongoApi>()

    val sut = CatalogService(productRepository, kongoApi)
    given("product repo returns a product") {
        every { productRepository.findAll() } returns mutableListOf(
            Product(
                1L,
                "someProductName",
                "someProductDescription",
                200.toBigDecimal(),
                "someKongoId"
            )
        )
        and("KongoApi is healthy") {
            and("it returns a lower price than ours") {
                every { kongoApi.scrapePrice(any()) } returns 100.toBigDecimal()
                `when`("the catalog is listed") {
                    val result = sut.listProducts()

                    then("the kongo price is taken") {
                        result.size shouldBe 1
                        result[0].price shouldBe 100.toBigDecimal()
                    }
                }
            }
            and("it returns a higher price than ours") {
                every { kongoApi.scrapePrice(any()) } returns 500.toBigDecimal()
                every { productRepository.findAll() } returns mutableListOf(
                    Product(
                        1L,
                        "someProductName",
                        "someProductDescription",
                        200.toBigDecimal(),
                        "someKongoId"
                    )
                )
                `when`("the catalog is listed") {
                    val result = sut.listProducts()

                    then("our price is taken") {
                        result.size shouldBe 1
                        result[0].price shouldBe 200.toBigDecimal()
                    }
                }
            }
        }
        given("KongoApi is unhealthy") {
            every { kongoApi.scrapePrice(any()) } throws RuntimeException("Kongo is down")
            `when`("the catalog is listed") {
                val result = sut.listProducts()

                then("our price is taken") {
                    result.size shouldBe 1
                    result[0].price shouldBe 200.toBigDecimal()
                }
            }
        }
    }

})
