package com.sorsix.blogitbackend.model.results.blog

import com.sorsix.blogitbackend.model.Blog

sealed interface BlogCreateResult : BlogResult

data class BlogCreated(val blog: Blog) : BlogCreateResult {
    override fun success() = true
}

data class BlogCreateError(val message: String) : BlogCreateResult {
    override fun success() = false
}

