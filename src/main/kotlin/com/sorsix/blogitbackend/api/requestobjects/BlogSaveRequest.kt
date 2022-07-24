package com.sorsix.blogitbackend.api.requestobjects

data class BlogSaveRequest(
    val title: String,
    val content: String,
    val profilePicture: ByteArray?
)