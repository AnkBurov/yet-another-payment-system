package io.demo.paymentsystem.service.lifecycle.check

import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.api.document.DocType

/**
 * In-memory storage for doc operation checkers
 */
object DocCheckerStorage {

    private val docCheckersMap = hashMapOf<DocCheckerMapKey, List<DocOperationChecker>>()

    init {
        val paymentCheckers = listOf(OpenAccountsChecker(), EnoughBalanceAmountChecker(), NotTheSameAccountChecker())
        docCheckersMap.put(DocCheckerMapKey(DocType.PAYMENT, DocStatus.CREATED), paymentCheckers)
        docCheckersMap.put(DocCheckerMapKey(DocType.PAYMENT, DocStatus.CONFIRMED), paymentCheckers)

        val accountOperationsCheckers = listOf(OpenAccountChecker(), WithdrawEnoughMoney())
        docCheckersMap.put(DocCheckerMapKey(DocType.ACCOUNT_OPERATION, DocStatus.CONFIRMED), accountOperationsCheckers)
    }

    /**
     * Get the list of document operation checkers for specified docType and docStatus
     */
    fun getDocOperationCheckers(docType: DocType, docStatus: DocStatus): List<DocOperationChecker> {
        return docCheckersMap.get(DocCheckerMapKey(docType, docStatus)) ?: emptyList()
    }

    private data class DocCheckerMapKey(val docType: DocType, val docStatus: DocStatus)
}