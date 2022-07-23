package com.sorsix.blogitbackend.model.results.blog

import com.sorsix.blogitbackend.model.dto.BlogDto


sealed interface BlogLikeResult: BlogResult

data class BlogLiked(val blogDto: BlogDto) : BlogLikeResult {
    override fun success() = true
}

data class BlogUnliked(val message: String) : BlogLikeResult {
    override fun success() = true
}

data class BlogLikeError(val message: String) : BlogLikeResult {
    override fun success() = false
}

