package io.demo.paymentsystem.api

import java.math.BigDecimal

class Account(
        var id: Long? = null,

        val accountNumber: String,

        val accountState: AccountState,

        val userId: Long?,

        var balance: BigDecimal,

        val currencyCode: Int
) {
}