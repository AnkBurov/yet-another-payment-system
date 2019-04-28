package io.demo.paymentsystem.service.account

import io.demo.paymentsystem.api.Account
import io.demo.paymentsystem.config.Holder.jdbi
import io.demo.paymentsystem.dao.AccountDao
import io.demo.paymentsystem.utils.transactional
import org.jdbi.v3.core.Handle
import java.math.BigDecimal

object AccountServiceImpl : AccountService {

    private val accountDao = jdbi.onDemand(AccountDao::class.java)

    override fun getAccount(id: Long): Account {
        // in a real application there would be an additional criteria by user's org id to prevent security issues
        return accountDao.findOne(id) ?: throw IllegalArgumentException("Account with id $id is not found")
    }

    override fun getAccount(accountNumber: String): Account {
        return accountDao.findByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account with id $accountNumber is not found")
    }

    /**
     * Subtract money from the account
     *
     * Less than zero trigger check will do the work in case of a non-repeatable read, so no repeatable read isolation
     * is needed
     */
    override fun subtractMoney(handle: Handle, account: Account, amount: BigDecimal, currencyCode: Int): BigDecimal {
        account.balance -= amount
        if (account.balance < BigDecimal.ZERO) throw IllegalArgumentException("Balance cannot be negative number")
        handle.transactional(AccountDao::class).subtractMoney(account.id, amount)
        return amount
    }

    override fun addMoney(handle: Handle, account: Account, amount: BigDecimal, currencyCode: Int) {
        account.balance += amount
        handle.transactional(AccountDao::class).addMoney(account.id, amount)
    }
}
