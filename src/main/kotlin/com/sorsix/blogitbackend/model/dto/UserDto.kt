package com.sorsix.blogitbackend.model.dto

import com.sorsix.blogitbackend.model.enumeration.Role
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated

data class UserDto(

    val username: String,

    val email: String?,

    val shortBio: String?,

    val profilePicture: ByteArray?,

    val profilePictureFormat: String?,

    val role: Role = Role.ROLE_USER,

)
