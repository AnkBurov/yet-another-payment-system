package io.demo.paymentsystem.service.payment

import io.demo.paymentsystem.api.PaymentCreationRequest
import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.api.document.PaymentDto
import io.demo.paymentsystem.api.document.Payment
import io.demo.paymentsystem.api.document.toPaymentDto
import io.demo.paymentsystem.api.toPayment
import io.demo.paymentsystem.dao.PaymentDao
import io.demo.paymentsystem.service.account.AccountService
import io.demo.paymentsystem.service.account.AccountServiceImpl
import io.demo.paymentsystem.service.lifecycle.DocLifeCycleService
import io.demo.paymentsystem.service.lifecycle.DocLifeCycleServiceImpl
import io.demo.paymentsystem.utils.dao
import io.demo.paymentsystem.utils.inTransaction
import io.demo.paymentsystem.utils.transactional
import org.slf4j.LoggerFactory

object PaymentServiceImpl : PaymentService {

    private val accountService: AccountService = AccountServiceImpl
    private val docTypeLifeCycleService: DocLifeCycleService = DocLifeCycleServiceImpl
    private val paymentDao = dao(PaymentDao::class)
    private val log = LoggerFactory.getLogger(javaClass)

    override fun createPayment(creationRequest: PaymentCreationRequest): PaymentDto {
        return inTransaction { handle ->
            val payerAccount = accountService.getAccount(creationRequest.payerAccountId)
            val payeeAccount = accountService.getAccount(creationRequest.payeeAccountNumber)
            val payment = creationRequest.toPayment(payerAccount, payeeAccount, DocStatus.INITIAL)

            return@inTransaction docTypeLifeCycleService.changeDocStatus(payment, DocStatus.CREATED) {
                handle.transactional(PaymentDao::class).insert(it)
                    .toPaymentDto()
            }
        }
    }

    override fun approveCreatedPayments(): List<Payment> {
        return paymentDao.findByDocStatus(DocStatus.CREATED)
            .map { payment ->
                try {
                    docTypeLifeCycleService.changeDocStatus(payment, DocStatus.CONFIRMED) { executeTransaction(it) }
                } catch (e: Exception) {
                    log.warn("Declining payment because of: ", e)
                    docTypeLifeCycleService.changeDocStatus(payment, DocStatus.DECLINED) { declineTransaction(it) }
                }
            }
    }

    private fun executeTransaction(payment: Payment): Payment {
        return with(payment) {
            inTransaction { handle ->
                val subtractedMoney = accountService.subtractMoney(handle, payer, amount, currencyCode)
                accountService.addMoney(handle, payee, subtractedMoney, currencyCode)
                docStatus = DocStatus.CONFIRMED
                handle.transactional(PaymentDao::class).updateDocStatus(id, docStatus)
                this
            }
        }
    }

    private fun declineTransaction(payment: Payment): Payment {
        payment.docStatus = DocStatus.DECLINED
        paymentDao.updateDocStatus(payment.id, payment.docStatus)
        return payment
    }
}