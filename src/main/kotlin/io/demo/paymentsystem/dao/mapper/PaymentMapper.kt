package io.demo.paymentsystem.dao.mapper

import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.api.document.Payment
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

object PaymentMapper : RowMapper<Payment> {
    override fun map(rs: ResultSet, ctx: StatementContext): Payment {
        val payeeAccount = AccountMapper.map("PAYEE_ACCOUNT_", rs, ctx)
        val payerAccount = AccountMapper.map("PAYER_ACCOUNT_", rs, ctx)

        return Payment(
            id = rs.getLong("DOCUMENT.ID"),
            version = rs.getLong("DOCUMENT.VERSION"),
            docStatus = DocStatus.valueOf(rs.getString("DOCUMENT.DOC_STATUS")),
            description = rs.getString("DOCUMENT.DESCRIPTION"),
            payee = payeeAccount,
            payer = payerAccount,
            amount = rs.getBigDecimal("PAYMENT.AMOUNT"),
            currencyCode = rs.getInt("PAYMENT.CURRENCY_CODE")
        )
    }
}