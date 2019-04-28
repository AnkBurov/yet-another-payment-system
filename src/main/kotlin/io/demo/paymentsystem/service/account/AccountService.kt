package io.demo.paymentsystem.service.account

import io.demo.paymentsystem.api.Account
import org.jdbi.v3.core.Handle
import java.math.BigDecimal

/**
 * Service for accounts
 */
interface AccountService {

    /**
     * Get an account by id
     */
    fun getAccount(id: Long): Account

    /**
     * Get an account by account number
     */
    fun getAccount(accountNumber: String): Account

    /**
     * Subtract money from the account
     * @return subtracted money
     */
    fun subtractMoney(handle: Handle, account: Account, amount: BigDecimal, currencyCode: Int): BigDecimal

    /**
     * Add money to the account
     */
    fun addMoney(handle: Handle, account: Account, amount: BigDecimal, currencyCode: Int)
}