package io.demo.paymentsystem.bankmock.job

import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.api.document.Payment
import io.demo.paymentsystem.service.payment.PaymentService
import io.demo.paymentsystem.service.payment.PaymentServiceImpl
import kotlinx.coroutines.time.delay
import org.slf4j.LoggerFactory
import java.time.Duration

/**
 * A mock of an external bank system
 *
 * Every n interval approves all payments with CREATED status
 */
class BankApprovalJob(
    private val duration: Duration
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private val paymentService: PaymentService = PaymentServiceImpl

    suspend fun approveAllCreatedPayments() {
        while (true) {
            delay(duration)
            log.info("Approval job task is started")
            val handledPayments = paymentService.approveCreatedPayments()
            val approvedPaymentsCount = handledPayments.getPaymentsCountByDocStatus(DocStatus.CONFIRMED)
            val declinedPaymentsCount = handledPayments.getPaymentsCountByDocStatus(DocStatus.DECLINED)
            if (approvedPaymentsCount != 0) log.info("$approvedPaymentsCount payments were confirmed")
            if (declinedPaymentsCount != 0) log.info("$declinedPaymentsCount payments were declined")
            log.info("Approval job task has ended")
        }
    }

    private fun List<Payment>.getPaymentsCountByDocStatus(docStatus: DocStatus): Int {
        return this.asSequence()
            .filter { it.docStatus == docStatus }
            .count()
    }
}