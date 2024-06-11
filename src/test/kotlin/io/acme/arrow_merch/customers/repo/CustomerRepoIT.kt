package io.acme.arrow_merch.customers.repo

import io.acme.arrow_merch.jackson
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CustomerRepoIT {
    @Autowired
    var repo: CustomerRepository? = null

    val log = LoggerFactory.getLogger(this::class.java)

    @Test
    fun itSavesACustomerEntityToDb() {
        val actual = repo!!.save(
            CustomerEntity(
                id = null,
                name = "Bob",
                email = "bob@email.com",
                birthday = null,
                addresses = listOf(
                    AddressEntity(
                        id = null,
                        user = null,
                        streetRow1 = "Road 1",
                        streetRow2 = "House number 2",
                        city = "City",
                        zip = "12345",
                        country = "Country",
                        version = 0
                    )
                ),
                version = 0
            ).apply {
                addresses.forEach { it.user = this }    //don't you just love bi-directional relationships?
            }
        )

        actual.apply { id shouldNotBe null }
        log.info("Saved customer entity: \n${jackson().writeValueAsString(actual)}")
    }
}
