package io.acme.arrow_merch.catalog

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/catalog")
class CatalogController(private val catalogService: CatalogService) {

    @GetMapping("/products")
    fun listProducts() = catalogService.listProducts()
}