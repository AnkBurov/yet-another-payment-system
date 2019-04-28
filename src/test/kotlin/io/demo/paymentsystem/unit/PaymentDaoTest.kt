package io.demo.paymentsystem.unit

import io.demo.paymentsystem.api.Account
import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.api.document.Payment
import io.demo.paymentsystem.dao.AccountDao
import io.demo.paymentsystem.dao.PaymentDao
import io.demo.paymentsystem.dao.UserDao
import io.demo.paymentsystem.utils.createAccount
import io.demo.paymentsystem.utils.dao
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class PaymentDaoTest {

    private val paymentDao = dao(PaymentDao::class)
    private val accountDao = dao(AccountDao::class)
    private val userDao = dao(UserDao::class)

    @Test
    fun testInsert() {
        val payeeAccount = createAccount(userDao, accountDao)
        val payerAccount = createAccount(userDao, accountDao)

        val insertedPayment = paymentDao.insert(buildPayment(payerAccount, payeeAccount))

        assertNotNull(insertedPayment.id)
        assertEquals(payeeAccount.id, insertedPayment.payee.id)
        assertEquals(payerAccount.id, insertedPayment.payer.id)
    }

    @Test
    fun testUpdateDocStatus() {
        val payeeAccount = createAccount(userDao, accountDao)
        val payerAccount = createAccount(userDao, accountDao)

        val insertedPayment = paymentDao.insert(buildPayment(payerAccount, payeeAccount))

        val updatedRows = paymentDao.updateDocStatus(insertedPayment.id, DocStatus.DECLINED)

        assertEquals(1, updatedRows)
    }

    @Test
    fun testFindByDocStatus() {
        val payeeAccount = createAccount(userDao, accountDao)
        val payerAccount = createAccount(userDao, accountDao)

        val insertedPayment = paymentDao.insert(buildPayment(payerAccount, payeeAccount))

        val foundPayments = paymentDao.findByDocStatus(insertedPayment.docStatus)

        assertFalse(foundPayments.isEmpty())
        assertTrue(foundPayments.any { it.id == insertedPayment.id })
    }

    private fun buildPayment(
        payerAccount: Account,
        payeeAccount: Account
    ): Payment {
        return Payment(
            payer = payerAccount,
            payee = payeeAccount,
            amount = BigDecimal.TEN,
            currencyCode = 810,
            docStatus = DocStatus.CREATED
        )
    }
}