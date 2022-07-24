package com.sorsix.blogitbackend.api.requestresponseobjects

data class BlogDeleteRequest(
    val userId: Long,
    val blogId: Long
)