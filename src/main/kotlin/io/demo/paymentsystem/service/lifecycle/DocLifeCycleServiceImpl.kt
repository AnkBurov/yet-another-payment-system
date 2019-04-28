package io.demo.paymentsystem.service.lifecycle

import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.api.document.Document
import io.demo.paymentsystem.api.lifecycle.check.CheckOperationResult
import io.demo.paymentsystem.api.lifecycle.check.DocOperationCheckResult
import io.demo.paymentsystem.service.lifecycle.check.DocCheckerStorage

object DocLifeCycleServiceImpl : DocLifeCycleService {

    private val docCheckerStorage = DocCheckerStorage

    override fun isOperationAccepted(document: Document, newStatus: DocStatus): DocOperationCheckResult {
        val docOperationCheckers = docCheckerStorage.getDocOperationCheckers(document.docType, newStatus)

        val notSuccessfulCheckErrors = docOperationCheckers.asSequence()
            .map { checker -> checker.checkDocOperation(document) }
            .filterNot(CheckOperationResult::isSuccessful)
            .map(CheckOperationResult::errorMessage)
            .toList()

        return when {
            notSuccessfulCheckErrors.isNotEmpty() -> DocOperationCheckResult(false, notSuccessfulCheckErrors)
            else -> DocOperationCheckResult(true)
        }
    }

    override fun <T : Document, R> changeDocStatus(document: T, newStatus: DocStatus, action: (T) -> R): R {
        val operationAcceptedResult = isOperationAccepted(document, newStatus)
        if (!operationAcceptedResult.isSuccessful) throw IllegalArgumentException(operationAcceptedResult.errors.joinToString())
        document.docStatus = newStatus
        return action(document)
    }
}