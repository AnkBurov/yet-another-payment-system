package io.demo.paymentsystem.api

import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.api.document.Payment
import java.math.BigDecimal

data class PaymentCreationRequest(
    val payerAccountId: Long,

    val amount: BigDecimal,

    val currencyCode: Int? = null,

    val payeeAccountNumber: String,

    val description: String? = null
) {

    /**
     * @return possible error response
     */
    fun validate(): String? {
        return when {
            amount <= BigDecimal.ZERO -> "amount cannot be negative number or zero"
            payeeAccountNumber.isBlank() -> "payeeAccountNumber cannot be blank"
            else -> return null
        }
    }
}

fun PaymentCreationRequest.toPayment(payerAccount: Account, payeeAccount: Account, docStatus: DocStatus): Payment {
    val currencyCode = this.currencyCode ?: payerAccount.currencyCode
    return Payment(
        description = this.description,
        payer = payerAccount,
        payee = payeeAccount,
        amount = this.amount,
        currencyCode = currencyCode,
        docStatus = docStatus
    )
}