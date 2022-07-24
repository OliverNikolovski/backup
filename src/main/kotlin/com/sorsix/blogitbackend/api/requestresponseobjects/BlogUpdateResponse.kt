package com.sorsix.blogitbackend.api.requestresponseobjects

import com.sorsix.blogitbackend.model.dto.BlogDto

data class BlogUpdateResponse(
    val blog: BlogDto?,
    val message: String
)