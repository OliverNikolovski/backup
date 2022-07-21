package com.sorsix.blogitbackend.service.impl

import com.sorsix.blogitbackend.model.enumeration.Role
import com.sorsix.blogitbackend.repository.UserRepository
import com.sorsix.blogitbackend.service.UserService
import com.sorsix.blogitbackend.model.User
import com.sorsix.blogitbackend.model.results.user.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder
) : UserService {

    //BE CAREFUL / Possibly bug
    override fun register(
        username: String,
        password: String,
        repeatPassword: String,
        email: String,
        shortBio: String,
        profilePictureStream: InputStream,
        role: Role
    ): UserResult {
        if (username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty())
            return InputMissing("You must enter username, password and repeat password")

        if (password != repeatPassword)
            return PasswordsDoNotMatch("You must enter two identical passwords")

        if (userRepository.existsByUsername1(username))
            return UsernameAlreadyExists("Username already exists")

        val profilePicture = ByteArrayOutputStream()
        profilePictureStream.use { input ->
            profilePicture.use { output ->
                input.copyTo(output)
            }
        }

        val user = User(
            id = 0,
            username1 = username,
            password1 = passwordEncoder.encode(password),
            email = email,
            shortBio = shortBio,
            profilePicture = profilePicture.toByteArray(),
            role = Role.ROLE_USER,
            isAccountNonExpired1 = true,
            isAccountNonLocked1 = true,
            isCredentialsNonExpired1 = true,
            isEnabled1 = true
        )

        return UserCreated(userRepository.save(user))

    }

    //BE CAREFUL / Possibly bug
    override fun loadUserByUsername(username: String): UserDetails? {
        return userRepository.findByUsername1(username)
    }
}