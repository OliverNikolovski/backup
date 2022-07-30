package com.sorsix.blogitbackend.api.requestobjects

import com.sorsix.blogitbackend.model.enumeration.Tag

data class BlogSaveRequest(
    val title: String,
    val content: String,
    val picture: ByteArray?,
    val tags: List<Tag>
)