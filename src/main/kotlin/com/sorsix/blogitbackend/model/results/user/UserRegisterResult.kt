package com.sorsix.blogitbackend.model.results.user

import com.sorsix.blogitbackend.model.User
import com.sorsix.blogitbackend.model.dto.UserDto

sealed interface UserRegisterResult {
    fun success(): Boolean
}

data class UserRegistered(val user: UserDto) : UserRegisterResult {
    override fun success() = true
}

data class InvalidUsername(val message: String) : UserRegisterResult {
    override fun success() = false
}

data class InvalidPassword(val message: String) : UserRegisterResult {
    override fun success() = false

}

data class PasswordsDoNotMatch(val message: String) : UserRegisterResult {
    override fun success() = false
}

data class UsernameTaken(val message: String) : UserRegisterResult {
    override fun success() = false
}

data class UsernameRegistrationError(val message: String) : UserRegisterResult {
    override fun success() = false
}





