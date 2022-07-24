package com.sorsix.blogitbackend.api.responseobjects

import com.sorsix.blogitbackend.model.dto.BlogDto

data class BlogDeleteResponse(
    val blog: BlogDto?,
    val message: String
)
