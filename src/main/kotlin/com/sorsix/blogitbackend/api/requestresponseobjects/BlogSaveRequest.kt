package com.sorsix.blogitbackend.api.requestresponseobjects

data class BlogSaveRequest(
    val title: String,
    val content: String,
    val userId: Long,
    val profilePicture: ByteArray?
)