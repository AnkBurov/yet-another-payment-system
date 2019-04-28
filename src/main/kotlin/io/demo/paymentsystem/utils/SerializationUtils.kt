package io.demo.paymentsystem.utils

import com.fasterxml.jackson.module.kotlin.readValue
import io.demo.paymentsystem.config.Holder
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond

suspend inline fun <reified T : Any> ApplicationCall.readJson(): T {
    return Holder.objectMapper.readValue<T>(this.receiveText())
}

suspend fun  ApplicationCall.respondJson(status: HttpStatusCode, dto: Any) {
    this.respond(status, Holder.objectMapper.writeValueAsString(dto))
}