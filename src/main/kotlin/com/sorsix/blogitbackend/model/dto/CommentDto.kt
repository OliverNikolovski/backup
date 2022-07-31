package com.sorsix.blogitbackend.model.dto

import java.time.ZonedDateTime
import javax.persistence.Column

data class CommentDto(
    val id: Long,

    val content: String,

    val dateCreated: ZonedDateTime,

    val numberOfLikes: Int,

    val username: String,

    val blog_id: Long,

    val isLikedByCurrentlyLoggedInUser: Boolean? = null
)
