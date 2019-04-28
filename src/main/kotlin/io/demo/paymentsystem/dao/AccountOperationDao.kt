package io.demo.paymentsystem.dao

import io.demo.paymentsystem.api.document.AccountOperation
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface AccountOperationDao : DocumentDao {


    @SqlUpdate("""
        INSERT INTO PAYMENT_SYSTEM.ACCOUNT_OPERATION(ID, ACCOUNT_OPERATION_TYPE, AMOUNT, ACCOUNT_ID)
        VALUES (:id, :accountOperationType, :amount, :account.id)
    """)
    fun insertAccountOperation(@BindBean accountOperation: AccountOperation): Int


    fun insert(accountOperation: AccountOperation): AccountOperation {
        val documentId = insertDocument(accountOperation)
        accountOperation.id = documentId
        insertAccountOperation(accountOperation)
        return accountOperation
    }
}