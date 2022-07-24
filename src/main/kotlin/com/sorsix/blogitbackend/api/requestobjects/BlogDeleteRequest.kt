package com.sorsix.blogitbackend.api.requestobjects

data class BlogDeleteRequest(
    val userId: Long,
    val blogId: Long
)