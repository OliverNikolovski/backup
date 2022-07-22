package com.sorsix.blogitbackend.service.impl

import com.sorsix.blogitbackend.model.enumeration.Role
import com.sorsix.blogitbackend.repository.UserRepository
import com.sorsix.blogitbackend.service.UserService
import com.sorsix.blogitbackend.model.User
import com.sorsix.blogitbackend.model.results.user.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder
) : UserService {
    override fun register(
        username: String,
        password: String,
        repeatedPassword: String,
        email: String?,
        shortBio: String?,
        profilePicture: InputStream?,
        role: Role?
    ): UserRegisterResult {

        if (username.isEmpty() || containsWhiteSpace(username))
            return InvalidUsername("Invalid username: $username")

        if (password.isBlank() || password.length < 3)
            return InvalidPassword("Invalid password: $password")

        if (password != repeatedPassword)
            return PasswordsDoNotMatch("Passwords do not match.")

        if (userRepository.existsByUsername(username))
            return UsernameTaken("Username $username taken.")

//        val profilePicture = ByteArrayOutputStream()
//        profilePictureStream.use { input ->
//            profilePicture.use { output ->
//                input.copyTo(output)
//            }
//        }

        val user = User(
            id = 0,
            username = username,
            password = passwordEncoder.encode(password),
            email = email,
            shortBio = shortBio,
            profilePicture = null
        )

        return UserRegistered(userRepository.save(user))
    }

    override fun loadUserByUsername(username: String) = userRepository.findByUsername(username)

    private fun containsWhiteSpace(str: String) = str.matches(Regex(""".*\s+.*"""))
}