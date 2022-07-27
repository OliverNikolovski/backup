package com.sorsix.blogitbackend.api.requestobjects

import org.springframework.web.multipart.MultipartFile

data class UserRequest(
    val username: String,
    val password: String,
    val repeatedPassword: String,
    val email: String?,
    val shortBio: String?,
    val image: MultipartFile?
) {
}