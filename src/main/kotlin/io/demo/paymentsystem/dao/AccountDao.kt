package io.demo.paymentsystem.dao

import io.demo.paymentsystem.api.Account
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.math.BigDecimal

interface AccountDao : Dao {

    @SqlUpdate(
        """
        INSERT INTO PAYMENT_SYSTEM.ACCOUNT(ACCOUNT_NUMBER, ACCOUNT_STATE, BALANCE, CURRENCY_CODE, USER_ID)
        VALUES (:accountNumber, :accountState, :balance, :currencyCode, :userId)
    """
    )
    @GetGeneratedKeys("id")
    fun insertAccount(@BindBean account: Account): Long

    fun insert(account: Account): Account {
        val accountId = insertAccount(account)
        account.id = accountId
        return account
    }

    @SqlQuery("""
        SELECT ACCOUNT.ID, ACCOUNT.ACCOUNT_NUMBER, ACCOUNT.ACCOUNT_STATE, ACCOUNT.BALANCE, ACCOUNT.CURRENCY_CODE, ACCOUNT.USER_ID
        FROM PAYMENT_SYSTEM.ACCOUNT
        WHERE ACCOUNT.ID = :accountId
    """)
    fun findOne(accountId: Long?): Account?

    @SqlQuery("""
        SELECT ACCOUNT.ID, ACCOUNT.ACCOUNT_NUMBER, ACCOUNT.ACCOUNT_STATE, ACCOUNT.BALANCE, ACCOUNT.CURRENCY_CODE, ACCOUNT.USER_ID
        FROM PAYMENT_SYSTEM.ACCOUNT
        WHERE ACCOUNT.ACCOUNT_NUMBER = :accountNumber
    """)
    fun findByAccountNumber(accountNumber: String): Account?

    @SqlUpdate("""
        UPDATE PAYMENT_SYSTEM.ACCOUNT
        SET BALANCE = BALANCE + :amount
        WHERE ID = :accountId
    """)
    fun addMoney(accountId: Long?, amount: BigDecimal): Int

    @SqlUpdate("""
        UPDATE PAYMENT_SYSTEM.ACCOUNT
        SET BALANCE = BALANCE - :amount
        WHERE ID = :accountId
    """)
    fun subtractMoney(accountId: Long?, amount: BigDecimal): Int
}