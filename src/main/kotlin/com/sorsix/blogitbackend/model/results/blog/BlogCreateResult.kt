package com.sorsix.blogitbackend.model.results.blog

import com.sorsix.blogitbackend.model.Blog
import com.sorsix.blogitbackend.model.dto.BlogDto

sealed interface BlogCreateResult : BlogResult

data class BlogCreated(val blogDto: BlogDto) : BlogCreateResult {
    override fun success() = true
}

data class BlogCreateError(val message: String) : BlogCreateResult {
    override fun success() = false
}

