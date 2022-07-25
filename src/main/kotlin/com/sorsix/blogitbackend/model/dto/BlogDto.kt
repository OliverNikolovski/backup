package com.sorsix.blogitbackend.model.dto

import com.sorsix.blogitbackend.model.enumeration.Tag
import java.time.ZonedDateTime

data class BlogDto(
    val title: String,
    val content: String,
    val dateCreated: ZonedDateTime,
    val estimatedReadTime: Int,
    val numberOfLikes: Int,
    val username: String,
    val tags: List<Tag>
)
