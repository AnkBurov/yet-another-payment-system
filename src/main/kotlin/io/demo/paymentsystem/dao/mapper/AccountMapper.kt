package io.demo.paymentsystem.dao.mapper

import io.demo.paymentsystem.api.Account
import io.demo.paymentsystem.api.AccountState
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

object AccountMapper : RowMapper<Account> {

    override fun map(rs: ResultSet, ctx: StatementContext): Account {
        return map("ACCOUNT.", rs, ctx)
    }

    fun map(tablePrefix: String, rs: ResultSet, ctx: StatementContext): Account {
        return Account(id = rs.getLong("${tablePrefix}ID"),
            accountNumber = rs.getString("${tablePrefix}ACCOUNT_NUMBER"),
            accountState = AccountState.valueOf(rs.getString("${tablePrefix}ACCOUNT_STATE")),
            balance = rs.getBigDecimal("${tablePrefix}BALANCE"),
            currencyCode = rs.getInt("${tablePrefix}CURRENCY_CODE"),
            userId = rs.getLong("${tablePrefix}USER_ID")
        )
    }
}