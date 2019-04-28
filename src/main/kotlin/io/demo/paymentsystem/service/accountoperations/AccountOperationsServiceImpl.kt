package io.demo.paymentsystem.service.accountoperations

import io.demo.paymentsystem.api.Account
import io.demo.paymentsystem.api.accountoperation.DepositMoneyRequest
import io.demo.paymentsystem.api.accountoperation.toAccountOperation
import io.demo.paymentsystem.api.document.AccountOperation
import io.demo.paymentsystem.api.document.AccountOperation.AccountOperationType.WITHDRAW
import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.dao.AccountOperationDao
import io.demo.paymentsystem.service.account.AccountService
import io.demo.paymentsystem.service.account.AccountServiceImpl
import io.demo.paymentsystem.service.lifecycle.DocLifeCycleService
import io.demo.paymentsystem.service.lifecycle.DocLifeCycleServiceImpl
import io.demo.paymentsystem.utils.inTransaction
import io.demo.paymentsystem.utils.transactional
import io.demo.paymentsystem.utils.useTransaction
import java.math.BigDecimal

object AccountOperationsServiceImpl : AccountOperationsService {

    private val accountService: AccountService = AccountServiceImpl
    private val docTypeLifeCycleService: DocLifeCycleService = DocLifeCycleServiceImpl

    override fun depositMoney(depositMoneyRequest: DepositMoneyRequest) {
        useTransaction { handle ->
            val account = accountService.getAccount(depositMoneyRequest.accountId)
            val accountOperation = depositMoneyRequest.toAccountOperation(account, DocStatus.INITIAL)

            docTypeLifeCycleService.changeDocStatus(accountOperation, DocStatus.CONFIRMED) {
                handle.transactional(AccountOperationDao::class).insert(it)
                accountService.addMoney(handle, it.account, it.amount, account.currencyCode)
            }
        }
    }

    override fun withdrawMoney(accountId: Long, amount: BigDecimal): BigDecimal {
        return inTransaction { handle ->
            val account = accountService.getAccount(accountId)
            val accountOperation = createAccountOperation(account, amount, WITHDRAW, DocStatus.INITIAL)

            return@inTransaction docTypeLifeCycleService.changeDocStatus(accountOperation, DocStatus.CONFIRMED) {
                handle.transactional(AccountOperationDao::class).insert(it)
                accountService.subtractMoney(handle, it.account, it.amount, account.currencyCode)
            }
        }
    }

    private fun createAccountOperation(
        account: Account, amount: BigDecimal,
        accountOperationType: AccountOperation.AccountOperationType, docStatus: DocStatus
    ): AccountOperation {
        return AccountOperation(
            account = account, amount = amount,
            accountOperationType = accountOperationType, docStatus = docStatus
        )
    }
}