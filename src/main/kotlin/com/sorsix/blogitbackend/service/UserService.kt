package com.sorsix.blogitbackend.service

import com.sorsix.blogitbackend.model.enumeration.Role
import com.sorsix.blogitbackend.model.results.user.UserResult
import org.springframework.security.core.userdetails.UserDetailsService
import java.io.InputStream


interface UserService: UserDetailsService {
    fun register(
        username: String,
        password: String,
        repeatPassword: String,
        email: String,
        shortBio: String,
        profilePictureStream: InputStream,
        role: Role
    ): UserResult
}