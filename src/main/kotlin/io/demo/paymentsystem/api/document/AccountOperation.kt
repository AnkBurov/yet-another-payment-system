package io.demo.paymentsystem.api.document

import io.demo.paymentsystem.api.Account
import java.math.BigDecimal

class AccountOperation(
        id: Long? = null,

        version: Long = 1,

       docStatus: DocStatus,

        description: String? = null,

        val account: Account,

        val amount: BigDecimal,

        val accountOperationType: AccountOperationType
) : Document(id, version, DocType.ACCOUNT_OPERATION, docStatus, description) {

    enum class AccountOperationType {
        DEPOSIT,
        WITHDRAW
    }
}