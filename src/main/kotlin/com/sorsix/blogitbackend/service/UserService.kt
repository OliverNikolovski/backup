package com.sorsix.blogitbackend.service

import com.sorsix.blogitbackend.model.User
import com.sorsix.blogitbackend.model.enumeration.Role
import com.sorsix.blogitbackend.model.results.follow.FollowResult
import com.sorsix.blogitbackend.model.results.user.UserRegisterResult
import org.springframework.security.core.userdetails.UserDetailsService
import java.io.InputStream


interface UserService: UserDetailsService {

    fun findByIdOrThrow(id: Long): User

    fun findByUsername(username: String): User?

    fun existsByUsername(username: String): Boolean

    fun register(
        username: String,
        password: String,
        repeatedPassword: String,
        email: String?,
        shortBio: String?,
        profilePicture: ByteArray?
    ): UserRegisterResult

    fun followOrUnfollow(followerId: Long, followedId: Long): FollowResult

    fun getFollowersForUser(userId: Long): List<User>

    fun getFollowingForUser(userId: Long): List<User>

}