package com.sorsix.blogitbackend.api.requestresponseobjects.blog

data class BlogUpdateRequest(
    val title: String,
    val content: String,
    val userId: Long
)
