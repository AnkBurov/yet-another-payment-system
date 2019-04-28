package io.demo.paymentsystem.initializer

import io.demo.paymentsystem.api.Account
import io.demo.paymentsystem.api.AccountState
import io.demo.paymentsystem.api.User
import io.demo.paymentsystem.api.document.DocStatus
import io.demo.paymentsystem.api.document.Payment
import io.demo.paymentsystem.dao.AccountDao
import io.demo.paymentsystem.dao.PaymentDao
import io.demo.paymentsystem.dao.UserDao
import io.demo.paymentsystem.utils.setUpSchema
import io.demo.paymentsystem.utils.transactional
import io.demo.paymentsystem.utils.useTransaction
import org.slf4j.LoggerFactory
import java.math.BigDecimal

object DataInitializer {

    private val log = LoggerFactory.getLogger(javaClass)

    fun createTables() {
        useTransaction { handle ->
            setUpSchema(handle)
        }
    }

    fun createEntities() {
        useTransaction { handle ->
            val userDao = handle transactional UserDao::class
            val accountDao = handle transactional AccountDao::class
            val paymentDao = handle transactional PaymentDao::class

            val firstUser = userDao.insert(User(null, "John", "Smith"))
            val secondUser = userDao.insert(User(null, "Jack", "Jones"))

            val firstAccount = accountDao.insert(
                Account(
                    null, "40810000000001234567", AccountState.OPEN,
                    firstUser.id, BigDecimal(1000), 810
                )
            )

            val secondAccount = accountDao.insert(
                Account(
                    null, "40810000000001234568", AccountState.OPEN,
                    secondUser.id, BigDecimal(1000), 810
                )
            )


            val payment = paymentDao.insert(
                Payment(
                    null, 1, DocStatus.CREATED, null, firstAccount,
                    secondAccount, BigDecimal.ONE, 810
                )
            )

            log.info("Entities are created")
        }
    }
}