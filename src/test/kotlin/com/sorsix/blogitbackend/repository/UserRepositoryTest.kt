package com.sorsix.blogitbackend.repository

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.User
import com.sorsix.blogitbackend.model.enumeration.Role
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.time.ZonedDateTime

class UserRepositoryTest : AbstractTest() {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var blogRepository: BlogRepository

    @AfterEach
    fun cleanup() {
        jdbcTempate.execute("truncate table users cascade ")
        jdbcTempate.execute("truncate table bookmarks cascade ")
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
        val foundUser = userRepository.findByUsername("john.doe")

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
        assertTrue(userRepository.existsByUsername("john.doe"))
    }

    @Test
    fun `testing follow`() {
        val follower = userRepository.save(User(0, "john.doe", "pass", "email", "shortBio", role = Role.ROLE_USER))
        val followed = userRepository.save(User(0, "john.doe1", "pass", "email1", "shortBio", role = Role.ROLE_USER))

        val isUserFollowed = userRepository.follow(follower.id, followed.id)

        assertEquals(1, isUserFollowed)
    }

    @Test
    fun `testing userIsFollowing`() {
        val follower = userRepository.save(User(0, "john.doe", "pass", "email", "shortBio", role = Role.ROLE_USER))
        val followed = userRepository.save(User(0, "john.doe1", "pass", "email1", "shortBio", role = Role.ROLE_USER))

        userRepository.follow(follower.id, followed.id)

        val followers = userRepository.getFollowingForUser(follower.id)

        assertEquals(1, followers.size)
    }

    @Test
    fun `testing userIsFollowedBy`() {
        val follower = userRepository.save(User(0, "john.doe", "pass", "email", "shortBio", role = Role.ROLE_USER))
        val followed = userRepository.save(User(0, "john.doe1", "pass", "email1", "shortBio", role = Role.ROLE_USER))

        userRepository.follow(follower.id, followed.id)

        val followedBy = userRepository.getFollowersForUser(followed.id)

        assertEquals(1, followedBy.size)
    }
}