package io.acme.arrow_merch.orders

import io.acme.arrow_merch.orders.repo.PaymentApi
import io.acme.arrow_merch.orders.repo.TaxOfficeApi
import io.acme.arrow_merch.orders.repo.WarehouseApi
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class OrderService(
    val warehouseApi: WarehouseApi,
    val paymentApi: PaymentApi,
    val taxOfficeApi: TaxOfficeApi,
    val rest: RestTemplate,
) {
    fun prepareOrder(order: Order) = let {
        val beforeTax = order.products
            .map { (product, quantity) -> product.price * quantity.toBigDecimal() }
            .reduce(BigDecimal::plus)
        val taxAmount = (beforeTax * BigDecimal("0.20")).setScale(2, RoundingMode.HALF_UP)
        val total = beforeTax + taxAmount

        val reservations = order.products
            .map { (product, quantity) -> warehouseApi.reserveProduct(product.id!!, quantity) }

        paymentApi.deductMoney(order.customer.id!!, total)
        taxOfficeApi.payTaxAmount(taxAmount)

        total to reservations
    }

    fun placeOrder(preparationId: UUID): UUID = TODO("takes the reservation and finalizes shipping")

}