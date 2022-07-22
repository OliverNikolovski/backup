package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.User
import com.sorsix.blogitbackend.model.enumeration.Role
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class UserRepositoryTest : AbstractTest() {
    @Autowired
    lateinit var userRepository: UserRepository

    @AfterEach
    fun cleanup() {
        jdbcTempate.execute("truncate table users cascade ")
    }

    @Test
    fun `save user`() {
        val user = User(0, "john.doe", "pass", "email", "shortBio", role = Role.ROLE_USER)
        val savedUser = userRepository.save(user)

        assertEquals(user.username, savedUser.username)
        assertEquals(user.password, savedUser.password)
        assertEquals(user.email, savedUser.email)
        assertEquals(user.shortBio, savedUser.shortBio)
        assertEquals(user.profilePicture, savedUser.profilePicture)
        assertEquals(user.role, savedUser.role)
        assertEquals(user.isAccountNonExpired, savedUser.isAccountNonExpired)
    }

    @Test
    fun findByUsername() {
        val savedUser = userRepository.save(User(0, "john.doe", "pass", "email", "shortBio", role = Role.ROLE_USER))
        val foundUser = userRepository.findByUsername1("john.doe")

        assertEquals(foundUser?.username, savedUser.username)
        assertEquals(foundUser?.password, savedUser.password)
        assertEquals(foundUser?.email, savedUser.email)
        assertEquals(foundUser?.shortBio, savedUser.shortBio)
        assertEquals(foundUser?.profilePicture, savedUser.profilePicture)
        assertEquals(foundUser?.role, savedUser.role)
        assertEquals(foundUser?.isAccountNonExpired, savedUser.isAccountNonExpired)
    }

    @Test
    fun existsByUsername() {
        userRepository.save(User(0, "john.doe", "pass", "email", "shortBio", role = Role.ROLE_USER))
        assertTrue(userRepository.existsByUsername1("john.doe"))
    }
}