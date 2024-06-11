package io.acme.arrow_merch

import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.time.Duration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.client.RestTemplate


@SpringBootApplication
@EnableJpaRepositories
class ArrowMerchApplication {

    @Bean
    fun jacksonBean(): JsonMapper = jackson()

    @Bean
    fun restTemplateBean(): RestTemplate = restTemplate()
}

fun main(args: Array<String>) {
    runApplication<ArrowMerchApplication>(*args)
}

fun jackson(): JsonMapper = JsonMapper.builder()
    .addModule(JavaTimeModule())
    .configure(INDENT_OUTPUT, true)
    .build()

fun restTemplate(): RestTemplate = RestTemplateBuilder()
    .setReadTimeout(Duration.ofSeconds(5))
    .setConnectTimeout(Duration.ofSeconds(5))
    .build()