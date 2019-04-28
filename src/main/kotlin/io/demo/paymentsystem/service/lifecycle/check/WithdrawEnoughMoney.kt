package io.demo.paymentsystem.service.lifecycle.check

import io.demo.paymentsystem.api.document.AccountOperation
import io.demo.paymentsystem.api.lifecycle.check.CheckOperationResult

/**
 * Check that account has enough money for withdrawal
 */
class WithdrawEnoughMoney : AbstractAccountOperationChecker() {

    override fun checkDocOperation(accountOperation: AccountOperation): CheckOperationResult {
        if (accountOperation.accountOperationType != AccountOperation.AccountOperationType.WITHDRAW) {
            return CheckOperationResult(true)
        }
        return if (accountOperation.amount <= accountOperation.account.balance) {
            CheckOperationResult(true)
        } else {
            CheckOperationResult(false, "Account ${accountOperation.account.accountNumber} doesn't have enough money for this operation")
        }
    }
}