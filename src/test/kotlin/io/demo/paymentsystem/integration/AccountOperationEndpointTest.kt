package io.demo.paymentsystem.integration

import io.demo.paymentsystem.api.accountoperation.DepositMoneyRequest
import io.demo.paymentsystem.api.accountoperation.WithdrawnMoneyResponse
import io.demo.paymentsystem.main
import io.demo.paymentsystem.utils.asJson
import io.demo.paymentsystem.utils.readJson
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class AccountOperationEndpointTest {

    @Test
    fun testDepositMoney() = withTestApplication(Application::main) {
        val depositMoneyRequest = DepositMoneyRequest(BigDecimal.TEN, accountId = 1)

        handleRequest(HttpMethod.Put, "/account-operation/deposit") {
            setBody(depositMoneyRequest.asJson())
        }.response.let {
            assertEquals(HttpStatusCode.OK, it.status())
        }
    }

    @Test
    fun testWithdrawMoney() = withTestApplication(Application::main) {
        val accountId = 1
        val amount = BigDecimal.ONE

        val url = "/account-operation/withdraw?accountId=$accountId&amount=$amount"
        handleRequest(HttpMethod.Get, url) {
        }.response.let {
            assertEquals(HttpStatusCode.OK, it.status())
            assertEquals(amount, it.readJson<WithdrawnMoneyResponse>()?.withdrawnMoney)
        }
    }
}