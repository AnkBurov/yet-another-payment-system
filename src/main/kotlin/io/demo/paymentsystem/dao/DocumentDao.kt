package io.demo.paymentsystem.dao

import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.api.document.Document
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface DocumentDao : Dao {

    @SqlUpdate("""
        INSERT INTO PAYMENT_SYSTEM.DOCUMENT(DESCRIPTION, DOC_STATUS, DOC_TYPE, VERSION)
        VALUES (:description, :docStatus, :docType, :version)
    """)
    @GetGeneratedKeys("id")
    fun insertDocument(@BindBean document: Document): Long

    @SqlUpdate("""
        UPDATE PAYMENT_SYSTEM.DOCUMENT
        SET DOC_STATUS = :docStatus,
            VERSION = VERSION + 1
        WHERE ID = :documentId
    """)
    fun updateDocStatus(documentId: Long?, docStatus: DocStatus): Int
}