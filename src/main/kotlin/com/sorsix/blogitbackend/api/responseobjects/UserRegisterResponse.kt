package com.sorsix.blogitbackend.api.responseobjects

import com.sorsix.blogitbackend.model.dto.UserDto

data class UserRegisterResponse(
    val user: UserDto?,
    val message: String
)
