package com.sorsix.blogitbackend.api.requestobjects

data class UpvoteRequest(
    val userId: Long,
    val blogId: Long
)
