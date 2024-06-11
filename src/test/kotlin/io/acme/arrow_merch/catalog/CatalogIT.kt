package io.acme.arrow_merch.catalog

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.http.Fault
import com.github.tomakehurst.wiremock.matching.UrlPathTemplatePattern
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import io.kotest.assertions.json.FieldComparison
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.wiremock.ListenerMode
import io.kotest.extensions.wiremock.WireMockListener
import io.kotest.matchers.longs.shouldBeInRange
import io.kotest.matchers.shouldBe
import kotlin.time.Duration
import kotlinx.datetime.Clock.System
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate

@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = ["app.kongoUrl=http://localhost:9000"]
)
class CatalogIT(
    @LocalServerPort
    private val port: Int,
) : BehaviorSpec({
    val log = LoggerFactory.getLogger(this::class.java)

    val rest = RestTemplate()

    val kongo = WireMockServer(
        options()
            .port(9000)
            .globalTemplating(true)
    )
    listener(WireMockListener(kongo, ListenerMode.PER_SPEC))

    given("Kongo is healthy") {
        and("running a sale where all prices are lower than ours") {
            kongo.givenThat(
                get(UrlPathTemplatePattern("/api/products/{productId}"))
                    .willReturn(
                        aResponse().withBody(
                            """
                                {
                                    "name": "Sale {{capitalize request.path.contactId}}",
                                    "price": 1.99
                                }
                            """.trimIndent()
                        )
                    )
            )
            `when`("listing products") {
                val response = rest.getForEntity("http://localhost:$port/catalog/products", String::class.java)
                then("all prices are from Kongo") {
                    response.run {
                        statusCode shouldBe HttpStatus.OK
                        body!! shouldEqualJson {
                            fieldComparison = FieldComparison.Lenient
                            """
                                [
                                    {
                                        "name": "Arrow Mug",
                                        "price": 1.99
                                    },
                                    {
                                        "name": "Arrow Shirt",
                                        "price": 1.99
                                    },
                                    {
                                        "name": "Arrow Cap",
                                        "price": 1.99
                                    },
                                    {
                                        "name": "Arrow Sticker",
                                        "price": 1.99
                                    }
                                ]
                            """.trimIndent()
                        }
                    }
                }
            }
        }
        and("all products are more expensive there") {
            kongo.givenThat(
                get(UrlPathTemplatePattern("/api/products/{productId}"))
                    .willReturn(
                        aResponse().withBody(
                            """
                                {
                                    "name": "EXPENSIVE {{capitalize request.path.contactId}}",
                                    "price": 1000.00
                                }
                            """.trimIndent()
                        )
                    )
            )
            `when`("listing products") {
                val response = rest.getForEntity("http://localhost:$port/catalog/products", String::class.java)
                then("all prices are ours") {
                    response.run {
                        statusCode shouldBe HttpStatus.OK
                        body!! shouldEqualJson {
                            fieldComparison = FieldComparison.Lenient
                            """
                                [
                                    {
                                        "name": "Arrow Mug",
                                        "price": 13.37
                                    },
                                    {
                                        "name": "Arrow Shirt",
                                        "price": 50.01
                                    },
                                    {
                                        "name": "Arrow Cap",
                                        "price": 25.50
                                    },
                                    {
                                        "name": "Arrow Sticker",
                                        "price": 10.99
                                    }
                                ]
                            """.trimIndent()
                        }
                    }
                }
            }
        }
    }

    val numberOfProducts = 4L
    given("Kongo is unhealthy") {
        kongo.givenThat(
            get(UrlPathTemplatePattern("/api/products/{productId}")).inScenario("Kongo is faulty")
                .whenScenarioStateIs(STARTED)
                .willReturn(
                    aResponse().withBody(
                        """
                        {
                            "name": "EXPENSIVE {{capitalize request.path.contactId}}",
                            "price": 1000.00
                        }
                    """.trimIndent()
                    )
                )
        )
        and("fails after first requests") {
            kongo.givenThat(
                get(UrlPathTemplatePattern("/api/products/{productId}")).inScenario("Kongo is faulty")
                    .whenScenarioStateIs("500")
                    .willReturn(
                        aResponse()
                            .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .withBody("Sorry, Kongo is sad right now. Come back with ice cream!")
                    )
            )

            `when`("listing products") {
                val response = rest.getForEntity("http://localhost:$port/catalog/products", String::class.java)
                response.statusCode shouldBe HttpStatus.OK
                kongo.setScenarioState("Kongo is faulty", "500")
                then("the second request fails") {  //TODO: We should probably figure out a way be more resilient
                    shouldThrow<HttpServerErrorException> {
                        rest.getForEntity(
                            "http://localhost:$port/catalog/products",
                            String::class.java
                        )
                    }.run {
                        statusCode shouldBe HttpStatus.INTERNAL_SERVER_ERROR
                    }
                }
            }
        }
        and("response time goes to 10 minutes") {
            kongo.givenThat(
                get(UrlPathTemplatePattern("/api/products/{productId}")).inScenario("Kongo is faulty")
                    .whenScenarioStateIs("timeout")
                    .willReturn(
                        aResponse()
                            .withFixedDelay(Duration.parse("10m").inWholeMilliseconds.toInt())
                            .withStatus(200)
                    )
            )
            kongo.setScenarioState("Kongo is faulty", "timeout")

            `when`("listing products") {
                then("the request times out") {  //TODO: We should probably figure out a way be more resilient
                    shouldThrow<HttpServerErrorException> {
                        rest.getForEntity(
                            "http://localhost:$port/catalog/products",
                            String::class.java
                        )
                    }.run {
                        statusCode shouldBe HttpStatus.INTERNAL_SERVER_ERROR
                    }
                }
            }
        }
        and("response time is slow (4 seconds)") {
            kongo.givenThat(
                get(UrlPathTemplatePattern("/api/products/{productId}")).inScenario("Kongo is faulty")
                    .whenScenarioStateIs("slow")
                    .willReturn(
                        aResponse()
                            .withFixedDelay(Duration.parse("4s").inWholeMilliseconds.toInt())
                            .withStatus(200)
                            .withBody(
                                """
                                    {
                                        "name": "EXPENSIVE {{capitalize request.path.contactId}}",
                                        "price": 1000.00
                                    }
                                """.trimIndent()
                            )
                    )
            )
            kongo.setScenarioState("Kongo is faulty", "slow")

            `when`("listing products") {
                val start = System.now()
                val actual = rest.getForEntity(
                    "http://localhost:$port/catalog/products",
                    String::class.java
                )
                val end = System.now()

                then("the response grows linearly with time") {
                    val duration = end.minus(start)
                    duration.inWholeSeconds shouldBeInRange ((numberOfProducts * 4 - 2)..(numberOfProducts * 4 + 2)) //TODO: maybe change this to parallel requests to avoid linear growth?
                }

                then("the response is correct") {
                    actual.apply {
                        statusCode shouldBe HttpStatus.OK
                        body!! shouldEqualJson {
                            fieldComparison = FieldComparison.Lenient
                            """
                                [
                                    {
                                        "name": "Arrow Mug",
                                        "price": 13.37
                                    },
                                    {
                                        "name": "Arrow Shirt",
                                        "price": 50.01
                                    },
                                    {
                                        "name": "Arrow Cap",
                                        "price": 25.50
                                    },
                                    {
                                        "name": "Arrow Sticker",
                                        "price": 10.99
                                    }
                                ]
                            """.trimIndent()
                        }
                    }
                }
            }
        }
        and("returns garbage") {
            kongo.givenThat(
                get(UrlPathTemplatePattern("/api/products/{productId}")).inScenario("Kongo is faulty")
                    .whenScenarioStateIs("garbage")
                    .willReturn(
                        aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)
                    )
            )
            kongo.setScenarioState("Kongo is faulty", "garbage")

            `when`("listing products") {
                then("the request fails") {  //TODO: We should probably figure out a way be more resilient
                    shouldThrow<HttpServerErrorException> {
                        rest.getForEntity(
                            "http://localhost:$port/catalog/products",
                            String::class.java
                        )
                    }.run {
                        statusCode shouldBe HttpStatus.INTERNAL_SERVER_ERROR
                    }
                }
            }
        }
    }
})
