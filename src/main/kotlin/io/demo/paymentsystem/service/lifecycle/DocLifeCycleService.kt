package io.demo.paymentsystem.service.lifecycle

import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.api.document.Document
import io.demo.paymentsystem.api.lifecycle.check.DocOperationCheckResult

/**
 * Service for document life cycles
 */
interface DocLifeCycleService {

    /**
     * Check if operation is accepted and can be executed
     *
     * @return isSuccessful
     */
    fun isOperationAccepted(document: Document, newStatus: DocStatus): DocOperationCheckResult

    /**
     * Change document status
     *
     * @param document changing document
     * @param newStatus new status of the document
     * @param action callback what to do after successful status changing
     * <T> type of document
     * <R> type of returned value in the action argument
     */
    fun <T : Document, R> changeDocStatus(document: T, newStatus: DocStatus, action: (T) -> (R)): R
}