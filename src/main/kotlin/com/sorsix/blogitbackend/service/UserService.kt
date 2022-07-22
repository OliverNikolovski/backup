package com.sorsix.blogitbackend.service

import com.sorsix.blogitbackend.model.enumeration.Role
import com.sorsix.blogitbackend.model.results.user.UserRegisterResult
import org.springframework.security.core.userdetails.UserDetailsService
import java.io.InputStream


interface UserService: UserDetailsService {
    fun register(
        username: String,
        password: String,
        repeatedPassword: String,
        email: String?,
        shortBio: String?,
        profilePicture: InputStream?,
        role: Role?
    ): UserRegisterResult
}