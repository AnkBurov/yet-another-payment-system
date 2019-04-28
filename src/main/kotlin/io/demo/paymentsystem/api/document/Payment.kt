package io.demo.paymentsystem.api.document

import io.demo.paymentsystem.api.Account
import java.math.BigDecimal

class Payment(
        id: Long? = null,

        version: Long = 1,

        docStatus: DocStatus,

        description: String? = null,

        val payer: Account,

        val payee: Account,

        val amount: BigDecimal,

        val currencyCode: Int
) : Document(id, version, DocType.PAYMENT,  docStatus, description) {
}

fun Payment.toPaymentDto(): PaymentDto {
    return PaymentDto(id!!, docType, description, payer.id!!, payer.accountNumber, payee.id!!, payee.accountNumber,
            amount, currencyCode, docStatus)
}

class PaymentDto(
        val id: Long,

        val docType: DocType,

        val description: String? = null,

        val payerAccountId: Long,

        val payerAccountNumber: String,

        val payeeAccountId: Long,

        val payeeAccountNumber: String,

        val amount: BigDecimal,

        val currencyCode: Int,

        var docStatus: DocStatus
)