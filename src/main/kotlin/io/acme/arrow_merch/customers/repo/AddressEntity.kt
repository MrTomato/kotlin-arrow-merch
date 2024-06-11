package io.acme.arrow_merch.customers.repo

import com.fasterxml.jackson.annotation.JsonIgnore
import io.acme.arrow_merch.customers.Address
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class AddressEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JsonIgnore
    var user: CustomerEntity? = null,

    var streetRow1: String? = null,
    var streetRow2: String? = null,
    var city: String? = null,
    var zip: String? = null,
    var country: String? = null,

    var version: Int = 0,
)

fun AddressEntity.toAddress(): Address = Address(
    id = id,
    streetRow1 = streetRow1!!,
    streetRow2 = streetRow2!!,
    city = city!!,
    zip = zip!!,
    country = country!!
)

fun Address.fromAddress(): AddressEntity = AddressEntity(
    id = id,
    streetRow1 = streetRow1,
    streetRow2 = streetRow2,
    city = city,
    zip = zip,
    country = country
)