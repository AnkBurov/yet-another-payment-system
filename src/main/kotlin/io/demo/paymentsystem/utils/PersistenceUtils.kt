package io.demo.paymentsystem.utils

import io.demo.paymentsystem.config.Holder
import io.demo.paymentsystem.dao.Dao
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import org.jdbi.v3.core.kotlin.useTransactionUnchecked
import kotlin.reflect.KClass

fun <T : Dao> dao(daoKClass: KClass<T>): T {
    return Holder.jdbi.onDemand(daoKClass.java)
}

fun useTransaction(block: (Handle) -> Unit) {
    Holder.jdbi.useTransactionUnchecked {
        block(it)
    }
}

fun <T> inTransaction(block: (Handle) -> T): T {
    return Holder.jdbi.inTransactionUnchecked {
        block(it)
    }
}

infix fun <T : Dao> Handle.transactional(daoKClass: KClass<T>): T {
    return this.attach(daoKClass.java)
}

fun setUpSchema(handle: Handle) {
    Holder::class.java.classLoader.getResource("init_script.sql")
        .readText()
        .split(";")
        .forEach { statement ->
            handle.execute(statement)
        }
}