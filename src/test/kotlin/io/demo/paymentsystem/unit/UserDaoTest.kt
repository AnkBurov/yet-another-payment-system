package io.demo.paymentsystem.unit

import io.demo.paymentsystem.api.User
import io.demo.paymentsystem.dao.UserDao
import io.demo.paymentsystem.utils.dao
import org.junit.Assert.assertNotNull
import org.junit.Test
import kotlin.test.assertEquals

class UserDaoTest {

    private val userDao = dao(UserDao::class)

    @Test
    fun testInsert() {
        val insertedUser = userDao.insert(User(null, "first", "second"))

        assertNotNull(insertedUser.id)
    }

    @Test
    fun testFind() {
        val insertedUser = userDao.insert(User(null, "first", "second"))

        val foundUser = userDao.find(insertedUser.id)

        assertNotNull(foundUser)
        assertEquals(insertedUser.id, foundUser?.id)
    }
}