package io.acme.arrow_merch.catalog

import io.acme.arrow_merch.customers.pricing.KongoApi
import io.acme.arrow_merch.products.Product
import io.acme.arrow_merch.products.repo.ProductRepository
import org.springframework.stereotype.Service

@Service
class CatalogService(
    private val productRepository: ProductRepository,
    private val kongoApi: KongoApi,
) {
    fun listProducts(): List<Product> = productRepository.findAll()
        .map { fixProductPriceWithKongo(it) }

    private fun fixProductPriceWithKongo(product: Product): Product =
        kongoApi.scrapePrice(product.kongoProductId)
            .let { kongoPrice ->
                if (kongoPrice < product.price)
                    product.copy(price = kongoPrice)
                else
                    product
            }
}