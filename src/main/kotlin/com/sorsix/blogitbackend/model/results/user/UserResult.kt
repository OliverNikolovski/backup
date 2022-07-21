package com.sorsix.blogitbackend.model.results.user

import com.sorsix.blogitbackend.model.User

sealed interface UserResult {
    fun success(): Boolean
}

data class UserCreated(val user: User) : UserResult {
    override fun success() = true
}

data class InputMissing (val message: String) : UserResult{
    override fun success() = false
}

data class PasswordsDoNotMatch (val message: String) : UserResult{
    override fun success() = false
}

data class UsernameAlreadyExists (val message: String) : UserResult{
    override fun success() = false
}






