package com.sorsix.blogitbackend.model

import com.sorsix.blogitbackend.model.enumeration.Tag
import java.time.ZonedDateTime

data class BlogView(
    val title: String,
    val content: String,
    val dateCreated: ZonedDateTime,
    val numberOfLikes: Int,
    val estimatedReadTime: Int,
    val username: String,
    val tags: List<Tag>
)
