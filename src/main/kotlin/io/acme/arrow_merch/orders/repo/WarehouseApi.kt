package io.acme.arrow_merch.orders.repo

import java.util.UUID
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Repository

@Repository
class WarehouseApi {
    fun reserveProduct(productId: Long, quantity: Int): UUID =
        runBlocking {
            delay(Random.nextLong(10, 1000))
        }.let {
            UUID.randomUUID()
        }

    fun clearReservation(reservationId: UUID) {
        runBlocking {
            delay(Random.nextLong(10, 1000))
        }
    }

}
