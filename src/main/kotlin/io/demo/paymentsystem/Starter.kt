package io.demo.paymentsystem

import io.demo.paymentsystem.api.exception.ErrorResponse
import io.demo.paymentsystem.api.PaymentCreationRequest
import io.demo.paymentsystem.api.accountoperation.DepositMoneyRequest
import io.demo.paymentsystem.api.accountoperation.WithdrawnMoneyResponse
import io.demo.paymentsystem.bankmock.job.BankApprovalJob
import io.demo.paymentsystem.config.Holder.bankAprovingInterval
import io.demo.paymentsystem.initializer.DataInitializer
import io.demo.paymentsystem.service.accountoperations.AccountOperationsServiceImpl
import io.demo.paymentsystem.service.payment.PaymentServiceImpl
import io.demo.paymentsystem.utils.readJson
import io.demo.paymentsystem.utils.respondJson
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

fun Application.main() {
    initialize()
    getRouting()
}

private fun Application.getRouting() {
    routing {
        put("/account-operation/deposit") {
            withExceptionHandler {
                val depositMoneyRequest = call.readJson<DepositMoneyRequest>()

                AccountOperationsServiceImpl.depositMoney(depositMoneyRequest)

                call.response.status(HttpStatusCode.OK)
            }
        }
        get("/account-operation/withdraw") {
            withExceptionHandler {
                with(call.request.queryParameters) {
                    val accountId = get("accountId")?.toLongOrNull()
                        ?: throw IllegalArgumentException("accountId is absent or not a number")
                    val amount = get("amount")?.toBigDecimalOrNull()
                        ?: throw IllegalArgumentException("amount is absent or not a number")

                    val withdrawnMoney = AccountOperationsServiceImpl.withdrawMoney(accountId, amount)
                    val response = WithdrawnMoneyResponse(withdrawnMoney)

                    call.respondJson(HttpStatusCode.OK, response)
                }
            }
        }
        post("/payment") {
            withExceptionHandler {
                val creationRequest = call.readJson<PaymentCreationRequest>()
                val error = creationRequest.validate()
                if (error != null) {
                    call.respondJson(HttpStatusCode.BadRequest,
                        ErrorResponse(error)
                    )
                } else {
                    val paymentDto = PaymentServiceImpl.createPayment(creationRequest)

                    call.respondJson(HttpStatusCode.OK, paymentDto)
                }
            }
        }
        get("/") {
            call.respondText("Payment system")
        }
    }
}

private val log = LoggerFactory.getLogger("Web")

private suspend fun PipelineContext<Unit, ApplicationCall>.withExceptionHandler(block: suspend () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        log.error("Exception happened in the web layer: ", e)
        val errorCode = when (e) {
            is IllegalArgumentException -> HttpStatusCode.BadRequest
            else -> HttpStatusCode.InternalServerError
        }
        call.respondJson(errorCode,
            ErrorResponse("${e::class.qualifiedName}: ${e.message}")
        )
    }
}

private fun initialize() {
    DataInitializer.createTables()
    DataInitializer.createEntities()
    GlobalScope.launch { BankApprovalJob(bankAprovingInterval).approveAllCreatedPayments() }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        initialize()
        getRouting()
    }.start(wait = true)
}
