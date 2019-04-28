package io.demo.paymentsystem.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.demo.paymentsystem.dao.mapper.AccountMapper
import io.demo.paymentsystem.dao.mapper.PaymentMapper
import io.demo.paymentsystem.dao.mapper.UserMapper
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import java.time.Duration

object Holder {

    val jdbi = Jdbi.create("jdbc:h2:file:./payment_system", "sa", "").apply {
        installPlugin(SqlObjectPlugin())
        installPlugin(KotlinPlugin())
        installPlugin(KotlinSqlObjectPlugin())
        registerRowMapper(UserMapper)
        registerRowMapper(AccountMapper)
        registerRowMapper(PaymentMapper)
    }

    val bankAprovingInterval = Duration.ofSeconds(10)

    val objectMapper = ObjectMapper().registerKotlinModule()
}

