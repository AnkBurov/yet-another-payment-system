package io.demo.paymentsystem.unit

import io.demo.paymentsystem.dao.AccountDao
import io.demo.paymentsystem.dao.UserDao
import io.demo.paymentsystem.utils.createAccount
import io.demo.paymentsystem.utils.dao
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.math.BigDecimal

class AccountDaoTest {

    private val accountDao = dao(AccountDao::class)
    private val userDao = dao(UserDao::class)

    @Test
    fun testInsert() {
        val insertedAccount = createAccount(userDao, accountDao)

        assertNotNull(insertedAccount.id)
    }

    @Test
    fun testFindOne() {
        val insertedAccount = createAccount(userDao, accountDao)

        val foundAccount = accountDao.findOne(insertedAccount.id)

        assertNotNull(foundAccount)
        assertEquals(insertedAccount.id, foundAccount?.id)
    }

    @Test
    fun testFindByAccountNumber() {
        val insertedAccount = createAccount(userDao, accountDao)

        val foundAccount = accountDao.findByAccountNumber(insertedAccount.accountNumber)

        assertNotNull(foundAccount)
        assertEquals(insertedAccount.id, foundAccount?.id)
    }

    @Test
    fun testAddMoney() {
        val insertedAccount = createAccount(userDao, accountDao)
        val addMoney = BigDecimal.TEN

        val updatedRows = accountDao.addMoney(insertedAccount.id, addMoney)
        assertEquals(1, updatedRows)

        val updatedBalance = accountDao.findOne(insertedAccount.id)?.balance?.setScale(0)
        assertEquals(insertedAccount.balance + addMoney, updatedBalance)
    }

    @Test
    fun testSubtractMoney() {
        val insertedAccount = createAccount(userDao, accountDao)
        val subtractMoney = BigDecimal.TEN

        val updatedRows = accountDao.subtractMoney(insertedAccount.id, subtractMoney)
        assertEquals(1, updatedRows)

        val updatedBalance = accountDao.findOne(insertedAccount.id)?.balance?.setScale(0)
        assertEquals(insertedAccount.balance - subtractMoney, updatedBalance)
    }

    @Test(expected = UnableToExecuteStatementException::class)
    fun testNegativeBalance() {
        val insertedAccount = createAccount(userDao, accountDao)

        val subtractMoney = BigDecimal.valueOf(100)

        accountDao.subtractMoney(insertedAccount.id, subtractMoney)
    }
}