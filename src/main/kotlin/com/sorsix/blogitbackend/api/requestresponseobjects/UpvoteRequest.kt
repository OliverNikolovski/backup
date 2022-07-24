package com.sorsix.blogitbackend.api.requestresponseobjects

data class UpvoteRequest(
    val userId: Long,
    val blogId: Long
)
