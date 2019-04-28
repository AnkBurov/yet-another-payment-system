package io.demo.paymentsystem.service.lifecycle.check

import io.demo.paymentsystem.api.AccountState
import io.demo.paymentsystem.api.document.AccountOperation
import io.demo.paymentsystem.api.lifecycle.check.CheckOperationResult

/**
 * Check that account in the open state
 */
class OpenAccountChecker : AbstractAccountOperationChecker() {

    override fun checkDocOperation(accountOperation: AccountOperation): CheckOperationResult {
        return when (accountOperation.account.accountState) {
            AccountState.OPEN -> CheckOperationResult(true)
            else -> CheckOperationResult(false, "Account is not in an open state")
        }
    }
}