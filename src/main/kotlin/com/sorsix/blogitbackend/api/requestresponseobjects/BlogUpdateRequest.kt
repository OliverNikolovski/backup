package com.sorsix.blogitbackend.api.requestresponseobjects

data class BlogUpdateRequest(
    val title: String,
    val content: String,
    val userId: Long
)
