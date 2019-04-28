package io.demo.paymentsystem.dao

import io.demo.paymentsystem.api.User
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface UserDao : Dao {

    @SqlUpdate("""
        INSERT INTO PAYMENT_SYSTEM.USER(FIRST_NAME, LAST_NAME) VALUES (:firstName, :lastName)
    """)
    @GetGeneratedKeys("id")
    fun insertUser(@BindBean user: User): Long

    fun insert(user: User): User {
        val userId = insertUser(user)
        user.id = userId
        return user
    }

    @SqlQuery("""
        SELECT USER.ID, USER.FIRST_NAME, USER.LAST_NAME
        FROM PAYMENT_SYSTEM.USER
        WHERE USER.ID = :userId
    """)
    fun find(userId: Long?): User?
}