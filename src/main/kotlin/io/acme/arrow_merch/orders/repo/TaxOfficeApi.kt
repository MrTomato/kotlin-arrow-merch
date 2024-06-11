package io.acme.arrow_merch.orders.repo

import java.math.BigDecimal
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Repository

@Repository
class TaxOfficeApi {
    fun payTaxAmount(amountInEur: BigDecimal) {
        runBlocking {
            delay(Random.nextLong(10, 1000))
        }
    }

    fun withdrawTaxAmount(amountInEur: BigDecimal) {
        runBlocking {
            delay(Random.nextLong(10, 1000))
        }
    }
}