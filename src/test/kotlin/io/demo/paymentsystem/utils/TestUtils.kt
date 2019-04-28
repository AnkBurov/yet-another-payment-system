package io.demo.paymentsystem.utils

import com.fasterxml.jackson.module.kotlin.readValue
import io.demo.paymentsystem.api.Account
import io.demo.paymentsystem.api.AccountState
import io.demo.paymentsystem.api.User
import io.demo.paymentsystem.config.Holder
import io.demo.paymentsystem.dao.AccountDao
import io.demo.paymentsystem.dao.UserDao
import io.ktor.server.testing.TestApplicationResponse
import java.math.BigDecimal
import java.util.*

fun <T> T.asJson(): String {
    return Holder.objectMapper.writeValueAsString(this)
}

inline fun <reified T: Any> TestApplicationResponse.readJson(): T? {
    return this.content?.let { Holder.objectMapper.readValue(it) }
}

fun createAccount(userDao: UserDao, accountDao: AccountDao): Account {
    val insertedUser = userDao.insert(User(null, "first", "second"))

    return accountDao.insert(
        Account(
            null, UUID.randomUUID().toString(),
            AccountState.OPEN, insertedUser.id, BigDecimal.TEN, 5
        )
    )
}