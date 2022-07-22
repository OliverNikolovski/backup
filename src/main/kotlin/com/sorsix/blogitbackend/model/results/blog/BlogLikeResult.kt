package com.sorsix.blogitbackend.model.results.blog

import com.sorsix.blogitbackend.model.Blog


sealed interface BlogLikeResult: BlogResult

data class BlogLiked(val blog: Blog) : BlogLikeResult {
    override fun success() = true
}

data class BlogUnliked(val message: String) : BlogLikeResult {
    override fun success() = true
}

data class BlogLikeError(val message: String) : BlogLikeResult {
    override fun success() = false
}

