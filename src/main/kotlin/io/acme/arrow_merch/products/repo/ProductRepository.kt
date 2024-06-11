package io.acme.arrow_merch.products.repo

import io.acme.arrow_merch.products.Product
import java.math.BigDecimal
import java.util.Optional
import java.util.function.Function
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery
import org.springframework.stereotype.Repository

@Repository
class ProductRepository : JpaRepository<Product, Long> {
    override fun <S : Product?> save(entity: S & Any): S & Any {
        TODO("Not yet implemented")
    }

    override fun <S : Product?> saveAll(entities: MutableIterable<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Optional<Product> {
        TODO("Not yet implemented")
    }

    override fun existsById(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun <S : Product?> findAll(example: Example<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun <S : Product?> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableList<Product> =
        mutableListOf(
            Product(
                id = 1,
                name = "Arrow Mug",
                description = "The best mug  in the world, made even better with the arrow logo on it!",
                price = BigDecimal("13.37"),
                kongoProductId = "arrow-mug-1",
            ),
            Product(
                id = 2,
                name = "Arrow Shirt",
                description = "The best shirt in the world, made even better with the arrow logo on it!",
                price = BigDecimal("50.01"),
                kongoProductId = "arrow-shirt-1",
            ),
            Product(
                id = 3,
                name = "Arrow Cap",
                description = "The best cap in the world, made even better with the arrow logo on it!",
                price = BigDecimal("25.50"),
                kongoProductId = "arrow-cap-1",
            ),
            Product(
                id = 4,
                name = "Arrow Sticker",
                description = "Make your laptop even better - put an arrow on it!",
                price = BigDecimal("10.99"),
                kongoProductId = "arrow-sticker-1",
            )
        )


    override fun findAll(sort: Sort): MutableList<Product> {
        TODO("Not yet implemented")
    }

    override fun findAll(pageable: Pageable): Page<Product> {
        TODO("Not yet implemented")
    }

    override fun count(): Long {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun flush() {
        TODO("Not yet implemented")
    }

    override fun deleteAllInBatch() {
        TODO("Not yet implemented")
    }

    override fun getReferenceById(id: Long): Product {
        TODO("Not yet implemented")
    }

    override fun getById(id: Long): Product {
        TODO("Not yet implemented")
    }

    override fun getOne(id: Long): Product {
        TODO("Not yet implemented")
    }

    override fun deleteAllByIdInBatch(ids: MutableIterable<Long>) {
        TODO("Not yet implemented")
    }

    override fun deleteAllInBatch(entities: MutableIterable<Product>) {
        TODO("Not yet implemented")
    }

    override fun <S : Product?> saveAllAndFlush(entities: MutableIterable<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun <S : Product?> saveAndFlush(entity: S & Any): S & Any {
        TODO("Not yet implemented")
    }

    override fun <S : Product?, R : Any?> findBy(
        example: Example<S>,
        queryFunction: Function<FetchableFluentQuery<S>, R>,
    ): R & Any {
        TODO("Not yet implemented")
    }

    override fun <S : Product?> exists(example: Example<S>): Boolean {
        TODO("Not yet implemented")
    }

    override fun <S : Product?> findOne(example: Example<S>): Optional<S> {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entities: MutableIterable<Product>) {
        TODO("Not yet implemented")
    }

    override fun deleteAllById(ids: MutableIterable<Long>) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: Product) {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun <S : Product?> count(example: Example<S>): Long {
        TODO("Not yet implemented")
    }

    override fun findAllById(ids: MutableIterable<Long>): MutableList<Product> {
        TODO("Not yet implemented")
    }

    override fun <S : Product?> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        TODO("Not yet implemented")
    }

}
