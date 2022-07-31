package com.sorsix.blogitbackend.model.dto

import com.sorsix.blogitbackend.model.enumeration.Tag
import java.time.ZonedDateTime

data class BlogDto(
    val id: Long,
    val title: String,
    val content: String,
    val dateCreated: ZonedDateTime,
    val estimatedReadTime: Int,
    val numberOfLikes: Int,
    val username: String,
    val tags: List<Tag>,
    val picture: ByteArray? = null,
    val pictureFormat: String? = null,
    val isLikedByCurrentlyLoggedInUser: Boolean? = null
)
