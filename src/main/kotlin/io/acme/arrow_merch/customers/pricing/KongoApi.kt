package io.acme.arrow_merch.customers.pricing

import com.fasterxml.jackson.databind.ObjectMapper
import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class KongoApi(
    private val om: ObjectMapper,
    private val rest: RestTemplate,
    @Value("\${app.kongoUrl}")
    private val kongoUrl: String,
) {

    fun scrapePrice(productId: String): BigDecimal =
        rest.getForObject("$kongoUrl/api/products/$productId", String::class.java)
            .let { json ->
                om.readTree(json)["price"].decimalValue()
            }
}