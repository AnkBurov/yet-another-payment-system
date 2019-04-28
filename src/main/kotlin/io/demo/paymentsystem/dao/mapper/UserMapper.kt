package io.demo.paymentsystem.dao.mapper

import io.demo.paymentsystem.api.User
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet

object UserMapper : RowMapper<User> {
    override fun map(rs: ResultSet, ctx: StatementContext): User {
        return User(id = rs.getLong("USER.ID"),
                firstName = rs.getString("USER.FIRST_NAME"),
                lastName = rs.getString("USER.LAST_NAME")
        )
    }
}