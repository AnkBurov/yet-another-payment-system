package io.demo.paymentsystem.dao

import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.api.document.Payment
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface PaymentDao : DocumentDao {

    @SqlUpdate(
        """
        INSERT INTO PAYMENT_SYSTEM.PAYMENT(ID, AMOUNT, CURRENCY_CODE, PAYEE_ID, PAYER_ID, )
        VALUES (:id, :amount, :currencyCode, :payee.id, :payer.id)
    """
    )
    fun insertPayment(@BindBean payment: Payment): Int

    fun insert(payment: Payment): Payment {
        val documentId = insertDocument(payment)
        payment.id = documentId
        insertPayment(payment)
        return payment
    }

    @SqlQuery(
        """
        SELECT DOCUMENT.ID, DOCUMENT.VERSION, DOCUMENT.DESCRIPTION, DOCUMENT.DOC_STATUS, DOCUMENT.DOC_TYPE,
             PAYMENT.AMOUNT, PAYMENT.CURRENCY_CODE, PAYMENT.PAYEE_ID, PAYMENT.PAYER_ID,
             PAYEE_ACCOUNT.ID as PAYEE_ACCOUNT_ID, PAYEE_ACCOUNT.ACCOUNT_NUMBER as PAYEE_ACCOUNT_ACCOUNT_NUMBER,
                PAYEE_ACCOUNT.ACCOUNT_STATE as PAYEE_ACCOUNT_ACCOUNT_STATE, PAYEE_ACCOUNT.BALANCE as PAYEE_ACCOUNT_BALANCE,
                PAYEE_ACCOUNT.CURRENCY_CODE as PAYEE_ACCOUNT_CURRENCY_CODE, PAYEE_ACCOUNT.USER_ID as PAYEE_ACCOUNT_USER_ID,
             PAYER_ACCOUNT.ID as PAYER_ACCOUNT_ID, PAYER_ACCOUNT.ACCOUNT_NUMBER as PAYER_ACCOUNT_ACCOUNT_NUMBER,
                PAYER_ACCOUNT.ACCOUNT_STATE as PAYER_ACCOUNT_ACCOUNT_STATE, PAYER_ACCOUNT.BALANCE as PAYER_ACCOUNT_BALANCE,
                PAYER_ACCOUNT.CURRENCY_CODE as PAYER_ACCOUNT_CURRENCY_CODE, PAYER_ACCOUNT.USER_ID as PAYER_ACCOUNT_USER_ID
        FROM PAYMENT_SYSTEM.DOCUMENT
        JOIN PAYMENT_SYSTEM.PAYMENT ON DOCUMENT.ID = PAYMENT.ID
        JOIN PAYMENT_SYSTEM.ACCOUNT PAYEE_ACCOUNT ON PAYMENT.PAYEE_ID = PAYEE_ACCOUNT.ID
        JOIN PAYMENT_SYSTEM.ACCOUNT PAYER_ACCOUNT ON PAYMENT.PAYER_ID = PAYER_ACCOUNT.ID
        WHERE DOCUMENT.DOC_STATUS = :docStatus
    """
    )
    fun findByDocStatus(docStatus: DocStatus): List<Payment>
}