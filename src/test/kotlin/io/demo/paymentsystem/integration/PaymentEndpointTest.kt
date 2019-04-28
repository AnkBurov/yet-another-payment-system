package io.demo.paymentsystem.integration

import io.demo.paymentsystem.api.exception.ErrorResponse
import io.demo.paymentsystem.api.PaymentCreationRequest
import io.demo.paymentsystem.api.document.PaymentDto
import io.demo.paymentsystem.main
import io.demo.paymentsystem.utils.asJson
import io.demo.paymentsystem.utils.readJson
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class PaymentEndpointTest {

    @Test
    fun testCreatePayment() = withTestApplication(Application::main) {
        val creationRequest = PaymentCreationRequest(1, BigDecimal.ONE, payeeAccountNumber = "40810000000001234568")

        handleRequest(HttpMethod.Post, "/payment") {
            setBody(creationRequest.asJson())
        }.response.let {
            Assert.assertEquals(HttpStatusCode.OK, it.status())
            Assert.assertNotNull(it.readJson<PaymentDto>()?.id)
        }
    }

    @Test
    fun testCreatePaymentZeroAmount() = withTestApplication(Application::main) {
        val creationRequest = PaymentCreationRequest(1, BigDecimal.ZERO, payeeAccountNumber = "40810000000001234567")

        handleRequest(HttpMethod.Post, "/payment") {
            setBody(creationRequest.asJson())
        }.response.let {
            Assert.assertEquals(HttpStatusCode.BadRequest, it.status())
            Assert.assertTrue(it.readJson<ErrorResponse>()?.error?.contains("amount") ?:false)
        }
    }

//    todo run async shit
    @Test
    fun testCreatePaymentToSameAccount() = withTestApplication(Application::main) {
        val creationRequest = PaymentCreationRequest(1, BigDecimal.ONE, payeeAccountNumber = "40810000000001234567")

        handleRequest(HttpMethod.Post, "/payment") {
            setBody(creationRequest.asJson())
        }.response.let {
            Assert.assertEquals(HttpStatusCode.BadRequest, it.status())

            val expectedError =
                "java.lang.IllegalArgumentException: Payee account cannot be same account with the payer account"
            Assert.assertEquals(expectedError, it.readJson<ErrorResponse>()?.error)
        }
    }
}