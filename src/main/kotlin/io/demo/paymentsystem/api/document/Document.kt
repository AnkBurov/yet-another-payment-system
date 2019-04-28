package io.demo.paymentsystem.api.document

open class Document(
        var id: Long? = null,

        var version: Long,

        val docType: DocType,

        var docStatus: DocStatus,

        val description: String? = null
) {
}