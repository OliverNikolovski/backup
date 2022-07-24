package com.sorsix.blogitbackend.api.requestresponseobjects

import com.sorsix.blogitbackend.model.dto.BlogDto

data class BlogUpvoteResponse(
    val blog: BlogDto?,
    val message: String
)
