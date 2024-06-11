package io.acme.arrow_merch.customers.repo

import arrow.core.Option
import com.fasterxml.jackson.databind.ObjectMapper
import io.acme.arrow_merch.customers.Customer
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import kotlinx.datetime.LocalDate

@Entity
class CustomerEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long? = null,

    var name: String? = null,
    var email: String? = null,

    var birthday: String? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    var addresses: List<AddressEntity> = mutableListOf(),

    var version: Int = 0,
)

fun CustomerEntity.toCustomer(): Customer = Customer(
    id = id,
    name = name!!, //should never be null
    email = email!!,
    birthday = Option.fromNullable(birthday).map { LocalDate.parse(it) },
    addresses = addresses.map { it.toAddress() }
)

fun Customer.fromCustomer(om: ObjectMapper): CustomerEntity = CustomerEntity(
    id = id,
    name = name,
    email = email,
    birthday = om.writeValueAsString(birthday)
)